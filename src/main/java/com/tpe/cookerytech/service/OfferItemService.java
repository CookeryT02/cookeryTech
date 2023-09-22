package com.tpe.cookerytech.service;

import com.tpe.cookerytech.domain.*;
import com.tpe.cookerytech.domain.enums.LogType;
import com.tpe.cookerytech.dto.request.OfferItemUpdateRequest;
import com.tpe.cookerytech.dto.response.OfferItemResponse;
import com.tpe.cookerytech.exception.ResourceNotFoundException;
import com.tpe.cookerytech.exception.message.ErrorMessage;
import com.tpe.cookerytech.mapper.OfferItemMapper;
import com.tpe.cookerytech.repository.LogRepository;
import com.tpe.cookerytech.repository.OfferItemRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OfferItemService {

    private final UserService userService;
    private final OfferItemRepository offerItemRepository;
    private final OfferItemMapper offerItemMapper;
    private final LogRepository logRepository;

    public OfferItemService(UserService userService, OfferItemRepository offerItemRepository, OfferItemMapper offerItemMapper, LogRepository logRepository) {
        this.userService = userService;
        this.offerItemRepository = offerItemRepository;
        this.offerItemMapper = offerItemMapper;
        this.logRepository = logRepository;
    }



    //E08
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

        Log log = new Log();
        log.setUser(offerItem.getOffer().getUser());
        log.setOffer(offerItem.getOffer());
        log.setCreateAt(LocalDateTime.now());
        if (offerItem.getOffer().getStatus() == 1){
            log.setLog(LogType.UPDATED);
        }else if(offerItem.getOffer().getStatus()==3){
            log.setLog(LogType.DECLINED);
        }
        logRepository.save(log);

        offerItemRepository.save(offerItem);

        OfferItemResponse offerItemResponse = offerItemMapper.offerItemToOfferItemResponse(offerItem);
        offerItemResponse.setDiscount(offerItem.getOffer().getDiscount());

        offerItemResponse.setSku(offerItem.getSku());
        offerItemResponse.setSubtotal(offerItem.getSub_total());

        return offerItemResponse;
    }




    //E09
    public OfferItemResponse deleteOfferItemById(Long id) {
        User user = userService.getCurrentUser();

        OfferItem offerItem = offerItemRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException(String.format(ErrorMessage.OFFER_ITEM_NOT_FOUND_EXCEPTION, id)));

        if (offerItem.getOffer().getStatus() != 0 && offerItem.getOffer().getStatus() != 3) {
            throw new ResourceNotFoundException(ErrorMessage.OFFER_ITEM_COULD_NOT_BE_DELETED);
        }
        Log log =new Log();
        log.setUser(user);
        log.setOffer(offerItem.getOffer());
        log.setCreateAt(LocalDateTime.now());
        log.setLog(LogType.DELETED);
        logRepository.save(log);

        offerItemRepository.deleteById(id);
        return offerItemMapper.offerItemToOfferItemResponse(offerItem);
    }
}
