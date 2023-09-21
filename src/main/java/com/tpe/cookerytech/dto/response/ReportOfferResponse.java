package com.tpe.cookerytech.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportOfferResponse {

    private String dailyDate;

    private int totalProduct;

    private double totalAmount;

}
