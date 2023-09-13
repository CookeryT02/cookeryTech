package com.tpe.cookerytech.service;

import com.tpe.cookerytech.domain.*;
import com.tpe.cookerytech.dto.request.CartRequest;
import com.tpe.cookerytech.dto.response.ShoppingCartItemResponse;
import com.tpe.cookerytech.exception.ResourceNotFoundException;
import com.tpe.cookerytech.exception.message.ErrorMessage;
import com.tpe.cookerytech.repository.ModelRepository;
import com.tpe.cookerytech.repository.ShoppingCartRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShoppingCartService {

    private final ShoppingCartRepository shoppingCartRepository;

    private final ModelRepository modelRepository;

    private final UserService userService;


    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, ModelRepository modelRepository, UserService userService) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.modelRepository = modelRepository;
        this.userService = userService;
    }


    public ShoppingCartItemResponse createUpdateCartItem(CartRequest cartRequest) {


//        User user = userService.getCurrentUser();
//
//        Model model = modelRepository.findById(cartRequest.getModelId()).orElseThrow(()->
//                new ResourceNotFoundException(String.format(ErrorMessage.MODEL_NOT_FOUND_EXCEPTION,cartRequest.getModelId())));
//
////        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(user.getId());
//
//        List<ShoppingCartItem> shoppingCartItems = shoppingCartRepository.findByUserId(user.getId());
//
//        List<Long> shoppingCart = new ArrayList<>();
//        for (Favorites favorites: favoritesListAuthUser){
//            modelIdList.add(favorites.getModel().getId());
//        }
//
//
//        if(!modelRepository.existsById(cartRequest.getModelId())){
//
//
//        }

        return null;

    }
}
