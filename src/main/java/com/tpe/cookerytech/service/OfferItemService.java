package com.tpe.cookerytech.service;

import com.tpe.cookerytech.domain.OfferItem;
import com.tpe.cookerytech.domain.Role;
import com.tpe.cookerytech.domain.User;
import com.tpe.cookerytech.domain.enums.RoleType;
import com.tpe.cookerytech.dto.request.OfferItemUpdateRequest;
import com.tpe.cookerytech.dto.response.OfferItemResponse;
import com.tpe.cookerytech.exception.BadRequestException;
import com.tpe.cookerytech.exception.ResourceNotFoundException;
import com.tpe.cookerytech.exception.message.ErrorMessage;
import com.tpe.cookerytech.mapper.OfferItemMapper;
import com.tpe.cookerytech.repository.OfferItemRepository;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class OfferItemService {

    private final UserService userService;

    private final OfferItemRepository offerItemRepository;

    private final OfferItemMapper offerItemMapper;


    public OfferItemService(UserService userService, OfferItemRepository offerItemRepository, OfferItemMapper offerItemMapper) {
        this.userService = userService;
        this.offerItemRepository = offerItemRepository;
        this.offerItemMapper = offerItemMapper;
    }

    public OfferItemResponse updateOfferItemWithIdByAdmin(Long id, OfferItemUpdateRequest offerItemUpdateRequest) {

        OfferItem offerItem = offerItemRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessage.OFFER_ITEM_NOT_FOUND_EXCEPTION, id)));

        if (offerItem.getOffer().getStatus() != 0 && offerItem.getOffer().getStatus() != 3) {
            throw new ResourceNotFoundException(ErrorMessage.OFFER_ITEM_COULD_NOT_BE_DELETED);
        }

        offerItem.setQuantity(offerItemUpdateRequest.getQuantity());
        offerItem.setSelling_price(offerItemUpdateRequest.getSelling_price());
        offerItem.setTax(offerItemUpdateRequest.getTax());

        offerItem.getOffer().setDiscount(offerItemUpdateRequest.getDiscount());
        offerItem.setSub_total(offerItemUpdateRequest.getSelling_price() * offerItemUpdateRequest.getQuantity() * (1 + offerItemUpdateRequest.getTax() / 100));

        offerItemRepository.save(offerItem);

        OfferItemResponse offerItemResponse = offerItemMapper.offerItemToOfferItemResponse(offerItem);
        offerItemResponse.setDiscount(offerItem.getOffer().getDiscount());

        offerItemResponse.setSku(offerItem.getSku());
        offerItemResponse.setSubtotal(offerItem.getSub_total());

        return offerItemResponse;
    }


    public OfferItemResponse deleteOfferItemById(Long id) {

        OfferItem offerItem = offerItemRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessage.OFFER_ITEM_NOT_FOUND_EXCEPTION, id)));

        if (offerItem.getOffer().getStatus() != 0 && offerItem.getOffer().getStatus() != 3) {
            throw new ResourceNotFoundException(ErrorMessage.OFFER_ITEM_COULD_NOT_BE_DELETED);
        }
        offerItemRepository.deleteById(id);
        return offerItemMapper.offerItemToOfferItemResponse(offerItem);
    }

}
