import com.kieslect.user.proto.KActivityProto;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class KActivityExample {
    public static void main(String[] args) {
        // 创建 KActivity.Builder 对象
        KActivityProto.KActivity.Builder kActivityBuilder = KActivityProto.KActivity.newBuilder();

        // 设置 user_id
        kActivityBuilder.setUserId(123456789); // 设置用户 ID

        // 构建 KActivityListData 对象
        KActivityProto.KActivityListData.Builder listDataBuilder = KActivityProto.KActivityListData.newBuilder();

        // 构建 DetailSteps 对象
        KActivityProto.DetailSteps.Builder detailStepsBuilder = KActivityProto.DetailSteps.newBuilder();
        detailStepsBuilder.setDate(20220415);
        detailStepsBuilder.setSteps(8000);
        detailStepsBuilder.setCalories(350);
        detailStepsBuilder.setDistance(6000);
        detailStepsBuilder.setMac("00:11:22:33:44:55");
        detailStepsBuilder.setOnly(123456789);
        listDataBuilder.addDetailSteps(detailStepsBuilder.build());

        // 构建 DetailHr 对象
        KActivityProto.DetailHr.Builder detailHrBuilder = KActivityProto.DetailHr.newBuilder();
        detailHrBuilder.setDate(20220415);
        detailHrBuilder.setHr(75);
        detailHrBuilder.setMac("00:11:22:33:44:55");
        detailHrBuilder.setOnly(123456789);
        listDataBuilder.addDetailHr(detailHrBuilder.build());

        // 构建 DetailSilentHr 对象
        KActivityProto.DetailSilentHr.Builder detailSilentHrBuilder = KActivityProto.DetailSilentHr.newBuilder();
        detailSilentHrBuilder.setDate(20220415);
        detailSilentHrBuilder.setSilentHr(60);
        detailSilentHrBuilder.setMac("00:11:22:33:44:55");
        detailSilentHrBuilder.setOnly(123456789);
        listDataBuilder.addDetailSilentHr(detailSilentHrBuilder.build());

        // 构建 DetailSpo2 对象
        KActivityProto.DetailSpo2.Builder detailSpo2Builder = KActivityProto.DetailSpo2.newBuilder();
        detailSpo2Builder.setDate(20220415);
        detailSpo2Builder.setSpo2(95);
        detailSpo2Builder.setPi(3);
        detailSpo2Builder.setMac("00:11:22:33:44:55");
        detailSpo2Builder.setOnly(123456789);
        listDataBuilder.addDetailSpo2(detailSpo2Builder.build());

        // 构建 DetailStress 对象
        KActivityProto.DetailStress.Builder detailStressBuilder = KActivityProto.DetailStress.newBuilder();
        detailStressBuilder.setDate(20220415);
        detailStressBuilder.setStress(50);
        detailStressBuilder.setMac("00:11:22:33:44:55");
        detailStressBuilder.setOnly(123456789);
        listDataBuilder.addDetailStress(detailStressBuilder.build());

        // 构建 DetailSleep 对象
        KActivityProto.DetailSleep.Builder detailSleepBuilder = KActivityProto.DetailSleep.newBuilder();
        detailSleepBuilder.setStartTime(1649990400); // 开始睡眠时间，单位为秒（2022-04-15 20:00:00）
        detailSleepBuilder.setEndTime(1650026400); // 结束睡眠时间，单位为秒（2022-04-16 06:00:00）
        detailSleepBuilder.setTime(28800); // 睡眠时长，单位为秒（8 小时）
        detailSleepBuilder.setCode(1); // 睡眠类型，1 表示深睡
        detailSleepBuilder.setMac("00:11:22:33:44:55");
        detailSleepBuilder.setOnly(123456789);
        listDataBuilder.addDetailSleep(detailSleepBuilder.build());

        // 构建 DetailWeight 对象
        KActivityProto.DetailWeight.Builder detailWeightBuilder = KActivityProto.DetailWeight.newBuilder();
        detailWeightBuilder.setDate(20220415);
        detailWeightBuilder.setWeight(65.5); // 体重，单位为千克
        detailWeightBuilder.setMac("00:11:22:33:44:55");
        detailWeightBuilder.setOnly(123456789);
        listDataBuilder.addDetailWeight(detailWeightBuilder.build());

        // 将 KActivityListData 添加到 KActivity 中
        kActivityBuilder.addList(listDataBuilder.build());

        // 构建完成的 KActivity 对象
        KActivityProto.KActivity kActivity = kActivityBuilder.build();

        // 将 KActivity 对象序列化为字节数组
        byte[] serializedActivity = kActivity.toByteArray();

        // 指定输出文件路径
        String outputPath = "src/main/resources/KActivityExampleData.dat";

        // 创建输出文件所在的目录（如果不存在）
        File outputFile = new File(outputPath);
        outputFile.getParentFile().mkdirs(); // 创建所有父级目录

        // 将序列化后的字节数组写入文件
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(serializedActivity);
            System.out.println("Serialized KActivity data written to file: " + outputPath);
        } catch (IOException e) {
            System.err.println("Error writing serialized KActivity data to file: " + e.getMessage());
        }


    }
}
