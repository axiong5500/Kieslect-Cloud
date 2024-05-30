package com.kieslect.generator;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import com.kieslect.gen.proto.GeoCityBeanProto;

import java.io.*;

public class JsonToProtobufGenerator {
    public static void main(String[] args) {

            String fileName = "C:\\Users\\kiesl\\Desktop\\t_geoname.kieslect";
            String jsonPath = "C:\\Users\\kiesl\\Desktop\\t_geoname.json";
        try (InputStream jsonStream = new FileInputStream(jsonPath);
             InputStreamReader jsonStreamReader = new InputStreamReader(jsonStream, java.nio.charset.StandardCharsets.UTF_8)) {

            GeoCityBeanProto.GeoCityBean.Builder builder = GeoCityBeanProto.GeoCityBean.newBuilder();
            JsonFormat.parser().merge(jsonStreamReader, builder);
            GeoCityBeanProto.GeoCityBean geoCityBean = builder.build();

            try (FileOutputStream output = new FileOutputStream(fileName)) {
                geoCityBean.writeTo(output);
                System.out.println("Conversion successful!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (InvalidProtocolBufferException | FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
