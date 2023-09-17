package com.tpe.cookerytech.controller;

import com.tpe.cookerytech.dto.request.OfferCreateRequest;
import com.tpe.cookerytech.dto.response.OfferResponse;
import com.tpe.cookerytech.dto.response.OfferResponseWithUser;
import com.tpe.cookerytech.repository.OfferRepository;
import com.tpe.cookerytech.service.OfferService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.websocket.server.PathParam;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


@RestController
@RequestMapping("/offers")
public class OfferController {


    private final OfferService offerService;
    public final OfferRepository offerRepository;


    public OfferController(OfferService offerService, OfferRepository offerRepository) {
        this.offerService = offerService;
        this.offerRepository = offerRepository;
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


    @GetMapping("/auth")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST') or hasRole('PRODUCT_MANAGER') or hasRole('CUSTOMER')")
    public ResponseEntity<Page<OfferResponseWithUser>> getOffersAccordingTimeAuthUser(
            @RequestParam(required = false, defaultValue = "", name = "q") String q,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date1,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date2,
            @RequestParam(required = false) Byte status,
            @RequestParam(required = false, defaultValue = "0", name = "page") int page,
            @RequestParam(required = false, defaultValue = "20", name = "size") int size,
            @RequestParam(required = false, defaultValue = "createAt", name = "sort") String sort,
            @RequestParam(required = false, defaultValue = "DESC", name = "type") String type) {


        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(type), sort));

        Page<OfferResponseWithUser> offerResponses  = offerService.getOffersAccordingTimeAuthUser(q,pageable,date1,date2);

        return ResponseEntity.ok(offerResponses);
    }

    @GetMapping("/admin/user/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST')")
    public ResponseEntity<Page<OfferResponseWithUser>> getUserOffers(
            @PathVariable("id") Long id,
            @RequestParam(required = false) Byte status,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date1,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date2,
            @RequestParam(required = false, defaultValue = "0", name = "page") int page,
            @RequestParam(required = false, defaultValue = "20", name = "size") int size,
            @RequestParam(required = false, defaultValue = "createAt", name = "sort") String sort,
            @RequestParam(required = false, defaultValue = "DESC", name = "type") String type) {



        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(type), sort));

        Page<OfferResponseWithUser> offerResponseWithUser = offerService.getUserOfferById(id,pageable,status,date1,date2);

        return ResponseEntity.ok(offerResponseWithUser);


    }


}
