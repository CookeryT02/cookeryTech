package com.tpe.cookerytech.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfferItemResponse {

    private String sku;

    private int quantity;

    private double selling_price;

    private double tax;

    private double discount;

    private double subtotal;

}
