package com.tpe.cookerytech.service;

import com.tpe.cookerytech.domain.Currency;
import com.tpe.cookerytech.domain.Offer;
import com.tpe.cookerytech.dto.request.OfferCreateRequest;
import com.tpe.cookerytech.dto.response.OfferResponse;
import com.tpe.cookerytech.mapper.CurrencyMapper;
import com.tpe.cookerytech.mapper.OfferMapper;
import com.tpe.cookerytech.repository.CurrencyRepository;
import com.tpe.cookerytech.repository.OfferRepository;
import net.bytebuddy.utility.RandomString;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OfferService {

    private final UserService userService;
    private final OfferRepository offerRepository;
    private final OfferMapper offerMapper;
    private final CurrencyMapper currencyMapper;
    private final CurrencyRepository currencyRepository;


    public OfferService(UserService userService, OfferRepository offerRepository, OfferMapper offerMapper, CurrencyMapper currencyMapper, CurrencyRepository currencyRepository) {
        this.userService = userService;
        this.offerRepository = offerRepository;
        this.offerMapper = offerMapper;
        this.currencyMapper = currencyMapper;
        this.currencyRepository = currencyRepository;
    }


    public OfferResponse createOfferAuthUser(OfferCreateRequest offerCreateRequest) {

        String randomCode = RandomString.make(8);

        Offer offer = new Offer();

        Currency currency = currencyRepository.findById(2L).orElseThrow(
                ()->new RuntimeException("asdasd")
        );



        offer.setCreateAt(LocalDateTime.now());
        offer.setUser(userService.getCurrentUser());
        offer.setCode(randomCode);
        offer.setDeliveryAt(offerCreateRequest.getDeliveryDate());
        offer.setCurrency(currency);

        offerRepository.save(offer);

        OfferResponse offerResponse = offerMapper.offerToOfferResponse(offer);
        offerResponse.setUserId(offer.getUser().getId());
        offerResponse.setCurrencyResponse(currencyMapper.currencyToCurrencyResponse(offer.getCurrency()));

        return offerResponse;
    }
}
