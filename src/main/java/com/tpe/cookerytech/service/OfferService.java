package com.tpe.cookerytech.service;

import com.tpe.cookerytech.domain.*;
import com.tpe.cookerytech.dto.request.OfferCreateRequest;
import com.tpe.cookerytech.dto.response.OfferResponse;
import com.tpe.cookerytech.dto.response.OfferResponseWithUser;
import com.tpe.cookerytech.exception.BadRequestException;
import com.tpe.cookerytech.exception.ResourceNotFoundException;
import com.tpe.cookerytech.exception.message.ErrorMessage;
import com.tpe.cookerytech.mapper.CurrencyMapper;
import com.tpe.cookerytech.mapper.OfferMapper;
import com.tpe.cookerytech.mapper.UserMapper;
import com.tpe.cookerytech.repository.*;
import net.bytebuddy.utility.RandomString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class OfferService {

    private final UserService userService;
    private final OfferRepository offerRepository;
    private final OfferMapper offerMapper;
    private final CurrencyMapper currencyMapper;
    private final CurrencyRepository currencyRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartItemRepository shoppingCartItemRepository;
    private final OfferItemRepository offerItemRepository;
    private final UserMapper userMapper;
    private final UserRepository userRepository;


    public OfferService(UserService userService, OfferRepository offerRepository, OfferMapper offerMapper, CurrencyMapper currencyMapper, CurrencyRepository currencyRepository, ShoppingCartRepository shoppingCartRepository, ShoppingCartItemRepository shoppingCartItemRepository, OfferItemRepository offerItemRepository, UserMapper userMapper, UserRepository userRepository) {
        this.userService = userService;
        this.offerRepository = offerRepository;
        this.offerMapper = offerMapper;
        this.currencyMapper = currencyMapper;
        this.currencyRepository = currencyRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.shoppingCartItemRepository = shoppingCartItemRepository;
        this.offerItemRepository = offerItemRepository;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }


    public OfferResponse createOfferAuthUser(OfferCreateRequest offerCreateRequest) {

        String randomCode = RandomString.make(8);

        User user = userService.getCurrentUser();

        Offer offer = new Offer();

        Currency currency = currencyRepository.findById(3L).orElseThrow(
                () -> new ResourceNotFoundException(ErrorMessage.CURRENCY_NOT_FOUND_EXCEPTION)
        );


        offer.setCreateAt(LocalDateTime.now());
        offer.setUser(user);
        offer.setCode(randomCode);
        offer.setDeliveryAt(offerCreateRequest.getDeliveryDate());
        offer.setCurrency(currency);
        offer.setDiscount(0);

        offerRepository.save(offer);


        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(user.getId()).orElseThrow(() ->
                new ResourceNotFoundException(ErrorMessage.SHOPPING_CART_NOT_FOUND));
        List<ShoppingCartItem> shoppingCartItemList = shoppingCartItemRepository.findByShoppingCartId(shoppingCart.getId());

        double offersubTotal = 0;

        List<OfferItem> offerItemList = new ArrayList<>();

        for (ShoppingCartItem shoppingCartItem : shoppingCartItemList) {
            OfferItem offerItem = new OfferItem();
            offerItem.setSku(shoppingCartItem.getModel().getSku());
            offerItem.setQuantity(shoppingCartItem.getModel().getIn_box_quantity());
            offerItem.setSelling_price(shoppingCartItem.getModel().getBuying_price() + (shoppingCartItem.getModel().getBuying_price() *
                    shoppingCartItem.getModel().getProduct().getBrand().getProfitRate()));
            offerItem.setTax(shoppingCartItem.getModel().getTax_rate());
            offerItem.setProduct(shoppingCartItem.getProduct());
            offerItem.setCreateAt(LocalDateTime.now());
            offerItem.setOffer(offer);
            offerItem.setSub_total(offerItem.getSelling_price()*offerItem.getQuantity()*(1+offerItem.getTax()/100));
            offerItemList.add(offerItem);
            offersubTotal += offerItem.getSub_total();

        }


        offer.setSubTotal(offersubTotal);
        offer.setGrandTotal(offersubTotal * (1 - offer.getDiscount() / 100));

        offerRepository.save(offer);

        for (OfferItem offerItem : offerItemList){
            offerItemRepository.save(offerItem);
        }

        OfferResponse offerResponse = offerMapper.offerToOfferResponse(offer);
        offerResponse.setUserId(offer.getUser().getId());
        offerResponse.setCurrencyResponse(currencyMapper.currencyToCurrencyResponse(offer.getCurrency()));

        return offerResponse;
    }

    public OfferResponse getOfferByAuthUser(Long id) {
        User user = userService.getCurrentUser();

        List<Offer> offerList = offerRepository.findByUserId(user.getId());

        OfferResponse offerResponse = new OfferResponse();

        for (Offer offer: offerList){
            if(Objects.equals(offer.getId(), id)){
                offerResponse = offerMapper.offerToOfferResponse(offer);
                offerResponse.setUserId(user.getId());
                offerResponse.setCurrencyResponse(currencyMapper.currencyToCurrencyResponse(offer.getCurrency()));
                return offerResponse;
            }
        }
        if (offerResponse.getCode()==null) {
            throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
        }
        return null;

        }


    public Page<OfferResponseWithUser> getOffersAccordingTimeAuthUser(String q, Pageable pageable, LocalDate date1, LocalDate date2) {

        Page<Offer> offerPages = offerRepository.findByCreateAtBetweenOrderByCreateAt(q, date1, date2, pageable);

        Page<OfferResponseWithUser> offerWithUser= offerPages.map(offer -> {
            OfferResponseWithUser offerResponseWithUser = offerMapper.offerToOfferResponsewithUser(offer);
            offerResponseWithUser.setUserResponse(userMapper.userToUserResponse(offer.getUser()));
            offerResponseWithUser.setCurrencyResponse(currencyMapper.currencyToCurrencyResponse(offer.getCurrency()));
            return offerResponseWithUser;
        });

        return offerWithUser;
    }

    public Page<OfferResponseWithUser> getUserOfferById(Long id, Pageable pageable, Byte status, LocalDate date1, LocalDate date2) {




        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null || authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))
                || authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_SALES_SPECIALIST"))
                || authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_SALES_MANAGER")) ) {

            userRepository.findById(id).orElseThrow(
                    () -> new BadRequestException(String.format(ErrorMessage.USER_NOT_FOUND_EXCEPTION, id)));

            Page<Offer> offerPages = offerRepository.findByUserIdBetweenOrderByCreateAt(id, pageable, status, date1, date2);

            Page<OfferResponseWithUser> offersWithUser= offerPages.map(offer -> {
                OfferResponseWithUser offerResponseWithUser = offerMapper.offerToOfferResponsewithUser(offer);
                offerResponseWithUser.setUserResponse(userMapper.userToUserResponse(offer.getUser()));
                offerResponseWithUser.setCurrencyResponse(currencyMapper.currencyToCurrencyResponse(offer.getCurrency()));
                return offerResponseWithUser;
            });

            return offersWithUser;

        } else {
            throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
        }

    }
}

