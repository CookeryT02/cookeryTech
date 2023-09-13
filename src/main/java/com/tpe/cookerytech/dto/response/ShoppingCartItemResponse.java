package com.tpe.cookerytech.dto.response;

import com.tpe.cookerytech.domain.Model;
import com.tpe.cookerytech.domain.Product;
import com.tpe.cookerytech.domain.ShoppingCart;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartItemResponse {

    private Long id;

    private ShoppingCart shoppingCart;

    private ProductResponse productResponse;

    private ModelResponse modelResponse;

    private double amount;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

}
