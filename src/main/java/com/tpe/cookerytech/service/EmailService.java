package com.tpe.cookerytech.service;

import com.tpe.cookerytech.dto.response.OfferResponse;

public interface EmailService {

    String sendSimpleMail(String email);

    void resetPasswordMessage(String resetPasswordCode, String newPassword);

    void sendOfferEmail(OfferResponse offerResponse);

}
