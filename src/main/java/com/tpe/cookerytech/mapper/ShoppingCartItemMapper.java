package com.tpe.cookerytech.mapper;

import com.tpe.cookerytech.domain.ShoppingCartItem;
import com.tpe.cookerytech.dto.response.ShoppingCartItemResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShoppingCartItemMapper {
   ShoppingCartItemResponse ShoppingCartItemToShoppingCartItemResponse(ShoppingCartItem shoppingCartItem);
}
