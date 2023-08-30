package com.tpe.cookerytech.service;

import com.tpe.cookerytech.dto.response.CkResponse;
import com.tpe.cookerytech.dto.response.EmailDetails;

public interface EmailService {

    String sendSimpleMail(String email);

    void resetPasswordMessage(String resetPasswordCode, String newPassword);


    //String sendMailWithAttachment(EmailDetails details);


}
