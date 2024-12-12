package com.kieslect.file.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.kieslect.api.RemoteAuthService;
import com.kieslect.common.redis.service.RedisService;
import com.kieslect.file.domain.*;
import com.kieslect.file.domain.vo.*;
import com.kieslect.file.enums.KieslectSportTypeEnum;
import com.kieslect.file.proto.KSportProto;
import com.kieslect.file.service.*;
import com.kieslect.file.utils.DistanceCalculator;
import com.kieslect.file.utils.SportTypeMatcher;
import com.kieslect.file.utils.TimestampUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static com.kieslect.file.utils.MapToListConverter.convertListToCoordinates;
import static com.kieslect.file.utils.TrainingCenterDatabaseUtil.addXsiSchemaLocation;
import static com.kieslect.file.utils.TrainingCenterDatabaseUtil.fillTrainingCenterDatabase;

@Service
public class StravaFileServiceImpl implements IStravaFileService {

    private static final Logger logger = LoggerFactory.getLogger(StravaFileServiceImpl.class);

    //Strava域名
    private static final String STRAVA_DOMAIN = "https://www.strava.com";
    // 创建活动url
    private static final String CREATE_ACTIVITIES_URL = STRAVA_DOMAIN + "/api/v3/activities";

    // 文件上传url
    private static final String FILE_UPLOADS_URL = STRAVA_DOMAIN + "/api/v3/uploads";

    @Autowired
    private RedisService redisService;
    @Autowired
    private RemoteAuthService remoteAuthService;

    @Autowired
    ISportDetailService detailSportService;
    @Autowired
    ISportKactivityService kActivityDataService;
    @Autowired
    ISportKdeviceService kDeviceService;
    @Autowired
    ISportKdistanceService kDistanceDataService;
    @Autowired
    ISportKlocationService kLocationService;
    @Autowired
    ISportKtimerService kTimerService;


    private static final String STRAVA_FILE_NAME = "strava_file_name:";
    private static final String STRAVA_TCX_FILE = "strava_tcx_file:";
    private static final String STRAVA_SUCCESS_SAVE_DB_KEY = "strava_success_save_db_key:";
    private static final String STRAVA_TOKEN = "strava_token:";

    public static String getStravaTokenRedisKey(String redisKey) {
        return STRAVA_TOKEN + redisKey;
    }


    private static String getStravaFileNameRedisKey(String redisKey) {
        return STRAVA_FILE_NAME + redisKey;
    }

    private static String getStravaTcxFileRedisKey(String redisKey) {
        return STRAVA_TCX_FILE + redisKey;
    }

    private static String getStravaSuccessSaveDbKeyRedisKey(String redisKey) {
        return STRAVA_SUCCESS_SAVE_DB_KEY + redisKey;
    }


    private static final String LOCAL_UPLOAD_FOLDER;
    private static final String LOCAL_TCX_FOLDER;

