package com.tpe.cookerytech.controller;

import com.tpe.cookerytech.dto.request.OfferCreateRequest;
import com.tpe.cookerytech.dto.response.OfferResponse;
import com.tpe.cookerytech.dto.response.ProductResponse;
import com.tpe.cookerytech.service.OfferService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;


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


//    @GetMapping
//    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST')")
//    public List<OfferResponse> getUserOffers(
//            @PathVariable("userId") Long userId,
//            @RequestParam(value = "status", required = false) String status,
//            @RequestParam(value = "date1", required = false) LocalDate date1,
//            @RequestParam(value = "date2", required = false) LocalDate date2,
//            @RequestParam(value = "page", defaultValue = "0") int page,
//            @RequestParam(value = "size", defaultValue = "20") int size,
//            @RequestParam(value = "sort", defaultValue = "create_at") String sort,
//            @RequestParam(value = "type", defaultValue = "desc") String type
//    ) {
//        // Implement the logic to retrieve offers based on the provided parameters
//        // Use the offerRepository to query the database and apply pagination, sorting, and filtering as needed
//        // Return the list of offers
//
//        return null;
//
//
//    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST')")
    public ResponseEntity<Page<OfferResponse>> getUserOffers(
            @PathVariable("userId") Long id,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "date1", required = false) LocalDate date1,
            @RequestParam(value = "date2", required = false) LocalDate date2,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "20") int size,
            @RequestParam(value = "sort", defaultValue = "createAt") String sort,
            @RequestParam(value = "type", defaultValue = "desc") String type
    ) {
        // Implement the logic to retrieve offers based on the provided parameters
        // Use the offerRepository to query the database and apply pagination, sorting, and filtering as needed
        // Return the list of offers

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.valueOf(type), sort));

        Page<OfferResponse> offerResponse = offerService.getUserOfferById(id,pageable,status,date1,date2);

        return ResponseEntity.ok(offerResponse);



    }





    @GetMapping("/{id}/auth")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST') or hasRole('PRODUCT_MANAGER') or hasRole('CUSTOMER')")
    public ResponseEntity<OfferResponse> getOfferByAuthUser(@Valid @PathVariable Long id){

        OfferResponse offerResponse = offerService.getOfferByAuthUser(id);

        return ResponseEntity.ok(offerResponse);
    }

}
