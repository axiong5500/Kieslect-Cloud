import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class idotest {
    public static void main(String[] args) {
        ExecutorService executorService = null;

        try {
            List<Integer> otaidlist = new ArrayList<>();
            // 从文件读取JSON数据
            String jsonString = FileUtil.readString(new File("C:\\Users\\kiesl\\Downloads\\response.json"), "UTF-8");

            // 解析JSON数据
            JSONObject jsonData = JSONUtil.parseObj(jsonString);
            JSONArray faceList = jsonData.getJSONArray("result");

            // 创建线程池，控制并发数量
            executorService = Executors.newFixedThreadPool(20);

            for (Object obj : faceList) {
                JSONObject face = (JSONObject) obj;
                int id = face.getInt("id");
                if (id < 0) {
                    continue;
                }
                int count = face.getInt("count");
                String name = face.getStr("name");
                System.out.println(String.format("id:%d,count:%d,name:%s", id, count, name));
                // 创建分类文件夹
                Path categoryFolder = FileUtil.mkdir("downloaded_images/" + name).toPath();

                JSONArray facelistArr = face.getJSONArray("faceList");
                for (int i = 0; i < facelistArr.size(); i++) {
                    JSONObject f = (JSONObject) facelistArr.get(i);
                    int otaId = f.getInt("id");
                    otaidlist.add(otaId);


//                    String faceTypeName = f.getStr("faceTypeName");
//                    String otaFaceName = f.getStr("otaFaceName");
//                    String imageUrl = f.getStr("imageUrl");
//                    System.out.println(faceTypeName);
//                    System.out.println(imageUrl);
//                    // 构建目标文件路径
//                    Path targetFilePath = categoryFolder.resolve(faceTypeName + "-" + otaFaceName + ".png");
//
//                    // 下载图片并保存到目标文件路径
//                    // 提交下载任务给线程池
//                    executorService.submit(() -> downloadImage(imageUrl, targetFilePath));
                }

            }


            //详情接口
            String url = "https://in-device.idoocloud.com/api/device/face/v2/get";
            String appKey = "c945645e6da4465ea7ef768ce3f5d7aa";
            HashMap<String, Object> formMap = new HashMap<>();
            formMap.put("language", "zh");
            formMap.put("appFaceVersion", "5");
            formMap.put("supportFaceVersion", "5");
            for (int i = 0; i < otaidlist.size(); i++) {
                int otaId = otaidlist.get(i);
                formMap.put("id", otaId);
                HttpResponse response = HttpUtil.createGet(url).form(formMap).header("appKey", appKey).execute();
                System.out.println(response.body());
                if (response.getStatus() != 200) {
                    System.out.println("Error occurred while downloading image: " + response.getStatus());
                    continue;
                }

                JSONObject jsonData2 = JSONUtil.parseObj(response.body());
                JSONObject face = jsonData2.getJSONObject("result");
                String faceTypeName = face.getStr("faceTypeName");
                String otaFaceName = face.getStr("otaFaceName");


                String imageUrl = face.getStr("imageUrl");
                JSONObject otaFaceVersion = face.getJSONObject("otaFaceVersion");
                String linkUrl = otaFaceVersion.getStr("linkUrl");


                // 构建目标文件路径
                // 创建分类文件夹
                Path categoryFolder = FileUtil.mkdir("downloaded_images/" + faceTypeName).toPath();
                Path targetFilePath = categoryFolder.resolve(faceTypeName + "-" + otaFaceName + ".png");
                Path targetFilePath2 = categoryFolder.resolve(faceTypeName + "-" + otaFaceName + ".zip");
                // 提交下载任务给线程池
                executorService.submit(() -> downloadImage(imageUrl, targetFilePath));
                executorService.submit(() -> downloadImage(linkUrl, targetFilePath2));
            }

        } catch (Exception e) {
            System.err.println("Error occurred while processing JSON data: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // 关闭线程池
            if (executorService != null) {
                executorService.shutdown(); // 不再接受新任务，等待已提交的任务执行完成
                try {
                    executorService.awaitTermination(Long.MAX_VALUE, java.util.concurrent.TimeUnit.NANOSECONDS); // 等待所有任务完成
                } catch (InterruptedException e) {
                    System.err.println("Error occurred while waiting for executor service termination: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    private static void downloadImage(String imageUrl, Path targetFilePath) {
        try {
            // 发起HTTP GET请求下载图片
            byte[] imageBytes = HttpRequest.get(imageUrl).execute().bodyBytes();

            // 写入文件
            FileUtil.writeBytes(imageBytes, targetFilePath.toFile());

            // 获取下载后文件的大小
            long fileSizeInKB = Files.size(targetFilePath) / 1024; // Convert bytes to KB

            if (fileSizeInKB < 1) {
                // 如果文件大小小于1KB，则删除文件并输出提示信息
                System.out.println("Downloaded image is less than 1KB and has been deleted.");
            } else {
                System.out.println("Successfully downloaded image to: " + targetFilePath);
            }

            System.out.println("Successfully downloaded image to: " + targetFilePath);
        } catch (Exception e) {
            System.err.println("Error occurred while downloading image from " + imageUrl + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}
