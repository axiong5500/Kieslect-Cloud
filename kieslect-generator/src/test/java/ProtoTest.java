import java.io.FileInputStream;
import java.io.IOException;

public class ProtoTest {
    public static void main(String[] args) throws IOException {
        // 从文件读取序列化数据
        FileInputStream fis = new FileInputStream("C:\\Users\\kiesl\\Downloads\\proto\\GeoCity.kieslect");

        try {
            // 使用 Protocol Buffers 生成的 Builder 类构建 GeoCityBean 对象
//            GeoCityBeanProto.GeoCityBean geoCityBean = GeoCityBeanProto.GeoCityBean.parseFrom(fis);
//
//            // 输出反序列化后的数据
//            System.out.println("Version: " + geoCityBean.getVersion());
//            System.out.println("Update Time: " + geoCityBean.getUpdateTime());
//            System.out.println("CityBean List: " + geoCityBean.getCityBeanList().size());

            // 遍历每个 CityBean 对象
//            for (GeoCityBeanProto.CityBean city : geoCityBean.getCityBeanList()) {
//                System.out.println("City ID: " + city.getId());
//                System.out.println("Geoname ID: " + city.getGeonameid());
//                System.out.println("Name: " + city.getName());
//                System.out.println("Alternate Names: " + city.getAlternatenames());
//                System.out.println("Latitude: " + city.getLatitude());
//                System.out.println("Longitude: " + city.getLongitude());
//                System.out.println("Timezone: " + city.getTimezone());
//                System.out.println(); // 用于分隔每个 CityBean 输出
//            }

            // 关闭文件输入流
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
