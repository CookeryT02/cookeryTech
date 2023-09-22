package com.tpe.cookerytech.dto.response;

import com.tpe.cookerytech.domain.Offer;
import com.tpe.cookerytech.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportOfferResponse {

    private String period;

    private int totalProduct;

    private double totalAmount;

    private Product product;

    private Offer offer;


}
