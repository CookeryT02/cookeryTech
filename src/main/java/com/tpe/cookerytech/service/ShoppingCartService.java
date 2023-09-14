package com.tpe.cookerytech.service;

import com.tpe.cookerytech.domain.*;
import com.tpe.cookerytech.dto.request.CartItemUpdateRequest;
import com.tpe.cookerytech.dto.response.ModelResponse;
import com.tpe.cookerytech.dto.response.ProductResponse;
import com.tpe.cookerytech.dto.response.ShoppingCartItemResponse;
import com.tpe.cookerytech.exception.ResourceNotFoundException;
import com.tpe.cookerytech.exception.message.ErrorMessage;
import com.tpe.cookerytech.mapper.ModelMapper;
import com.tpe.cookerytech.mapper.ProductMapper;
import com.tpe.cookerytech.mapper.ShoppingCartItemMapper;
import com.tpe.cookerytech.repository.ModelRepository;
import com.tpe.cookerytech.repository.ProductRepository;
import com.tpe.cookerytech.repository.ShoppingCartItemRepository;
import com.tpe.cookerytech.repository.ShoppingCartRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    private final ModelRepository modelRepository;
    private final ProductRepository productRepository;

    public ShoppingCartService(ShoppingCartRepository shoppingCartRepository, UserService userService, ShoppingCartItemRepository shoppingCartItemRepository, ShoppingCartItemMapper shoppingCartItemMapper, ProductMapper productMapper, ModelMapper modelMapper, ModelRepository modelRepository, ProductRepository productRepository) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.userService = userService;
        this.shoppingCartItemRepository = shoppingCartItemRepository;
        this.shoppingCartItemMapper = shoppingCartItemMapper;
        this.productMapper = productMapper;
        this.modelMapper = modelMapper;
        this.modelRepository = modelRepository;
        this.productRepository = productRepository;
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


    public ShoppingCartItemResponse updateCartItems(CartItemUpdateRequest cartItemUpdateRequest) {

        User user = userService.getCurrentUser();
        Model model= modelRepository.findById(cartItemUpdateRequest.getModelId()).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessage.MODEL_NOT_FOUND_EXCEPTION, cartItemUpdateRequest.getModelId())));

        if(shoppingCartRepository.findByUserId(user.getId()).isEmpty()){
            ShoppingCart shoppingCart=new ShoppingCart();
            shoppingCart.setCreateAt(LocalDateTime.now());
            shoppingCart.setUser(user);
            shoppingCartRepository.save(shoppingCart);

            ShoppingCartItem shoppingCartItem=new ShoppingCartItem();
            shoppingCartItem.setShoppingCart(shoppingCart);
            shoppingCartItemRepository.save(shoppingCartItem);
        }


        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(user.getId()).orElseThrow(() ->
                new ResourceNotFoundException(ErrorMessage.SHOPPING_CART_ITEMS_NOT_FOUND));

        List<ShoppingCartItem> shoppingCartItemList = shoppingCartItemRepository.findByShoppingCartId(shoppingCart.getId());

        List<Long> modelIdList = new ArrayList<>();
        for (ShoppingCartItem shoppingCartItem : shoppingCartItemList) {
            modelIdList.add(shoppingCartItem.getModel().getId());
        }

        if (modelIdList.contains(cartItemUpdateRequest.getModelId()) && cartItemUpdateRequest.getAmount()==0) {
            for (ShoppingCartItem shoppingCartItem : shoppingCartItemList) {
                if (shoppingCartItem.getModel().equals(model)) {


                    ShoppingCartItemResponse shoppingCartItemResponse = shoppingCartItemMapper.ShoppingCartItemToShoppingCartItemResponse(shoppingCartItem);

                    ProductResponse productResponse=  productMapper.productToProductResponse(shoppingCartItem.getProduct());
                    productResponse.setBrandId(model.getProduct().getBrand().getId());
                    productResponse.setCategoryId(model.getProduct().getCategory().getId());

                    shoppingCartItemResponse.setProductResponse(productResponse);

                    ModelResponse modelResponse=modelMapper.modelToModelResponse(shoppingCartItem.getModel());
                    modelResponse.setProductId(model.getProduct().getId());
                    modelResponse.setCurrencyId(model.getCurrency().getId());
                    shoppingCartItemResponse.setModelResponse(modelResponse);

                    shoppingCartItemResponse.setShoppingCartId(shoppingCart.getId());
                    shoppingCartItemResponse.setAmount(cartItemUpdateRequest.getAmount());

                    shoppingCartItemList.remove(shoppingCartItem);
                    shoppingCartItemRepository.delete(shoppingCartItem);
                    return shoppingCartItemResponse;
                }
            }
        } else if(modelIdList.contains(cartItemUpdateRequest.getModelId()) ) {
            for (ShoppingCartItem shoppingCartItem : shoppingCartItemList) {
                if (shoppingCartItem.getModel().equals(model)) {
                    shoppingCartItem.setShoppingCart(shoppingCart);
                    shoppingCartItem.setModel(model);
                    shoppingCartItem.setCreateAt(shoppingCartItem.getCreateAt());
                    shoppingCartItem.setUpdateAt(LocalDateTime.now());
                    shoppingCartItem.setAmount(cartItemUpdateRequest.getAmount());
                    shoppingCartItem.setProduct(model.getProduct());
                    shoppingCartItemRepository.save(shoppingCartItem);

                    ShoppingCartItemResponse shoppingCartItemResponse = shoppingCartItemMapper.ShoppingCartItemToShoppingCartItemResponse(shoppingCartItem);

                    ProductResponse productResponse=  productMapper.productToProductResponse(shoppingCartItem.getProduct());
                    productResponse.setBrandId(model.getProduct().getBrand().getId());
                    productResponse.setCategoryId(model.getProduct().getCategory().getId());

                    shoppingCartItemResponse.setProductResponse(productResponse);

                    ModelResponse modelResponse=modelMapper.modelToModelResponse(shoppingCartItem.getModel());
                    modelResponse.setProductId(model.getProduct().getId());
                    modelResponse.setCurrencyId(model.getCurrency().getId());
                    shoppingCartItemResponse.setModelResponse(modelResponse);

                    shoppingCartItemResponse.setShoppingCartId(shoppingCart.getId());
                    shoppingCartItemResponse.setAmount(cartItemUpdateRequest.getAmount());
                    return shoppingCartItemResponse;
                }
            }
        } else {
            ShoppingCartItem shoppingCartItem=new ShoppingCartItem();
            shoppingCartItem.setShoppingCart(shoppingCart);
            shoppingCartItem.setModel(model);
            shoppingCartItem.setCreateAt(LocalDateTime.now());
            shoppingCartItem.setAmount(cartItemUpdateRequest.getAmount());
            shoppingCartItem.setProduct(model.getProduct());
            shoppingCartItemRepository.save(shoppingCartItem);

            ShoppingCartItemResponse shoppingCartItemResponse = shoppingCartItemMapper.ShoppingCartItemToShoppingCartItemResponse(shoppingCartItem);

            ProductResponse productResponse=  productMapper.productToProductResponse(shoppingCartItem.getProduct());
            productResponse.setBrandId(model.getProduct().getBrand().getId());
            productResponse.setCategoryId(model.getProduct().getCategory().getId());
            shoppingCartItemResponse.setProductResponse(productResponse);

            ModelResponse modelResponse=modelMapper.modelToModelResponse(shoppingCartItem.getModel());
            modelResponse.setProductId(model.getProduct().getId());
            modelResponse.setCurrencyId(model.getCurrency().getId());
            shoppingCartItemResponse.setModelResponse(modelResponse);
            shoppingCartItemResponse.setShoppingCartId(shoppingCart.getId());
            shoppingCartItemResponse.setAmount(cartItemUpdateRequest.getAmount());
            return shoppingCartItemResponse;
        }
        return null;
    }
}
