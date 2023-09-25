package com.tpe.cookerytech.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemUpdateRequest {

    @NotNull
    private Long modelId;

    @NotNull
    private double amount;

}
