package com.kieslect.file.utils;


import com.kieslect.file.domain.TrainingCenterDatabase;
import com.kieslect.file.domain.vo.ActivityVO;
import com.kieslect.file.domain.vo.TrackpointDataVO;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.util.Arrays;
import java.util.List;

public class TrainingCenterDatabaseUtil {
    // 使用ActivityVO封装数据填充TrainingCenterDatabase
    public static TrainingCenterDatabase fillTrainingCenterDatabase(ActivityVO activityVO) {

        // 创建活动并设置ID
        TrainingCenterDatabase database = new TrainingCenterDatabase();
        TrainingCenterDatabase.Activity activity = new TrainingCenterDatabase.Activity();
        activity.setId(activityVO.getActivityId());  // 使用传入的活动ID
        activity.setSport(activityVO.getSportType());// 使用传入的体育类型

        // 创建Lap并设置开始时间和卡路里
        TrainingCenterDatabase.Activity.Lap lap = new TrainingCenterDatabase.Activity.Lap();
        lap.setStartTime(activityVO.getLapStartTime());  // 使用传入的开始时间
        lap.setCalories(activityVO.getCalories());  // 使用传入的卡路里

        // 创建Track对象
        TrainingCenterDatabase.Activity.Track track = new TrainingCenterDatabase.Activity.Track();

        // 创建多个轨迹点
        List<TrainingCenterDatabase.Activity.Trackpoint> trackpoints = createTrackpoints(activityVO.getTrackpointDataList());

        // 将多个轨迹点添加到Track
        track.setTrackpoint(trackpoints);

        // 设置Lap的Track
        lap.setTrack(track);

        // 设置活动的Lap
        activity.setLap(lap);

        // 将活动添加到数据库
        database.setActivities(Arrays.asList(activity));

        return database;
    }

    // 创建多个轨迹点的方法
    private static List<TrainingCenterDatabase.Activity.Trackpoint> createTrackpoints(List<TrackpointDataVO> trackpointDataList) {
        // 根据传入的轨迹点数据列表创建多个Trackpoint对象
        return trackpointDataList.stream()
                .map(data -> createTrackpoint(
                        data.getTime(),
                        data.getLatitude(),
                        data.getLongitude(),
                        data.getAltitude(),
                        data.getDistance(),
                        data.getHeartRate(),
                        data.getCadence()))
                .toList();
    }

    // 创建单个Trackpoint对象的方法
    private static TrainingCenterDatabase.Activity.Trackpoint createTrackpoint(
            String time, double latitude, double longitude, double altitude, double distance,
            int heartRate, int cadence) {

        TrainingCenterDatabase.Activity.Trackpoint point = new TrainingCenterDatabase.Activity.Trackpoint();
        point.setTime(time);
        // 创建Position对象并设置纬度和经度
        TrainingCenterDatabase.Activity.Trackpoint.Position position = new TrainingCenterDatabase.Activity.Trackpoint.Position();
        position.setLatitudeDegrees(latitude);
        position.setLongitudeDegrees(longitude);

        // 将Position对象设置到Trackpoint中
        point.setPosition(position);
        point.setAltitudeMeters(altitude);
        point.setDistanceMeters(distance);
        point.setHeartRateBpm(heartRate);
        point.setCadence(cadence);

        return point;
    }

    public static void main(String[] args) throws JAXBException {

        // 创建轨迹点数据列表
        List<TrackpointDataVO> trackpointDataList = Arrays.asList(
                new TrackpointDataVO("2024-11-22T04:30:00Z", 37.7749, -122.4194, 0.0, 0.0, 0, 0),
                new TrackpointDataVO("2024-11-22T04:30:30Z", 37.7750, -122.4195, 0.0, 0.0, 0, 0),
                new TrackpointDataVO("2024-11-22T04:31:00Z", 37.7751, -122.4196, 0.0, 0.0, 0, 0)
        );

        // 创建ActivityVO对象
        ActivityVO activityVO = new ActivityVO(
                "Biking",
                TimestampUtils.convertToISO8601(1732242350),
                TimestampUtils.convertToISO8601(1732242350),
                300,
                trackpointDataList
        );

        // 使用ActivityVO填充数据
        TrainingCenterDatabase database = fillTrainingCenterDatabase(activityVO);

        // 定义文件路径
        String filePath = "C:/Users/kiesl/Desktop/001.tcx";
        File outputFile = new File(filePath);

        // 使用 JAXB 将对象转换为 XML 文件
        JAXBContext jaxbContext = JAXBContext.newInstance(TrainingCenterDatabase.class);
        // 创建 Marshaller 对象
        Marshaller marshaller = jaxbContext.createMarshaller();
        // 格式化输出
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);


        // 将 TrainingCenterDatabase 对象写入文件
        marshaller.marshal(database, outputFile);

        System.out.println("TCX 文件已生成并保存到: " + filePath);

        // 手动修改输出文件，插入 xsi:schemaLocation
        addXsiSchemaLocation(outputFile);
    }

    public static void addXsiSchemaLocation(File file) {
        // 读取并手动修改生成的 XML，插入 xsi:schemaLocation
        try {
            String content = new String(java.nio.file.Files.readAllBytes(file.toPath()));
            content = content.replace("<TrainingCenterDatabase>",
                    "<TrainingCenterDatabase xmlns=\"http://www.garmin.com/xmlschemas/TrainingCenterDatabase/v2\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.garmin.com/xmlschemas/TrainingCenterDatabase/v2 http://www.garmin.com/xmlschemas/TrainingCenterDatabasev2.xsd\">");
            java.nio.file.Files.write(file.toPath(), content.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
