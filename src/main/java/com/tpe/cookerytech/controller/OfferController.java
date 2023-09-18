package com.tpe.cookerytech.controller;

import com.tpe.cookerytech.dto.request.OfferCreateRequest;
import com.tpe.cookerytech.dto.response.OfferResponse;
import com.tpe.cookerytech.dto.response.ProductResponse;
import com.tpe.cookerytech.dto.request.OfferItemUpdateRequest;
import com.tpe.cookerytech.dto.response.*;
import com.tpe.cookerytech.repository.OfferRepository;
import com.tpe.cookerytech.dto.response.OfferResponseWithUser;
import com.tpe.cookerytech.service.OfferService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.tpe.cookerytech.dto.request.OfferUpdateRequest;
import com.tpe.cookerytech.dto.response.OfferResponseWithUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import javax.websocket.server.PathParam;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.LocalDateTime;


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
    public ResponseEntity<OfferResponse> createOfferAuthUser(@RequestBody OfferCreateRequest offerCreateRequest) {

        OfferResponse offerResponse = offerService.createOfferAuthUser(offerCreateRequest);

        return ResponseEntity.ok(offerResponse);
    }





//    @GetMapping("/admin/user/{id}")
//    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST')")
//    public ResponseEntity<Page<OfferResponse>> getUserOffers(
//            @RequestParam("id") Long id,
//            @RequestParam(value = "status") Byte status,
//            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date1,
//            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date2,
//            @RequestParam(value = "page", defaultValue = "0") int page,
//            @RequestParam(value = "size", defaultValue = "20") int size,
//            @RequestParam(value = "sort", defaultValue = "createAt") String sort,
//            @RequestParam(value = "type", defaultValue = "DESC") String type
//    ) {
//
//
//        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(type), sort));
//
//        Page<OfferResponse> offerResponse = offerService.getUserOfferById(id,pageable,status,date1,date2);
//
//        return ResponseEntity.ok(offerResponse);
//
//
//
//    }






    @GetMapping("/{id}/auth")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST') or hasRole('PRODUCT_MANAGER') or hasRole('CUSTOMER')")
    public ResponseEntity<OfferResponse> getOfferByAuthUser(@Valid @PathVariable Long id) {

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




    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST')")
    public ResponseEntity<Page<OfferResponseWithUser>> getOffers(@RequestParam("q") String q,
                                                                 @RequestParam(value = "status", defaultValue = "0") byte status,
                                                                 @RequestParam("startingDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime startingDate,
                                                                 @RequestParam("endingDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime endingDate,
                                                                 @RequestParam(value = "page", defaultValue = "0") int page,
                                                                 @RequestParam(value = "size", defaultValue = "20") int size,
                                                                 @RequestParam(value = "sort", defaultValue = "createAt") String prop,
                                                                 @RequestParam(value = "type", required = false, defaultValue = "DESC") Sort.Direction direction
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, prop));
        Page<OfferResponseWithUser> offerResponseWithUsersPage = offerService.getOffers(q, pageable, startingDate, endingDate);

        return ResponseEntity.ok(offerResponseWithUsersPage);
    }

    @PutMapping("/{id}/admin")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST')")
    public ResponseEntity<OfferResponseWithUser> updateOfterByIdAuthorizedPeople(@PathVariable Long id, @Valid @RequestBody OfferUpdateRequest offerUpdateRequest) {

        OfferResponseWithUser offerResponseWithUser = offerService.updateOfferByManagements(id, offerUpdateRequest);

        return ResponseEntity.ok(offerResponseWithUser);

    }


}

