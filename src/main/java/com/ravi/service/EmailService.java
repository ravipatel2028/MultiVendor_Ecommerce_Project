package com.ravi.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    public void sendVerificationOtpEmail(String userEmail, String otp, String subject, String text) throws MailSendException {
       System.out.println("Sending verification OTP email to " + userEmail);
        try{
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "utf-8" );
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setTo(userEmail);
            mimeMessageHelper.setText(text+ otp);
            mimeMessageHelper.setFrom("rp8008077@gmail.com");
            mailSender.send(mimeMessage);
            System.out.println("Email sent");
        }
        catch(Exception e){
            e.printStackTrace();
            throw new MailSendException("Failed to send message", e);
        }
    }
}
