package com.tpe.cookerytech.service;

import com.tpe.cookerytech.domain.User;
import com.tpe.cookerytech.exception.BadRequestException;
import com.tpe.cookerytech.exception.ResourcesNotFoundException;
import com.tpe.cookerytech.exception.message.ErrorMessage;
import com.tpe.cookerytech.repository.UserRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;



@Service
public class EmailServiceImpl implements EmailService{

    private final JavaMailSender javaMailSender;

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserService userService;

    public EmailServiceImpl(JavaMailSender javaMailSender, UserRepository userRepository, PasswordEncoder passwordEncoder, UserService userService) {
        this.javaMailSender = javaMailSender;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @Value("${spring.mail.username}")
    private String sender;




    @Override
    public String sendSimpleMail(String email) {

        User user = userRepository.findByEmail(email).orElseThrow(()-> new
                ResourcesNotFoundException(ErrorMessage.USER_NOT_FOUND_EXCEPTION));

        try {

            if(user!=null) {
                // Creating a simple mail message
                SimpleMailMessage mailMessage
                        = new SimpleMailMessage();

                // Setting up necessary details
                mailMessage.setFrom(sender);
//                mailMessage.setTo(details.getRecipient());
                mailMessage.setSubject("Email Reset");
                mailMessage.setTo(email);


                //reset code
                String resetCode = UniqueCodeGenerator();
                //String encodeResetCode = passwordEncoder.encode(resetCode);
                user.setResetPasswordCode(resetCode);
                userRepository.save(user);

                String content = "Hello \n\n"
                        + "You have requested to reset your password."
                        + "Click the link below to change your password: "
                        +   resetCode + "  Change my password\n\n\n"
                        + "Ignore this email if you do remember your password, "
                        + "or you have not made the request.";
                mailMessage.setText(content);
                // Sending the mail
                javaMailSender.send(mailMessage);
            }
                return "Mail Sent Successfully...";

        }

        // Catch block to handle the exceptions
        catch (Exception e) {
            return "Error while Sending Mail";
        }
    }

    @Override
    public void resetPasswordMessage(String resetPasswordCode, String newPassword) {
        // String encodeResetCode = passwordEncoder.encode(code);
        User user = userRepository.findByResetPasswordCode(resetPasswordCode).orElseThrow(()->
                new ResourcesNotFoundException(ErrorMessage.USER_NOT_FOUND_EXCEPTION));

        if (!userService.isPasswordValid(newPassword)) {
            throw new BadRequestException(String.format(ErrorMessage.USER_PASSWORD_CONTROL));
        }else {
            String newEncodePassword = passwordEncoder.encode(newPassword);
            user.setPasswordHash(newEncodePassword);
        }
        //user.setResetPasswordCode(null);
        userRepository.save(user);
    }


    private String UniqueCodeGenerator() {
        String resetCode = RandomString.make(16);
        return resetCode;
    }



//    public String
//    sendMailWithAttachment(EmailDetails details)
//    {
//        // Creating a mime message
//        MimeMessage mimeMessage
//                = javaMailSender.createMimeMessage();
//        MimeMessageHelper mimeMessageHelper;
//
//        try {
//
//            // Setting multipart as true for attachments to
//            // be send
//            mimeMessageHelper
//                    = new MimeMessageHelper(mimeMessage, true);
//            mimeMessageHelper.setFrom(sender);
//            mimeMessageHelper.setTo(details.getRecipient());
//            mimeMessageHelper.setText(details.getMsgBody());
//            mimeMessageHelper.setSubject(
//                    details.getSubject());
//
//            // Adding the attachment
//            FileSystemResource file
//                    = new FileSystemResource(
//                    new File(details.getAttachment()));
//
//            mimeMessageHelper.addAttachment(
//                    file.getFilename(), file);
//
//            // Sending the mail
//            javaMailSender.send(mimeMessage);
//            return "Mail sent Successfully";
//        }
//
//        // Catch block to handle MessagingException
//        catch (MessagingException e) {
//
//            // Display message when exception occurred
//            return "Error while sending mail!!!";
//        }
//    }




}
