package com.tpe.cookerytech.controller;

import com.tpe.cookerytech.dto.request.ResetRequestPassword;
import com.tpe.cookerytech.dto.response.CkResponse;
import com.tpe.cookerytech.service.EmailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping
public class EmailController {

    private final EmailService emailService;


    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }



    //F03 -> It will generate and email reset password code            Page:69
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestParam String email) {

        String status = emailService.sendSimpleMail(email);

        return ResponseEntity.ok(status);
    }



    //F04 -> It will update password
    @PostMapping("/reset-password")
    //@PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<CkResponse> resetPassword(@Valid @RequestBody ResetRequestPassword resetRequestPassword) {

        emailService.resetPasswordMessage(resetRequestPassword.getResetPasswordCode(), resetRequestPassword.getNewPassword());

        CkResponse ckResponse = new CkResponse("Password successfully updated", true);


        return ResponseEntity.ok(ckResponse);
    }


}