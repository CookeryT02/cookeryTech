package com.tpe.cookerytech.controller;

import com.tpe.cookerytech.dto.request.OfferItemUpdateRequest;
import com.tpe.cookerytech.dto.response.OfferItemResponse;
import com.tpe.cookerytech.service.OfferItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/offer-items")
public class OfferItemController {
    private final OfferItemService offerItemService;

    public OfferItemController(OfferItemService offerItemService) {
        this.offerItemService = offerItemService;
    }



    //E08 -> It will update the offer item    Page:64
    @PutMapping("/{id}/admin")
    @PreAuthorize("hasRole('SALES_SPECIALIST')")
    public ResponseEntity<OfferItemResponse> updateOfferItemWithIdByAdmin(@PathVariable Long id,
                                                                          @RequestBody OfferItemUpdateRequest offerItemUpdateRequest){

        OfferItemResponse offerItemResponse= offerItemService.updateOfferItemWithIdByAdmin(id,offerItemUpdateRequest);

        return ResponseEntity.ok(offerItemResponse);
    }




    //E09 -> It willwilldeletethe offer item
    @DeleteMapping("/{id}/admin")
    @PreAuthorize("hasRole('SALES_SPECIALIST')")
    public ResponseEntity<OfferItemResponse> deleteOfferItem(@PathVariable Long id){

        OfferItemResponse offerItemResponse = offerItemService.deleteOfferItemById(id);

        return ResponseEntity.ok(offerItemResponse);
    }

    //--------------------------------------------------------------------------------
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST')")
    public ResponseEntity<List<OfferItemResponse>> getAllOfferItems(){

        List<OfferItemResponse> offerItemResponseList= offerItemService.getAllOfferItems();

        return ResponseEntity.ok(offerItemResponseList);

    }

    //---------------------------------------------------------------------------------

}
