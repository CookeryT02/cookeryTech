package com.tpe.cookerytech.controller;

import com.tpe.cookerytech.dto.request.OfferCreateRequest;
import com.tpe.cookerytech.dto.response.OfferResponse;
import com.tpe.cookerytech.service.OfferService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/offers")
public class OfferController {


    private final OfferService offerService;


    public OfferController(OfferService offerService) {
        this.offerService = offerService;
    }

    @PostMapping("/auth")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST') or hasRole('PRODUCT_MANAGER') or hasRole('CUSTOMER')")
    public ResponseEntity<OfferResponse> createOfferAuthUser(@RequestBody OfferCreateRequest offerCreateRequest){

        OfferResponse offerResponse = offerService.createOfferAuthUser(offerCreateRequest);

        return ResponseEntity.ok(offerResponse);
    }


    @GetMapping("/{id}/auth")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST') or hasRole('PRODUCT_MANAGER') or hasRole('CUSTOMER')")
    public ResponseEntity<OfferResponse> getOfferByAuthUser(@Valid @PathVariable Long id){

        OfferResponse offerResponse = offerService.getOfferByAuthUser(id);

        return ResponseEntity.ok(offerResponse);
    }

}
