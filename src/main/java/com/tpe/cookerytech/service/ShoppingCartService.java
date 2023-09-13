package com.tpe.cookerytech.service;

import com.tpe.cookerytech.domain.ShoppingCart;
import com.tpe.cookerytech.domain.ShoppingCartItem;
import com.tpe.cookerytech.domain.User;
import com.tpe.cookerytech.dto.response.ShoppingCartItemResponse;
import com.tpe.cookerytech.exception.ResourceNotFoundException;
import com.tpe.cookerytech.exception.message.ErrorMessage;
import com.tpe.cookerytech.mapper.ModelMapper;
import com.tpe.cookerytech.mapper.ProductMapper;
import com.tpe.cookerytech.repository.ShoppingCartRepository;
import org.springframework.context.annotation.Lazy;
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

    private final UserService userService;

    private final ShoppingCartItemRepository shoppingCartItemRepository;

    private final ShoppingCartItemMapper shoppingCartItemMapper;

    private final ProductMapper productMapper;

    private final ModelMapper modelMapper;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, UserService userService, ShoppingCartItemRepository shoppingCartItemRepository, ShoppingCartItemMapper shoppingCartItemMapper, ProductMapper productMapper, ModelMapper modelMapper) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.userService = userService;
        this.shoppingCartItemRepository = shoppingCartItemRepository;
        this.shoppingCartItemMapper = shoppingCartItemMapper;
        this.productMapper = productMapper;
        this.modelMapper = modelMapper;
    }


    public List<ShoppingCartItemResponse> getCartItemsAuthUser() {
        User user = userService.getCurrentUser();

        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(user.getId()).orElseThrow(()->
                new ResourceNotFoundException(ErrorMessage.SHOPPING_CART_ITEMS_NOT_FOUND));

        List<ShoppingCartItem> shoppingCartItemList = shoppingCartItemRepository.findByShoppingCartId(shoppingCart.getId());

        List<ShoppingCartItemResponse> shoppingCartItemResponseList = new ArrayList<>();
        for (ShoppingCartItem shoppingCartItem: shoppingCartItemList){
            ShoppingCartItemResponse shoppingCartItemResponse = shoppingCartItemMapper.ShoppingCartItemToShoppingCartItemResponse(shoppingCartItem);
            shoppingCartItemResponse.setProductResponse(productMapper.productToProductResponse(shoppingCartItem.getProduct()));
            shoppingCartItemResponse.setModelResponse(modelMapper.modelToModelResponse(shoppingCartItem.getModel()));
            shoppingCartItemResponse.setShoppingCartId(shoppingCart.getId());

            shoppingCartItemResponseList.add(shoppingCartItemResponse);
        }
        return shoppingCartItemResponseList;

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
