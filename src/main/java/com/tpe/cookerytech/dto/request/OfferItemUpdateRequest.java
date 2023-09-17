package com.tpe.cookerytech.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfferItemUpdateRequest {


    private int quantity;

    private double selling_price;

    private double tax;

    private double discount;

}
