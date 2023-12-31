package com.tpe.cookerytech.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor


public class OfferResponse {

    private String code;

    private Byte status;

    private double subTotal;

    private double discount;

    private double grandTotal;

    private Long userId;

    private CurrencyResponse currencyResponse;

    private LocalDate deliveryAt;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;
}
