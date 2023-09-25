package com.tpe.cookerytech.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartItemResponse {

    private Long id;

    private Long shoppingCartId;

    private ProductResponse productResponse;

    private ModelResponse modelResponse;

    private double amount;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

}
