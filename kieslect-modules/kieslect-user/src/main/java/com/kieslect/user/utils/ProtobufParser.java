package com.kieslect.user.utils;


import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.kieslect.user.proto.KActivityProto;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ProtobufParser {
    public static KActivityProto.KActivity parseFromInputStream(InputStream inputStream)
            throws IOException, InvalidProtocolBufferException {
        // 创建 KActivity.Builder 对象
        KActivityProto.KActivity.Builder builder = KActivityProto.KActivity.newBuilder();

        // 从 InputStream 中读取数据并解析到 Builder 中
        builder.mergeFrom(inputStream);

        // 构建最终的 Protocol Buffers 消息对象
        KActivityProto.KActivity kActivity = builder.build();

        return kActivity;
    }

    /**
     * 解析从 InputStream 中读取的 Protocol Buffers 消息并构建对应的 Message 对象。
     *
     * @param inputStream 输入流，包含 Protocol Buffers 消息的字节数据
     * @param builder     Protocol Buffers 自动生成的消息 Builder 对象
     * @param <T>         Protocol Buffers 自动生成的消息类型
     * @return 解析后的 Protocol Buffers 消息对象
     * @throws IOException 如果解析过程中发生 I/O 错误
     */
    public static <T extends com.google.protobuf.Message.Builder> T parseFromInputStream
    (InputStream inputStream, T builder) throws IOException {
        // 从 InputStream 中读取数据并解析到 Builder 中
        builder.mergeFrom(inputStream);

        // 构建最终的 Protocol Buffers 消息对象
        @SuppressWarnings("unchecked")
        T message = (T) builder.build();

        return message;
    }

    /**
     * 解析从 InputStream 中读取的 Protocol Buffers 消息并构建对应的 Message 对象。
     *
     * @param inputStream  输入流，包含 Protocol Buffers 消息的字节数据
     * @param messageClass Protocol Buffers 自动生成的消息类型的 Class 对象
     * @param <T>          Protocol Buffers 自动生成的消息类型
     * @return 解析后的 Protocol Buffers 消息对象
     * @throws IOException 如果解析过程中发生 I/O 错误
     */
    public static <T extends Message> T parseFromInputStream(InputStream inputStream, Class<T> messageClass)
            throws IOException {
        try {
            // 使用反射获取 Builder 对象
            Message.Builder builder = (Message.Builder) messageClass.getMethod("newBuilder").invoke(null);

            // 调用通用的解析方法
            Message message = (Message) parseFromInputStream(inputStream, builder);

            // 返回解析后的消息对象
            @SuppressWarnings("unchecked")
            T typedMessage = (T) message;

            return typedMessage;
        } catch (Exception e) {
            throw new RuntimeException("Error parsing Protocol Buffers message", e);
        }
    }


    /**
     * 将 Protocol Buffers 消息 Builder 对象序列化为字节数组。
     *
     * @param builder Protocol Buffers 消息 Builder 对象
     * @param <T>     Protocol Buffers 消息 Builder 的类型（必须是 Message.Builder 的子类）
     * @return 序列化后的字节数组
     * @throws IOException 如果序列化过程中发生 I/O 错误
     */
    public static <T extends Message.Builder> byte[] serializeBuilder(T builder) throws IOException {
        // 创建一个 ByteArrayOutputStream 来缓存序列化后的数据
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            // 将 Builder 对象构建为消息对象并序列化到 ByteArrayOutputStream 中
            builder.build().writeTo(outputStream);

            // 返回序列化后的字节数组
            return outputStream.toByteArray();

        } finally {
            // 关闭 ByteArrayOutputStream
            outputStream.close();
        }
    }

}
