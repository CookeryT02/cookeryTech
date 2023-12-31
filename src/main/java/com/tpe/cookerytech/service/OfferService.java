package com.tpe.cookerytech.service;

import com.tpe.cookerytech.domain.*;
import com.tpe.cookerytech.domain.Currency;
import com.tpe.cookerytech.domain.enums.LogType;
import com.tpe.cookerytech.domain.enums.RoleType;
import com.tpe.cookerytech.dto.request.OfferCreateRequest;
import com.tpe.cookerytech.dto.request.OfferUpdateRequest;
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
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
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

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final OfferItemMapper offerItemMapper;

    private final LogRepository logRepository;


    public OfferService(UserService userService, OfferRepository offerRepository, OfferMapper offerMapper, CurrencyMapper currencyMapper, CurrencyRepository currencyRepository, ShoppingCartRepository shoppingCartRepository, ShoppingCartItemRepository shoppingCartItemRepository, OfferItemRepository offerItemRepository, UserMapper userMapper, OfferItemMapper offerItemMapper, UserRepository userRepository, LogRepository logRepository) {
        this.userService = userService;
        this.offerRepository = offerRepository;
        this.offerMapper = offerMapper;
        this.currencyMapper = currencyMapper;
        this.currencyRepository = currencyRepository;
        this.shoppingCartRepository = shoppingCartRepository;
        this.shoppingCartItemRepository = shoppingCartItemRepository;
        this.offerItemRepository = offerItemRepository;
        this.userRepository = userRepository;
        this.offerItemMapper = offerItemMapper;
        this.userMapper = userMapper;
        this.logRepository = logRepository;
    }




    //E01
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
                offerResponse.setUserResponse(this.userMapper.userToUserResponse(offer.getUser()));
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



    //E02
    public OfferResponseWithUser getOfferByAdmin ( Long offerId ) {
        User user = userService.getCurrentUser();
        Offer offer = offerRepository.findById( offerId ).orElseThrow( ( ) -> new ResourceNotFoundException( ErrorMessage.OFFER_NOT_FOUND_EXCEPTION ) );
        User offerUser = offer.getUser();

        Set<Role> roleControl = user.getRoles();

        boolean hasRole = false;

        for ( Role role : roleControl ) {
            if ( role.getType().equals( RoleType.ROLE_ADMIN ) || role.getType().equals( RoleType.ROLE_SALES_SPECIALIST ) || role.getType().equals( RoleType.ROLE_SALES_MANAGER ) ) {
                hasRole = true;
                break;
            }
        }
        if ( !hasRole ) {
            throw new BadRequestException( ErrorMessage.NOT_PERMITTED_METHOD_MESSAGE );
        }
        List<OfferItem> offerItems = offerItemRepository.findByOfferId( offerId );
        List<OfferItemResponse> offerItemResponse = offerItems.stream().map( offerItemMapper::offerItemToOfferItemResponse ).collect( Collectors.toList() );

        OfferResponseWithUser offerResponseWithUser = offerMapper.offerToOfferResponsewithUser( offer );

        offerResponseWithUser.setUserResponse( userMapper.userToUserResponse( offerUser ) );
        offerResponseWithUser.setCurrencyResponse( currencyMapper.currencyToCurrencyResponse( offer.getCurrency() ) );
        offerResponseWithUser.setOfferItems( offerItemResponse );

        return offerResponseWithUser;

    }



    //E03
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




    //E04
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




    //E05
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



    //E06
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


        Log log =new Log();
        log.setUser(user);
        log.setOffer(offer);
        log.setCreateAt(LocalDateTime.now());
        log.setLog(LogType.CREATED);

        offerRepository.save(offer);
        logRepository.save(log);
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(user.getId()).orElseThrow(() ->
                new ResourceNotFoundException(ErrorMessage.SHOPPING_CART_NOT_FOUND));
        List<ShoppingCartItem> shoppingCartItemList = shoppingCartItemRepository.findByShoppingCartId(shoppingCart.getId());

        double offersubTotal = 0;

        List<OfferItem> offerItemList = new ArrayList<>();

        for (ShoppingCartItem shoppingCartItem : shoppingCartItemList) {
            OfferItem offerItem = new OfferItem();
            offerItem.setSku(shoppingCartItem.getModel().getSku());
            offerItem.setQuantity(shoppingCartItem.getModel().getInBoxQuantity());
            offerItem.setSelling_price(shoppingCartItem.getModel().getBuyingPrice() + (shoppingCartItem.getModel().getBuyingPrice() *
                    shoppingCartItem.getModel().getProduct().getBrand().getProfitRate()));
            offerItem.setTax(shoppingCartItem.getModel().getTaxRate());
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



    //E07
    public OfferResponseWithUser updateOfferByManagements(Long id, OfferUpdateRequest offerUpdateRequest) {


        User user = userService.getCurrentUser();

        String roleControl = user.getRoles().toString();

        Offer offer = offerRepository.findById(id).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessage.OFFER_NOT_FOUND_EXCEPTION,id)));

        if (roleControl.contains("ROLE_SALES_SPECIALIST") && (offer.getStatus() != 0 && offer.getStatus() != 3)) {
            throw new AccessDeniedException("Sales professionals can only update quotes with status 0 or 3");
        }


        if (roleControl.contains("ROLE_SALES_MANAGER") && offer.getStatus() != 1) {
            throw new AccessDeniedException("Sales managers can only update quotes with status 1");
        }

        Currency currency = currencyRepository.findById(offerUpdateRequest.getCurrencyId()).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessage.CURRENCY_NOT_FOUND_EXCEPTION,id)));

        offer.setDiscount(offerUpdateRequest.getDiscount());
        offer.setStatus(offerUpdateRequest.getStatus());
        offer.setCurrency(currency);
        offer.setGrandTotal(offer.getSubTotal() * (1 - offerUpdateRequest.getDiscount() / 100));
        offer.setUpdateAt(LocalDateTime.now());

        offerRepository.save(offer);

        //offer.getStatus() => 0: Created 1:Waiting for approval 2: Approved 3: Rejected 4: Paid

        Log log =new Log();
        log.setUser(user);
        log.setOffer(offer);
        log.setCreateAt(LocalDateTime.now());
        if(offer.getStatus()==3) {
            log.setLog(LogType.DECLINED);
        }else if(offer.getStatus()==2){
            log.setLog(LogType.APPROVED);
        }else{
            log.setLog(LogType.UPDATED);
        }
        logRepository.save(log);

        OfferResponseWithUser offerResponseWithUser =  offerMapper.offerToOfferResponsewithUser(offer);

        offerResponseWithUser.setUserResponse(this.userMapper.userToUserResponse(user));
        offerResponseWithUser.setCurrencyResponse(currencyMapper.currencyToCurrencyResponse(currency));

        return offerResponseWithUser;
    }


}

