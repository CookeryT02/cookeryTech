package com.tpe.cookerytech.service;

import com.tpe.cookerytech.dto.request.ContactMessageRequest;
import com.tpe.cookerytech.dto.response.CkResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
public class ContactMessageService {

    private final JavaMailSender javaMailSender;

    public ContactMessageService(JavaMailSender javaMailSender) {

        this.javaMailSender = javaMailSender;

    }

    @Value("${spring.mail.username}")
    private String email;


    //J01
    public CkResponse createContactMessage(ContactMessageRequest contactMessageRequest) {

        try {
            // Creating an HTML email message
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            // Setting up necessary details
            helper.setFrom(contactMessageRequest.getEmail());
            helper.setTo(email);
            helper.setSubject("Contact Me");

            // Creating the HTML content
            String content = "<html><body>" +
                    "<h3>Hello, my name is " + contactMessageRequest.getName() + "</h3>" +
                    "<p>Your products interested me, and here is my contact information:</p>" +
                    "<ul>" +
                    "<li><b>Phone Number:</b> " +contactMessageRequest.getPhone() + "</li>" +
                    "<li><b>Email Address:</b> " + contactMessageRequest.getEmail() + "</li>" +
                    "<li><b>Company Name:</b> " + contactMessageRequest.getCompany() + "</li>" +
                    "</ul>" +
                    "<p><b>Message:</b></p>" +
                    "<p>" + contactMessageRequest.getMessage() + "</p>" +
                    "<p>Could you please get back to me?</p>" +
                    "</body></html>";

            helper.setText(content, true); // true parameter indicates HTML content

            // Sending the mail
            javaMailSender.send(mimeMessage);

            return new CkResponse("Contact Messages Successfully", true);

        } catch (Exception e) {

            // Handle any exceptions that may occur during email sending
            return new CkResponse("Error sending contact message: " + e.getMessage(), false);
        }
    }
}
