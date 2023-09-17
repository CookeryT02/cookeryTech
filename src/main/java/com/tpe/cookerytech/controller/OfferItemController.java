package com.tpe.cookerytech.controller;

import com.tpe.cookerytech.dto.request.OfferItemUpdateRequest;
import com.tpe.cookerytech.dto.response.OfferItemResponse;
import com.tpe.cookerytech.service.OfferItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/offer-items")
public class OfferItemController {

    private final OfferItemService offerItemService;

    public OfferItemController(OfferItemService offerItemService) {
        this.offerItemService = offerItemService;
    }

    @PutMapping("/{id}/admin")
    @PreAuthorize("hasRole('SALES_SPECIALIST')")
    public ResponseEntity<OfferItemResponse> updateOfferItemWithIdByAdmin(@PathVariable Long id,
                                                                          @RequestBody OfferItemUpdateRequest offerItemUpdateRequest){

        OfferItemResponse offerItemResponse= offerItemService.updateOfferItemWithIdByAdmin(id,offerItemUpdateRequest);

        return ResponseEntity.ok(offerItemResponse);

    }
}
