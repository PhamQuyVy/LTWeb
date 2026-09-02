package com.hcmute.demo.util;

import java.util.Properties;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class MailUtil {

    public static void sendOtpEmail(String toEmail, String fullName, String otpCode)
            throws MessagingException {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", Constant.MAIL_HOST);
        props.put("mail.smtp.port", String.valueOf(Constant.MAIL_PORT));

        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                        Constant.MAIL_USERNAME,
                        Constant.MAIL_APP_PASSWORD
                );
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);

           message.setFrom(new InternetAddress(Constant.MAIL_USERNAME, "CRUD Demo"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(toEmail)
            );

            message.setSubject("[CRUD Demo] Mã xác thực OTP kích hoạt tài khoản");

            String content =
                    "<div style=\"font-family:Arial,sans-serif;\">"
                  + "<p>Xin chào <b>" + fullName + "</b>,</p>"
                  + "<p>Bạn vừa đăng ký tài khoản tại hệ thống CRUD Demo. "
                  + "Mã OTP kích hoạt tài khoản của bạn là:</p>"
                  + "<h2 style=\"letter-spacing:4px;\">" + otpCode + "</h2>"
                  + "<p>Mã có hiệu lực trong <b>" + Constant.OTP_EXPIRE_MINUTES
                  + " phút</b>. Vui lòng không chia sẻ mã này cho bất kỳ ai.</p>"
                  + "<p>Nếu bạn không thực hiện yêu cầu này, vui lòng bỏ qua email.</p>"
                  + "</div>";

            message.setContent(content, "text/html; charset=UTF-8");

            Transport.send(message);

        } catch (java.io.UnsupportedEncodingException e) {
            throw new MessagingException("Lỗi cấu hình email gửi đi", e);
        }
    }
        public static void sendResetPasswordEmail(String toEmail, String fullName, String otpCode)
            throws MessagingException {

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", Constant.MAIL_HOST);
        props.put("mail.smtp.port", String.valueOf(Constant.MAIL_PORT));

        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Constant.MAIL_USERNAME, Constant.MAIL_APP_PASSWORD);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(Constant.MAIL_USERNAME, Constant.MAIL_APP_PASSWORD));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("[CRUD Demo] Mã OTP đặt lại mật khẩu");

            String content =
                    "<div style=\"font-family:Arial,sans-serif;\">"
                  + "<p>Xin chào <b>" + fullName + "</b>,</p>"
                  + "<p>Bạn vừa yêu cầu đặt lại mật khẩu. Mã OTP của bạn là:</p>"
                  + "<h2 style=\"letter-spacing:4px;\">" + otpCode + "</h2>"
                  + "<p>Mã có hiệu lực trong <b>" + Constant.OTP_EXPIRE_MINUTES
                  + " phút</b>. Nếu bạn không yêu cầu, vui lòng bỏ qua email này.</p>"
                  + "</div>";

            message.setContent(content, "text/html; charset=UTF-8");
            Transport.send(message);

        } catch (java.io.UnsupportedEncodingException e) {
            throw new MessagingException("Lỗi cấu hình email gửi đi", e);
        }
    }
}