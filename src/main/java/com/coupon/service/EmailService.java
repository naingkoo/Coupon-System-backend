package com.coupon.service;

import com.coupon.model.MessageObject;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class EmailService {
    @Value("${spring.mail.username}")
    private String senderEmail;
    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }
    public void sendMail(MessageObject messageBody) throws MessagingException {
//        SimpleMailMessage message= new SimpleMailMessage();
//        message.setTo(messageBody.to());
//        message.setFrom(senderEmail);
//        message.setSubject(messageBody.Subject());
//        message.setText(messageBody.text());


        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
        helper.setTo(messageBody.to());
        helper.setSubject(messageBody.Subject());
        helper.setText(messageBody.text(), true); // Set 'true' to indicate HTML content
        javaMailSender.send(mimeMessage);
    }

}
