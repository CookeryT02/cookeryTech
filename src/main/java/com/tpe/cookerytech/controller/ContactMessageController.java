package com.tpe.cookerytech.controller;


import com.tpe.cookerytech.dto.request.ContactMessageRequest;
import com.tpe.cookerytech.dto.response.CkResponse;
import com.tpe.cookerytech.service.ContactMessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/contact-messages")
public class ContactMessageController {

    private final ContactMessageService contactMessageService;

    public ContactMessageController(ContactMessageService contactMessageService) {
        this.contactMessageService = contactMessageService;
    }


    //J01 -> It will get an image of a product
    @PostMapping
    public ResponseEntity<CkResponse> createContactMessages(@Valid @RequestBody ContactMessageRequest contactMessageRequest){

        CkResponse ckResponse = contactMessageService.createContactMessage(contactMessageRequest);

        return ResponseEntity.ok(ckResponse);
    }

}
