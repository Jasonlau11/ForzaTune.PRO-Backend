package com.forzatune.backend.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    @Value("${app.mail.from:noreply@forzatune.pro}")
    private String senderAddress;

    @Value("${app.mail.sender-name:ForzaTune.PRO}")
    private String senderName;

    public void sendVerificationCode(String toEmail, String code) {
        String subject = "[ForzaTune.PRO] 邮箱验证代码";
        String text = String.format("您的验证码为：%s\n验证码10分钟内有效。若非本人操作，请忽略此邮件。\n\nForzaTune.PRO 团队", code);
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(String.format("%s <%s>", senderName, senderAddress));
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
            logger.info("📧 验证码邮件已发送到: {}", maskEmail(toEmail));
        } catch (Exception e) {
            logger.error("❌ 发送验证码邮件失败: {}", e.getMessage());
            throw new RuntimeException("邮件发送失败，请稍后重试");
        }
    }

    private String maskEmail(String email) {
        int atIndex = email.indexOf('@');
        if (atIndex <= 1) return "***" + email.substring(atIndex);
        String prefix = email.substring(0, Math.min(2, atIndex));
        return prefix + "***" + email.substring(atIndex);
    }
}

