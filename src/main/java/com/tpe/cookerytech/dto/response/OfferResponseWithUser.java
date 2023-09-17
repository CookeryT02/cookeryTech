package com.tpe.cookerytech.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor


public class OfferResponseWithUser {

    private String code;

    private byte status;

    private double subTotal;

    private double discount;

    private double grandTotal;

    private UserResponse userResponse;

    private CurrencyResponse currencyResponse;

    private LocalDate deliveryAt;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;
}
