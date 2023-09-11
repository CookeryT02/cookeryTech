package com.tpe.cookerytech.service;

public interface EmailService {

    String sendSimpleMail(String email);

    void resetPasswordMessage(String resetPasswordCode, String newPassword);


    //String sendMailWithAttachment(EmailDetails details);
}
