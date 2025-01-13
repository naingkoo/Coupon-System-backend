package com.coupon.service;

import com.coupon.entity.ForgetPassword;
import com.coupon.entity.UserEntity;
import com.coupon.model.MessageObject;
import com.coupon.reposistory.ForgetPasswordRepository;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;

@Service
public class ForgetPasswordService {
    @Autowired
    private UserService userService;
    @Autowired
    private ForgetPasswordRepository forgetPasswordRepository;
    @Autowired
    private EmailService emailService;


    public ForgetPassword sendOTP(String email) throws MessagingException {
        ForgetPassword forgetPassword=forgetPasswordRepository.findOtpByEmail(email).orElse(null);
        if(forgetPassword!=null && forgetPassword.getExpired_date().before(new Date(System.currentTimeMillis()))) {
            forgetPasswordRepository.delete(forgetPassword);
          return  sendMailForgetPassword(email);
        }else if(forgetPassword==null){
               return sendMailForgetPassword(email);
        }

        return forgetPassword;
    }

    public Boolean VerifyOTP(Integer otp,String email){
        ForgetPassword forgetPassword=forgetPasswordRepository.findOtpByEmail(email).orElseThrow() ;
        System.out.println(forgetPassword.getUser().toString());
        if(!forgetPassword.getExpired_date().before(new Date(System.currentTimeMillis())) ){
            if(forgetPassword.getOtp().equals(otp)){
                forgetPasswordRepository.delete(forgetPassword);
                return true;
            }
        }else{
            forgetPasswordRepository.delete(forgetPassword);
        }
        return false;
    }

    private Integer generateOTP(){
        Random otp=new Random();
        return otp.nextInt(000_000,999_999);
    }


    private ForgetPassword sendMailForgetPassword(String email) throws MessagingException {
        UserEntity userEntity = userService.findByEmail(email);
        ForgetPassword forgetPassword=new ForgetPassword();
        forgetPassword.setOtp(generateOTP());
        forgetPassword.setExpired_date(new Date(System.currentTimeMillis() + 1000 * 300));
        forgetPassword.setUser(userEntity);
        forgetPasswordRepository.save(forgetPassword);
        String messageBody = String.format(
                "<html>" +
                        "<body>" +
                        "<p>Dear %s,</p>" +
                        "<p>We have received a request to reset your password. Your One-Time Password (OTP) for resetting your password is:</p>" +
                        "<h2 style='color: #2E86C1;'>%s</h2>" +
                        "<p>Please note that this OTP will expire in 5 minutes. If you did not request a password reset, please ignore this message.</p>" +
                        "<p>To reset your password, click the button below:</p>" +
                        "<a href='' style='text-decoration: none;'>" +
                        "<button style='background-color: #2E86C1; color: white; padding: 10px 20px; border: none; border-radius: 5px;'>Reset Password</button>" +
                        "</a>" +
                        "<p>Thank you,<br>The Support Team</p>" +
                        "</body>" +
                        "</html>",
                userEntity.getName(),
                forgetPassword.getOtp(),
                userEntity.getEmail(),
                forgetPassword.getOtp()
        );

        MessageObject message = new MessageObject(userEntity.getEmail(), "Forget Password OTP From Coupon", messageBody);
        emailService.sendMail(message);
        return forgetPassword;
    }

}
