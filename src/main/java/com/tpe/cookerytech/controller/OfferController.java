package com.tpe.cookerytech.controller;

import com.tpe.cookerytech.dto.request.OfferCreateRequest;
import com.tpe.cookerytech.dto.request.OfferItemUpdateRequest;
import com.tpe.cookerytech.dto.request.OfferUpdateRequest;
import com.tpe.cookerytech.dto.response.OfferItemResponse;
import com.tpe.cookerytech.dto.request.OfferItemUpdateRequest;
import com.tpe.cookerytech.dto.response.OfferItemResponse;
import com.tpe.cookerytech.dto.response.OfferResponse;
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

import javax.validation.Valid;
import java.time.LocalDateTime;


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

    @PutMapping("/offer-items/{id}/admin")
    @PreAuthorize("hasRole('SALES_SPECIALIST')")
    public ResponseEntity<OfferItemResponse> updateOfferItemWithIdByAdmin(@PathVariable Long id,
                                                                          @RequestBody OfferItemUpdateRequest offerItemUpdateRequest){

        OfferItemResponse offerItemResponse= offerService.updateOfferItemWithIdByAdmin(id,offerItemUpdateRequest);

        return ResponseEntity.ok(offerItemResponse);

    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST')")
    public ResponseEntity<Page<OfferResponseWithUser>> getOffers(@RequestParam("q")String q,
                                                                 @RequestParam(value = "status",defaultValue = "0") byte status,
                                                                 @RequestParam("startingDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime startingDate,
                                                                 @RequestParam("endingDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime endingDate,
                                                                 @RequestParam(value = "page", defaultValue = "0") int page,
                                                                 @RequestParam(value = "size", defaultValue = "20") int size,
                                                                 @RequestParam(value = "sort",defaultValue = "createAt") String prop,
                                                                 @RequestParam(value = "type",required = false,defaultValue = "DESC")Sort.Direction direction
                                                         ){
        Pageable pageable = PageRequest.of(page,size,Sort.by(direction,prop));
        Page<OfferResponseWithUser> offerResponseWithUsersPage = offerService.getOffers(q,pageable,startingDate,endingDate);

        return ResponseEntity.ok(offerResponseWithUsersPage);
    }

    @PutMapping("/{id}/admin")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST')")
    public ResponseEntity<OfferResponseWithUser> updateOfterByIdAuthorizedPeople(@PathVariable Long id, @Valid @RequestBody OfferUpdateRequest offerUpdateRequest){

        OfferResponseWithUser offerResponseWithUser = offerService.updateOfferByManagements(id,offerUpdateRequest);

        return ResponseEntity.ok(offerResponseWithUser);

    }


}