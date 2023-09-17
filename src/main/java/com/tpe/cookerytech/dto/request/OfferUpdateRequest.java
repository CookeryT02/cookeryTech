package com.tpe.cookerytech.dto.request;

import com.tpe.cookerytech.domain.Currency;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OfferUpdateRequest {

    private double discount;

    private byte status;

    private Long currencyId;


}
