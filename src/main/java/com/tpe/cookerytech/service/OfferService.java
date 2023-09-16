package com.tpe.cookerytech.service;

import com.tpe.cookerytech.domain.*;
import com.tpe.cookerytech.domain.enums.RoleType;
import com.tpe.cookerytech.dto.request.OfferCreateRequest;
import com.tpe.cookerytech.dto.request.OfferItemUpdateRequest;
import com.tpe.cookerytech.dto.response.OfferItemResponse;
import com.tpe.cookerytech.dto.response.OfferResponse;
import com.tpe.cookerytech.dto.response.OfferResponseWithUser;
import com.tpe.cookerytech.exception.BadRequestException;
import com.tpe.cookerytech.exception.ResourceNotFoundException;
import com.tpe.cookerytech.exception.message.ErrorMessage;
import com.tpe.cookerytech.mapper.CurrencyMapper;
import com.tpe.cookerytech.mapper.OfferItemMapper;
import com.tpe.cookerytech.mapper.OfferMapper;
import com.tpe.cookerytech.mapper.UserMapper;
import com.tpe.cookerytech.repository.*;
import net.bytebuddy.utility.RandomString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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

    private final OfferItemMapper offerItemMapper;

    private final UserMapper userMapper;



    public OfferService(UserService userService, OfferRepository offerRepository, OfferMapper offerMapper, CurrencyMapper currencyMapper, CurrencyRepository currencyRepository, ShoppingCartRepository shoppingCartRepository, ShoppingCartItemRepository shoppingCartItemRepository, OfferItemRepository offerItemRepository, OfferItemMapper offerItemMapper, UserMapper userMapper) {
        this.userService = userService;
        this.offerRepository = offerRepository;
        this.offerMapper = offerMapper;
        this.currencyMapper = currencyMapper;
        this.currencyRepository = currencyRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.shoppingCartItemRepository = shoppingCartItemRepository;
        this.offerItemRepository = offerItemRepository;
        this.offerItemMapper = offerItemMapper;
        this.userMapper = userMapper;
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

    public OfferItemResponse updateOfferItemWithIdByAdmin(Long id, OfferItemUpdateRequest offerItemUpdateRequest) {

        User user = userService.getCurrentUser();

        OfferItem offerItem=offerItemRepository.findById(id).
                orElseThrow(()->new ResourceNotFoundException(String.format(ErrorMessage.OFFER_ITEM_NOT_FOUND_EXCEPTION,id)));

        Set<Role> roleControl = user.getRoles();
        for(Role r:roleControl) {
            if (r.getType().equals(RoleType.ROLE_SALES_SPECIALIST) &&
                    (offerItem.getOffer().getStatus()==0 ||offerItem.getOffer().getStatus()==3) ) {

                offerItem.setQuantity(offerItemUpdateRequest.getQuantity());
                offerItem.setSelling_price(offerItemUpdateRequest.getSelling_price());
                offerItem.setTax(offerItemUpdateRequest.getTax());

                offerItem.getOffer().setDiscount(offerItemUpdateRequest.getDiscount());
                offerItem.setSub_total(offerItemUpdateRequest.getSelling_price()* offerItemUpdateRequest.getQuantity()*(1+offerItemUpdateRequest.getTax()/100));

                offerItemRepository.save(offerItem);

                OfferItemResponse offerItemResponse=offerItemMapper.offerItemToOfferItemResponse(offerItem);
                offerItemResponse.setDiscount(offerItem.getOffer().getDiscount());

                offerItemResponse.setSku(offerItem.getSku());
                offerItemResponse.setSubtotal(offerItem.getSub_total());

                return offerItemResponse;

            } else {

                throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
            }
        }

        return null;

    }

    public Page<OfferResponseWithUser> getOffers(String q, Pageable pageable, LocalDateTime startingDate, LocalDateTime endingDate) {
        User user = userService.getCurrentUser();

        Set<Role> roleControl = user.getRoles();
        for(Role r:roleControl)
        {
            Page<Offer> offerPage = offerRepository.findFilteredOffers(q,pageable);

            List<Offer> offerLists = offerPage.getContent().stream().filter(offer -> (startingDate.isBefore(offer.getCreateAt()) && endingDate.isAfter(offer.getCreateAt()))).collect(Collectors.toList());

            Page<Offer> offerPages = new PageImpl<>(offerLists);

            Page<OfferResponseWithUser> offerResponseWithUserPage = offerPages.map(offer -> {
                OfferResponseWithUser offerResponse=offerMapper.offerToOfferResponsewithUser(offer);
                offerResponse.setUserResponse(userMapper.userToUserResponse(offer.getUser()));
                offerResponse.setCurrencyResponse(currencyMapper.currencyToCurrencyResponse(offer.getCurrency()));
                return offerResponse;
            });
            if (r.getType().equals(RoleType.ROLE_ADMIN)) {

                return offerResponseWithUserPage;

            } else if (r.getType().equals(RoleType.ROLE_SALES_MANAGER)) {
                List<OfferResponseWithUser> offerResponseWithUserList= offerResponseWithUserPage.stream().filter(offer -> offer.getStatus()==1).collect(Collectors.toList());
                return new PageImpl<>(offerResponseWithUserList);
            } else if (r.getType().equals(RoleType.ROLE_SALES_SPECIALIST)) {
                List<OfferResponseWithUser> offerResponseWithUserList1= offerResponseWithUserPage.stream().filter(offer -> offer.getStatus()==0).collect(Collectors.toList());
                return new PageImpl<>(offerResponseWithUserList1);
            } else {
                throw new BadRequestException(ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE);
            }
        }
        return null;
    }
}