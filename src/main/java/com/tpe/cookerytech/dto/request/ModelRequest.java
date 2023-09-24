package com.tpe.cookerytech.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModelRequest {


    @Size(min = 5, max = 150, message = "Title must be between 5 and 150 characters")
    private String title;

    @Size(min = 10, max = 100, message = "sku must be between 10 and 100 characters")
    private String sku;

    @NotNull
    private int stock_amount;

    @NotNull
    private int in_box_quantity=1;

    @NotNull
    private int seq=0;

    @NotNull
    private double buying_price; //decimal

    @NotNull
    private double tax_rate=0; //decimal

    @NotNull
    private Boolean isActive=true;

    @NotNull
    private Boolean built_in=false;  //default 0 diyor

    private Long currencyId;

    private Long productId;

}
