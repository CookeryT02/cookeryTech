package com.tpe.cookerytech.service;

import com.tpe.cookerytech.dto.request.ContactMessageRequest;
import com.tpe.cookerytech.dto.response.CkResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class ContactMessageService {

    private final JavaMailSender javaMailSender;

    public ContactMessageService(JavaMailSender javaMailSender) {

        this.javaMailSender = javaMailSender;

    }

    @Value("${spring.mail.username}")
    private String email;


    public CkResponse createContactMessage(ContactMessageRequest contactMessageRequest) {


        try {
                // Creating a simple mail message
                SimpleMailMessage mailMessage
                        = new SimpleMailMessage();

                // Setting up necessary details
                mailMessage.setFrom(contactMessageRequest.getEmail());
                mailMessage.setSubject("Contact Me");
                mailMessage.setTo(email);


                String content = " Hello my name is " + contactMessageRequest.getName()
                        + "\n\n My product interested me my phone number " + contactMessageRequest.getPhone()
                        + "\n\n My email adress " + contactMessageRequest.getEmail()
                        + "\n\n Could you please get back to me again?"
                        + "\n\n My company name : " + contactMessageRequest.getCompany()
                        + "\n\n My Message: " + contactMessageRequest.getMessage();
                mailMessage.setText(content);
                // Sending the mail
                javaMailSender.send(mailMessage);

            return new CkResponse("Contact Messages Successfully", true);

        }

        // Catch block to handle the exceptions
        catch (Exception e) {

            return new CkResponse("Contact Messages Could Not Be Sent", false);

        }

    }
}
