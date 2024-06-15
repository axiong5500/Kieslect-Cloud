package com.kieslect.common.core.utils;

import com.kieslect.common.core.config.MailConfig;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;


public class EmailUtils {

    // 发送邮件的方法
    public static void sendEmail(MailConfig mailConfig, String toEmail, String subject, String body) {
        final String fromEmail = mailConfig.getUsername();
        final String password = mailConfig.getPassword();
        String host = mailConfig.getHost();
        int port = mailConfig.getPort();

        // 设置邮件属性
        Properties props = new Properties();
        if (port == 465){
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.socketFactory.fallback", "false");
            props.put("mail.smtp.socketFactory.port", port);
        }else if (port == 587){
            props.put("mail.smtp.starttls.enable", "true");
        }
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        // 获取会话对象
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            // 创建邮件消息
            Message message = new MimeMessage(session);
            // 设置发件人
            message.setFrom(new InternetAddress(fromEmail));
            // 设置收件人
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            // 设置邮件主题
            message.setSubject(subject);
            // 设置邮件内容
            message.setText(body);
            // 发送邮件
            Transport.send(message);
            System.out.println("邮件发送成功");
        } catch (MessagingException e) {
            System.out.println("邮件发送失败，错误信息：" + e.getMessage());
        }
    }

    /**
     * 判断是否是邮箱
     * @param str
     * @return  true 是邮箱 false 不是邮箱
     */
    public static boolean isEmail(String str){
        if(StringUtils.isEmpty(str)){
            throw new RuntimeException("不能为空");
        }
        String pattern = "^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)+$";
        return str.matches(pattern);
    }

    public static void main(String[] args) {
        MailConfig mailConfig = new MailConfig();
        //QQ
        mailConfig.setUsername("lianyixiong@qq.com");
        mailConfig.setPassword("jgbencutyyygbbgj");
        mailConfig.setHost("smtp.qq.com");
        mailConfig.setPort(587);
        sendEmail(mailConfig, "lianyixiong@gmail.com", "Test Subject", "Test Body");
        //ALi
        mailConfig.setUsername("KstyleOS@kieslect-cn.com");
        mailConfig.setPassword("HH123456@");
        mailConfig.setHost("smtp.qiye.aliyun.com");
        mailConfig.setPort(465);
        sendEmail(mailConfig, "lianyixiong@gmail.com", "Test Subject", "Test Body");
    }
}
