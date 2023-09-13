package com.tpe.cookerytech.controller;

import com.tpe.cookerytech.dto.request.CartRequest;
import com.tpe.cookerytech.dto.request.FavoritesRequest;
import com.tpe.cookerytech.dto.response.ModelResponse;
import com.tpe.cookerytech.dto.response.ShoppingCartItemResponse;
import com.tpe.cookerytech.service.ShoppingCartService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class ShoppingCartController {

    private final ShoppingCartService shoppingCartService;

    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }


    @PostMapping("/auth")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST') or hasRole('PRODUCT_MANAGER') or hasRole('CUSTOMER')")
    public ResponseEntity<ShoppingCartItemResponse> updateAuthUserFavorites(@Valid @RequestBody CartRequest cartRequest){

        ShoppingCartItemResponse shoppingCartItemResponse = shoppingCartService.createUpdateCartItem(cartRequest);

        return ResponseEntity.ok(shoppingCartItemResponse);
    }

}
