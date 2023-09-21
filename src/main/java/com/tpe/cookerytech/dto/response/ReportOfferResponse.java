package com.tpe.cookerytech.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportOfferResponse {

    private String period;

    private int totalProduct;

    private double totalAmount;

}
