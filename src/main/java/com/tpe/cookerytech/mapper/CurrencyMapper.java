package com.tpe.cookerytech.mapper;

import com.tpe.cookerytech.domain.Currency;
import com.tpe.cookerytech.dto.response.CurrencyResponse;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {
    CurrencyResponse currencyToCurrencyResponse(Currency currency);


    List<CurrencyResponse> currencyListToCurrencyResponseList(List<Currency> currencyList);
}