    static {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            LOCAL_UPLOAD_FOLDER = "D:" + File.separator + "data" + File.separator + "strava_files";
            LOCAL_TCX_FOLDER = "D:" + File.separator + "data" + File.separator + "strava_tcx_files";
        } else {
            LOCAL_UPLOAD_FOLDER = File.separator + "var" + File.separator + "data" + File.separator + "strava_files";
            LOCAL_TCX_FOLDER = File.separator + "var" + File.separator + "data" + File.separator + "strava_tcx_files";
        }
        // 使用 Hutool 创建文件夹（如果文件夹不存在）
        FileUtil.mkdir(LOCAL_UPLOAD_FOLDER);
        FileUtil.mkdir(LOCAL_TCX_FOLDER);
    }

    @Override
    public int uploadFile(MultipartFile file, double offsetHours) {
        logger.info("接收到文件上传请求，文件名:{},偏移量：{}", file.getOriginalFilename(), offsetHours);
        // 使用毫秒值作为文件名
        String newFileName = String.valueOf(Instant.now().toEpochMilli());

        // 文件保存路径
        File saveFile = FileUtil.file(LOCAL_UPLOAD_FOLDER, newFileName);

        try {
            file.transferTo(saveFile);
            redisService.setCacheObject(getStravaFileNameRedisKey(newFileName), offsetHours);
            logger.info("文件保存成功，文件名:{}", newFileName);
            return 1;
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public void parseFileToDB() {
        // 从缓存中获取已经上传完成的文件名集合
        Collection<String> fileNames = redisService.keys(getStravaFileNameRedisKey("*"));
        logger.info("从缓存中获取已经上传完成的文件名:{}", fileNames);
        // 开始读取本地服务器文件
        for (String fileName : fileNames) {
            // 解析入库
            parseFileToDB(getFileNameWithoutPrefix(fileName));
        }

    }

    @Override
    public void getDBToTcx() {
        // 从缓存中获取已经上传完成的文件名集合
        Collection<String> dbkeys = redisService.keys(getStravaSuccessSaveDbKeyRedisKey("*"));
        logger.info("从缓存中获取已经解析入库成功的数据键:{}", dbkeys);
        for (String userIdOnly : dbkeys) {
            String userIdOnlyWithoutPrefix = getDbKeysWithoutPrefix(userIdOnly);
            String[] userIdOnlysWithoutPrefix = userIdOnlyWithoutPrefix.split("_");
            Long userId = Long.valueOf(userIdOnlysWithoutPrefix[0]);
            Long onlyId = Long.valueOf(userIdOnlysWithoutPrefix[1]);
            // 解析生成TCX文件
            try {
                generateTcxFile(userId, onlyId);
            } catch (JAXBException e) {
                throw new RuntimeException(e);
            }
        }

    }

    private void generateTcxFile(Long userId, Long onlyId) throws JAXBException {
        try {
            LambdaQueryWrapper<SportDetail> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SportDetail::getUserId, userId);
            queryWrapper.eq(SportDetail::getOnly, onlyId);
            SportDetail sportDetail = detailSportService.getOne(queryWrapper);
            if (sportDetail == null){
                logger.info("该用户运动数据不存在");
                redisService.deleteObject(getStravaSuccessSaveDbKeyRedisKey(userId + "_" + onlyId));
                return;
            }
            LambdaQueryWrapper<SportKlocation> locationQueryWrapper =  new LambdaQueryWrapper<>();
            locationQueryWrapper.eq(SportKlocation::getUserId, onlyId);
            List<SportKlocation> klocationList = kLocationService.list(locationQueryWrapper);
            // 运动类型
            Integer sportType = sportDetail.getType();
            // 时区偏移量
            double offsetHours = sportDetail.getOffsetHours();
            // 运动开始时间
            Long startTime = sportDetail.getStartTime();
            // 运动消耗卡路里
            Double calories = sportDetail.getCalories();


            if(!klocationList.isEmpty() && klocationList.size() > 2){
                logger.info("开始生成TCX文件");
                // 创建轨迹点数据列表
                List<TrackpointDataVO> trackpointDataList = new ArrayList<>();
                List<SportKlocationVO> sportKlocationList = kLocationService.listByUserId(sportDetail.getOnly());

                // 获取每个轨迹点的距离
                LambdaQueryWrapper<SportKdistance> sportKdistanceQueryWrapper = new LambdaQueryWrapper<>();
                sportKdistanceQueryWrapper.eq(SportKdistance::getUserId, sportDetail.getOnly());
                List<SportKdistanceVO> sportKdistanceList = kDistanceDataService.listByUserId(sportDetail.getOnly());
                Map<Integer, Double> timeToDistanceMap = sportKdistanceList.stream()
                        .collect(Collectors.toMap(SportKdistanceVO::getTotalTime, SportKdistance::getDistance));

                //test计算测试
                List<double[]> sportKlocationMapList = convertListToCoordinates(sportKlocationList);
                double totalDistance = DistanceCalculator.calculateTotalDistance(sportKlocationMapList);
                logger.info("userId:{}, onlyId:{}",userId, onlyId);
                logger.info("totalDistance:{}",totalDistance);

                AtomicInteger index = new AtomicInteger(0);
                sportKlocationList.stream()
                        .filter(sportKlocation -> index.getAndIncrement() % 5 == 0)
                        .forEach(sportKlocation -> {
                            Integer totalTime = sportKlocation.getTotalTime();
                            logger.info("totalTime:{}", totalTime);
                            double latitude = sportKlocation.getLatitude();
                            double longitude = sportKlocation.getLongitude();
                            TrackpointDataVO trackpointData = new TrackpointDataVO(
                                    TimestampUtils.convertToISO8601WithDecimalOffset(startTime + totalTime, offsetHours),
                                    latitude,
                                    longitude,
                                    0.0,
                                    timeToDistanceMap.get(totalTime) == null ? 0.0 : timeToDistanceMap.get(totalTime).intValue(),
                                    0,
                                    100);
                            trackpointDataList.add(trackpointData);
                        });

                // 额外添加最后一个对象
                if (!sportKlocationList.isEmpty()) {
                    SportKlocationVO lastObject = sportKlocationList.get(sportKlocationList.size() - 1);
                    Integer totalTime = lastObject.getTotalTime();
                    double latitude = lastObject.getLatitude();
                    double longitude = lastObject.getLongitude();
                    TrackpointDataVO trackpointData = new TrackpointDataVO(
                            TimestampUtils.convertToISO8601WithDecimalOffset(startTime + totalTime,offsetHours),
                            latitude,
                            longitude,
                            0.0,
                            timeToDistanceMap.get(totalTime).intValue(),
                            0,
                            100);
                    // 检查是否已存在，避免重复
                    if (!trackpointDataList.contains(trackpointData)) {
                        trackpointDataList.add(trackpointData);
                    }
                }


                // 创建ActivityVO对象
                ActivityVO activityVO = new ActivityVO(
                        SportTypeMatcher.getOutdoorStravaActivityType(KieslectSportTypeEnum.fromCode(sportType)).getEnglishDescription(),
                        TimestampUtils.convertToISO8601WithDecimalOffset(startTime, offsetHours),
                        TimestampUtils.convertToISO8601WithDecimalOffset(startTime, offsetHours),
                        calories.intValue(),
                        trackpointDataList
                );

                // 使用ActivityVO填充数据
                TrainingCenterDatabase database = fillTrainingCenterDatabase(activityVO);

                // 使用毫秒值作为文件名
                String newFileName = userId + "_" + onlyId;

                // 文件保存路径
                File outputFile = FileUtil.file(LOCAL_TCX_FOLDER, newFileName);


                // 使用 JAXB 将对象转换为 XML 文件
                JAXBContext jaxbContext = JAXBContext.newInstance(TrainingCenterDatabase.class);
                // 创建 Marshaller 对象
                Marshaller marshaller = jaxbContext.createMarshaller();
                // 格式化输出
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);


                // 将 TrainingCenterDatabase 对象写入文件
                marshaller.marshal(database, outputFile);

                logger.info("TCX 文件已生成并保存到: " + outputFile.getAbsolutePath());

                // 手动修改输出文件，插入 xsi:schemaLocation
                addXsiSchemaLocation(outputFile);

                // 写入strava文件key缓存
                redisService.setCacheObject(getStravaTcxFileRedisKey(userId + "_" + onlyId), 1);

                //删除数据
                removeData(sportDetail);

                // 删除缓存
                redisService.deleteObject(getStravaSuccessSaveDbKeyRedisKey(userId + "_" + onlyId));
            }else {
                String result = indoorRunCreateActivities(sportDetail);
                logger.info("室内运动:{},{},{}, result:{}",userId,onlyId,sportType, result);
                //删除数据
                removeData(sportDetail);
                // 删除缓存
                redisService.deleteObject(getStravaSuccessSaveDbKeyRedisKey(userId + "_" + onlyId));
            }
        }catch (Exception e){
            logger.error("generateTcxFile error:{},{},{}", userId,onlyId);
        }





    }

    private void removeData(SportDetail sportDetail) {
        // t_sport_detail
        detailSportService.removeById(sportDetail.getId());
        // t_sport_kdevice
        LambdaQueryWrapper<SportKdevice> deviceQueryWrapper = new LambdaQueryWrapper<>();
        deviceQueryWrapper.eq(SportKdevice::getUserId, sportDetail.getOnly());
        kDeviceService.remove(deviceQueryWrapper);
        // t_sport_ktimer
        LambdaQueryWrapper<SportKtimer> timerQueryWrapper = new LambdaQueryWrapper<>();
        timerQueryWrapper.eq(SportKtimer::getUserId, sportDetail.getOnly());
        kTimerService.remove(timerQueryWrapper);
        // t_sport_klocation
        LambdaQueryWrapper<SportKlocation> kLocationQueryWrapper = new LambdaQueryWrapper<>();
        kLocationQueryWrapper.eq(SportKlocation::getUserId, sportDetail.getOnly());
        kLocationService.remove(kLocationQueryWrapper);
        // t_sport_kactivity
        LambdaQueryWrapper<SportKactivity> activityQueryWrapper = new LambdaQueryWrapper<>();
        activityQueryWrapper.eq(SportKactivity::getUserId, sportDetail.getOnly());
        kActivityDataService.remove(activityQueryWrapper);
        // t_sport_kdistance
        LambdaQueryWrapper<SportKdistance> kDistanceDataQueryWrapper = new LambdaQueryWrapper<>();
        kDistanceDataQueryWrapper.eq(SportKdistance::getUserId, sportDetail.getOnly());
        kDistanceDataService.remove(kDistanceDataQueryWrapper);
    }


    private void parseFileToDB(String fileName) {
        try {
            double offsetHours = redisService.getCacheObject(getStravaFileNameRedisKey(fileName));

            String filePath = LOCAL_UPLOAD_FOLDER + File.separator + fileName;
            if (!FileUtil.exist(filePath)){
                logger.info("file not exist:{}",filePath);
                return;
            }
            // 读取protobuf二进制文件
            FileInputStream fis = new FileInputStream(filePath);

            // 反序列化数据
            KSportProto.KSport kSport = KSportProto.KSport.parseFrom(fis);

            long userId = kSport.getUserId();
            List<Long> onlyIds = kSport.getDetailSportList().stream().map(KSportProto.DetailSport::getOnly).collect(Collectors.toList());

            // 使用反序列化的数据
            logger.info("User ID: " + kSport.getUserId());
            logger.info("Number of DetailSport items: " + kSport.getDetailSportCount());

            long timestamp = Instant.now().getEpochSecond();  // 统一的时间戳

            // 保存数据到数据库（分批处理）
            processBatches(kSport, userId, timestamp,offsetHours);

            // 缓存
            onlyIds.stream().forEach(onlyId -> redisService.setCacheObject(getStravaSuccessSaveDbKeyRedisKey(userId + "_" + onlyId), 1));

            fis.close();

            //删除文件
//            File file = new File(filePath);
//            file.delete();

            // 删除缓存
            redisService.deleteObject(getStravaFileNameRedisKey(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processBatches(KSportProto.KSport kSport, long userId, long timestamp, double offsetHours) {
        // 创建固定线程池，线程数量为 6
        ExecutorService executor = Executors.newFixedThreadPool(6);

        try {
            // 提交任务到线程池
            executor.submit(() -> saveDetailSportBatch(kSport, timestamp, offsetHours));
            executor.submit(() -> saveKDeviceBatch(kSport, userId, timestamp));
            executor.submit(() -> saveKTimerBatch(kSport, userId, timestamp));
            executor.submit(() -> saveKLocationBatch(kSport, userId, timestamp));
            executor.submit(() -> saveKActivityDataBatch(kSport, userId, timestamp));
            executor.submit(() -> saveKDistanceDataBatch(kSport, userId, timestamp));
        } finally {
            // 关闭线程池，等待所有任务完成
            executor.shutdown();
        }
    }

    // 保存 DetailSport 数据
    private void saveDetailSportBatch(KSportProto.KSport kSport, long timestamp, double offsetHours) {
        List<SportDetail> detailSportList = buildDetailSportList(kSport, timestamp,offsetHours);
        detailSportService.insertOrUpdateSportDetailBatch(detailSportList);
    }


    // 保存 KDevice 数据
    private void saveKDeviceBatch(KSportProto.KSport kSport, long userId, long timestamp) {
        List<SportKdevice> kDeviceList = new ArrayList<>();
        for (KSportProto.DetailSport detailSport : kSport.getDetailSportList()) {
            kDeviceList.addAll(buildKDeviceList(detailSport, userId, timestamp));
        }
        kDeviceService.saveOrUpdateBatch(kDeviceList);
    }

    // 保存 KTimer 数据
    private void saveKTimerBatch(KSportProto.KSport kSport, long userId, long timestamp) {
        List<SportKtimer> kTimerList = new ArrayList<>();
        for (KSportProto.DetailSport detailSport : kSport.getDetailSportList()) {
            kTimerList.addAll(buildKTimerList(detailSport, userId, timestamp));
        }
        kTimerService.saveOrUpdateBatch(kTimerList);
    }

    // 保存 KLocation 数据
    private void saveKLocationBatch(KSportProto.KSport kSport, long userId, long timestamp) {
        List<SportKlocation> kLocationList = new ArrayList<>();
        for (KSportProto.DetailSport detailSport : kSport.getDetailSportList()) {
            kLocationList.addAll(buildKLocationList(detailSport, userId, timestamp));
        }
        kLocationService.saveOrUpdateBatch(kLocationList);
    }

    // 保存 KActivityData 数据
    private void saveKActivityDataBatch(KSportProto.KSport kSport, long userId, long timestamp) {
        List<SportKactivity> kActivityDataList = new ArrayList<>();
        for (KSportProto.DetailSport detailSport : kSport.getDetailSportList()) {
            kActivityDataList.addAll(buildKActivityDataList(detailSport, userId, timestamp));
        }
        kActivityDataService.saveOrUpdateBatch(kActivityDataList);
    }

    // 保存 KDistanceData 数据
    private void saveKDistanceDataBatch(KSportProto.KSport kSport, long userId, long timestamp) {
        List<SportKdistance> kDistanceDataList = new ArrayList<>();
        for (KSportProto.DetailSport detailSport : kSport.getDetailSportList()) {
            kDistanceDataList.addAll(buildKDistanceDataList(detailSport, userId, timestamp));
        }
        kDistanceDataService.saveOrUpdateBatch(kDistanceDataList);
    }

    private List<SportDetail> buildDetailSportList(KSportProto.KSport kSport, long timestamp, double offsetHours) {
        logger.info("开始构建 DetailSport 数据：{}", kSport.getDetailSportCount());
        List<SportDetail> detailSportList = new ArrayList<>();

        for (KSportProto.DetailSport detailSportProto : kSport.getDetailSportList()) {
            SportDetail detailSport = new SportDetail();

            // 设置 DetailSport 的基本属性
            detailSport.setUserId(kSport.getUserId()); // 从父级对象获取 userId
            detailSport.setOnly(detailSportProto.getOnly());
            detailSport.setType(detailSportProto.getInfo().getType());
            detailSport.setOffsetHours(offsetHours);
            detailSport.setStartTime(detailSportProto.getInfo().getStartTime());
            detailSport.setEndTime(detailSportProto.getInfo().getEndTime());
            detailSport.setTime(detailSportProto.getInfo().getTime());
            detailSport.setSteps(detailSportProto.getInfo().getSteps());
            detailSport.setDistance((double) detailSportProto.getInfo().getDistance());
            detailSport.setMaxSpeed((double) detailSportProto.getInfo().getMaxSpeed());
            detailSport.setAvgHr((double) detailSportProto.getInfo().getAvgHr());
            detailSport.setMaxHr(detailSportProto.getInfo().getMaxHr());
            detailSport.setMinHr(detailSportProto.getInfo().getMinHr());
            detailSport.setDeviceType(detailSportProto.getInfo().getDeviceType());
            detailSport.setAimData((double) detailSportProto.getInfo().getAimData());
            detailSport.setAimType(detailSportProto.getInfo().getAimType());
            detailSport.setCalories((double) detailSportProto.getInfo().getCalories());
            detailSport.setLap(detailSportProto.getInfo().getLap());

            // 设置创建和更新时间
            detailSport.setCreateTime(timestamp);
            detailSport.setUpdateTime(timestamp);

            // 添加到列表中
            detailSportList.add(detailSport);
        }

        return detailSportList;
    }

    private List<SportKdevice> buildKDeviceList(KSportProto.DetailSport detailSport, long userId, long timestamp) {
        logger.info("开始构建 KDevice 数据：{}", detailSport.getDevicesCount());
        List<SportKdevice> kDeviceList = new ArrayList<>();
        for (KSportProto.KDevice device : detailSport.getDevicesList()) {
            SportKdevice kDevice = new SportKdevice();
            // 设置 KDevice 的各字段
            kDevice.setUserId(detailSport.getOnly());
            kDevice.setDeviceType(device.getDeviceType());
            kDevice.setMac(device.getMac());
            kDevice.setName(device.getName());
            // 设置其他必要的字段
            kDevice.setCreateTime(timestamp);
            kDevice.setUpdateTime(timestamp);
            kDeviceList.add(kDevice);
        }
        return kDeviceList;
    }


    private List<SportKtimer> buildKTimerList(KSportProto.DetailSport detailSport, long userId, long timestamp) {
        logger.info("开始构建 KTimer 数据：{}", detailSport.getTimersCount());
        List<SportKtimer> kTimerList = new ArrayList<>();
        for (KSportProto.KTimer timer : detailSport.getTimersList()) {
            SportKtimer kTimer = new SportKtimer();
            kTimer.setUserId(detailSport.getOnly());
            kTimer.setTotalTime(timer.getTotalTime());
            kTimer.setTime(timer.getTime());
            kTimer.setStatus(timer.getStatus());
            // 可以设置创建和更新时间，通常为当前时间戳
            kTimer.setCreateTime(timestamp);
            kTimer.setUpdateTime(timestamp);
            kTimerList.add(kTimer);
        }
        return kTimerList;
    }

    private List<SportKlocation> buildKLocationList(KSportProto.DetailSport detailSport, long userId, long timestamp) {
        logger.info("开始构建 KLocation 数据：{}", detailSport.getLocationsCount());
        List<SportKlocation> kLocationList = new ArrayList<>();
        for (KSportProto.KLocation location : detailSport.getLocationsList()) {
            SportKlocation kLocation = new SportKlocation();
            kLocation.setUserId(detailSport.getOnly());
            kLocation.setTime(location.getTime());
            kLocation.setLatitude(location.getLatitude());
            kLocation.setLongitude(location.getLongitude());
            // 可以设置创建和更新时间，通常为当前时间戳
            kLocation.setCreateTime(timestamp);
            kLocation.setUpdateTime(timestamp);
            kLocationList.add(kLocation);
        }
        return kLocationList;
    }

    private List<SportKactivity> buildKActivityDataList(KSportProto.DetailSport detailSport, long userId, long timestamp) {
        logger.info("开始构建 KActivityData 数据：{}", detailSport.getActivityDataCount());
        List<SportKactivity> kActivityDataList = new ArrayList<>();
        for (KSportProto.KActivityData activityData : detailSport.getActivityDataList()) {
            SportKactivity kActivityData = new SportKactivity();
            kActivityData.setUserId(detailSport.getOnly());
            kActivityData.setTime(activityData.getTime());
            kActivityData.setHr(activityData.getHr());
            kActivityData.setSteps(activityData.getSteps());
            kActivityData.setCadence(activityData.getCadence());
            // 可以设置创建和更新时间，通常为当前时间戳
            kActivityData.setCreateTime(timestamp);
            kActivityData.setUpdateTime(timestamp);
            kActivityDataList.add(kActivityData);
        }
        return kActivityDataList;
    }

    private List<SportKdistance> buildKDistanceDataList(KSportProto.DetailSport detailSport, long userId, long timestamp) {
        logger.info("开始构建 KDistanceData 数据，{}", detailSport.getDistanceDataCount());
        List<SportKdistance> kDistanceDataList = new ArrayList<>();
        for (KSportProto.KDistanceData distanceData : detailSport.getDistanceDataList()) {
            SportKdistance kDistanceData = new SportKdistance();
            kDistanceData.setUserId(detailSport.getOnly());
            kDistanceData.setTime(distanceData.getTime());
            kDistanceData.setDistance((double) distanceData.getDistance());
            kDistanceData.setSpeed((double) distanceData.getSpeed());
            kDistanceData.setLap(distanceData.getLap());
            // 可以设置创建和更新时间，通常为
            kDistanceData.setCreateTime(timestamp);
            kDistanceData.setUpdateTime(timestamp);
            kDistanceDataList.add(kDistanceData);
        }
        return kDistanceDataList;
    }

    @Override
    public void readTcxFileUploadStrava() {
        // 从缓存中获取已经上传完成的文件名集合
        Collection<String> tcxFiles = redisService.keys(getStravaTcxFileRedisKey("*"));
        logger.info("获取到{}个文件", tcxFiles.size());
        for (String tcxFileRedisKey : tcxFiles) {
            String tcxFileName = getTcxFileNameWithoutPrefix(tcxFileRedisKey);
            String[] tcxs = tcxFileName.split("_");
            Long userId = Long.valueOf(tcxs[0]);

            // 获取Strava授权信息
            String Authorization = getStravaAuthorization(userId);
            if (Authorization == null){
                continue;
            }
            HttpResponse response = HttpUtil.createPost(FILE_UPLOADS_URL)
                    .header("Authorization", "Bearer " + Authorization)
                    .form("file", new File(LOCAL_TCX_FOLDER + File.separator + tcxFileName))
                    .form("name", "Sport")  // 设置文件的名称
                    .form("description", "Sport")  // 设置描述
                    .form("data_type", "tcx")  // 设置文件类型
                    .execute();
            logger.info("上传文件：{}，返回响应：{}", tcxFileName, response.body());
            if (response.getStatus() == 201) {
                redisService.deleteObject(tcxFileRedisKey);
            }

        }


    }

    private String indoorRunCreateActivities(SportDetail sportDetail) {
        logger.info("发送室内运动数据");

        StravaCreateActivitiesVO vo = new StravaCreateActivitiesVO();
        vo.setName("Sport");
        vo.setSport_type(SportTypeMatcher.getIndoorStravaActivityType(KieslectSportTypeEnum.fromCode(sportDetail.getType())).getEnglishDescription());
        vo.setStart_date_local(TimestampUtils.convertToISO8601WithDecimalOffset(sportDetail.getStartTime(), sportDetail.getOffsetHours()));
        vo.setElapsed_time(sportDetail.getTime());
        vo.setDescription("Sport");
        vo.setDistance(sportDetail.getDistance().intValue());
        vo.setCommute(false);
        vo.setTrainer(false);
        vo.setCalories(sportDetail.getCalories().intValue());
        // 获取Strava授权信息
        String Authorization = getStravaAuthorization(sportDetail.getUserId());
        logger.info("发送室内运动数据，授权信息：{}", Authorization);
        logger.info("发送的JSON数据：{}", JSONUtil.toJsonStr(vo));
        return HttpUtil
                .createPost(CREATE_ACTIVITIES_URL)
                .header("Authorization", "Bearer "+Authorization)
                .body(JSONUtil.toJsonStr(vo))
                .execute().body();
    }

    private String getStravaAuthorization(Long userId) {
        ThirdUserInfo thirdUserInfo = detailSportService.getThirdInfoByUserId(userId);
        if (thirdUserInfo != null) {
            //从缓存里获取授权信息
            String redisKey = getStravaTokenRedisKey(thirdUserInfo.getThirdId());

            JSONObject jsonObject = redisService.getCacheObject(redisKey);
            StravaTokenInfo stravaAuth = JSON.parseObject(String.valueOf(jsonObject),StravaTokenInfo.class);
            if (stravaAuth.getExpiresAt() < Instant.now().getEpochSecond()){
                logger.error("stravaAuth用户授权信息已过期，{}",stravaAuth.getAthleteId());
                return remoteAuthService.getRefreshToken(stravaAuth.getRefreshToken());
            }
            return stravaAuth.getAccessToken();
        }
        logger.error("用户授权信息为空，{}",userId);
        return null;
    }

    private String getFileNameWithoutPrefix(String fileName) {
        return fileName.substring(STRAVA_FILE_NAME.length());
    }

    private String getDbKeysWithoutPrefix(String dbkeys) {
        return dbkeys.substring(STRAVA_SUCCESS_SAVE_DB_KEY.length());
    }

    private String getTcxFileNameWithoutPrefix(String tcxRedisKey) {
        return tcxRedisKey.substring(STRAVA_TCX_FILE.length());
    }

}
