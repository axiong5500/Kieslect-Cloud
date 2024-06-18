package com.kieslect.common.core.utils;

import cn.hutool.core.lang.UUID;
import com.kieslect.common.core.config.MailConfig;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.io.UnsupportedEncodingException;
import java.util.Properties;


public class EmailUtils {

    // 发送邮件的方法
    public static void sendEmail(MailConfig mailConfig, String toEmail, String subject, String body) {
        final String fromEmail = mailConfig.getUsername();
        final String password = mailConfig.getPassword();
        final String personal = mailConfig.getPersonal();
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
        props.put("mail.smtp.from", fromEmail);
        props.put("mail.user", fromEmail);
        props.put("mail.password", password);
        props.setProperty("mail.smtp.ssl.enable", "true");
        System.setProperty("mail.mime.splitlongparameters", "false");

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
            message.setFrom(new InternetAddress(fromEmail, personal));
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
        } catch (UnsupportedEncodingException e) {
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
//        MailConfig mailConfig = new MailConfig();
        //QQ
//        mailConfig.setUsername("lianyixiong@qq.com");
//        mailConfig.setPassword("jgbencutyyygbbgj");
//        mailConfig.setHost("smtp.qq.com");
//        mailConfig.setPort(587);
//        sendEmail(mailConfig, "lianyixiong@gmail.com", "Test Subject", "Test Body");
        //ALi
//        mailConfig.setUsername("KstyleOS@kieslect-cn.com");
//        mailConfig.setPassword("HH123456@");
//        mailConfig.setHost("smtp.qiye.aliyun.com");
//        mailConfig.setPort(465);
//        sendEmail(mailConfig, "lianyixiong@gmail.com", "Test Subject", "Test Body");
        //
        sendMessage("smtpdm.aliyun.com", "kstyleos@mail.kieslect.top", "KStyleos123456", "KstyleOS","lianyixiong@gmail.com");
    }

    protected static String genMessageID(String mailFrom) {
        // message-id 用于唯一地标识每一封邮件，其格式需要遵循RFC 5322标准，通常如 <uniquestring@example.com>，其中uniquestring是邮件服务器生成的唯一标识，可能包含时间戳、随机数等信息。
        String[] mailInfo = mailFrom.split("@");
        String domain = mailFrom;
        int index = mailInfo.length - 1;
        if (index >= 0) {
            domain = mailInfo[index];
        }
        UUID uuid = UUID.randomUUID();
        StringBuffer messageId = new StringBuffer();
        messageId.append('<').append(uuid).append('@').append(domain).append('>');
        return messageId.toString();
    }

    public static void sendMessage(String smtp,String user, String password,String personal,String toUser) {
        // 配置发送邮件的环境属性
        final Properties props = new Properties();

        // 表示SMTP发送邮件，需要进行身份验证
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", smtp);
        //设置端口：
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.port", "465");
        //加密方式：
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        props.put("mail.smtp.from", user);    //mailfrom 参数
        props.put("mail.user", user);// 发件人的账号（在控制台创建的发信地址）
        props.put("mail.password", password);// 发信地址的smtp密码（在控制台选择发信地址进行设置）
        props.setProperty("mail.smtp.ssl.enable", "true");  //请配合465端口使用
        System.setProperty("mail.mime.splitlongparameters", "false");//用于解决附件名过长导致的显示异常
        //props.put("mail.smtp.connectiontimeout", 1000);

        // 构建授权信息，用于进行SMTP进行身份验证
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // 用户名、密码
                String userName = props.getProperty("mail.user");
                String password = props.getProperty("mail.password");
                return new PasswordAuthentication(userName, password);
            }
        };

        //使用环境属性和授权信息，创建邮件会话
        Session mailSession = Session.getInstance(props, authenticator);
        mailSession.setDebug(true);//开启debug模式


        final String messageIDValue = genMessageID(props.getProperty("mail.user"));
        //创建邮件消息
        MimeMessage message = new MimeMessage(mailSession) {
            @Override
            protected void updateMessageID() throws MessagingException {
                //设置自定义Message-ID值
                setHeader("Message-ID", messageIDValue);//创建Message-ID
            }
        };

        try {
            // 设置发件人邮件地址和名称。填写控制台配置的发信地址。和上面的mail.user保持一致。名称用户可以自定义填写。
            InternetAddress from = new InternetAddress(user, personal);//from 参数,可实现代发，注意：代发容易被收信方拒信或进入垃圾箱。
            message.setFrom(from);

            // 设置收件人邮件地址
            InternetAddress to = new InternetAddress(toUser);
            message.setRecipient(MimeMessage.RecipientType.TO, to);

            //设置邮件标题
            message.setSubject("测试主题");
            message.setContent("测试<br> 内容", "text/plain;charset=UTF-8");//html超文本；// "text/plain;charset=UTF-8" //纯文本。


            // 发送邮件
            Transport.send(message);

        } catch (MessagingException e) {
            String err = e.getMessage();
            // 在这里处理message内容， 格式是固定的
            System.out.println(err);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
