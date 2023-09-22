package com.tpe.cookerytech.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportOfferResponse {

    private String period;

    private Long totalProduct;

    private Double totalAmount;

}
