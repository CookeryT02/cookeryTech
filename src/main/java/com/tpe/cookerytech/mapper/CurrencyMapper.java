package com.tpe.cookerytech.mapper;

import com.tpe.cookerytech.domain.Currency;
import com.tpe.cookerytech.dto.response.CurrencyResponse;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CurrencyMapper {

    default List<CurrencyResponse> currenciesToCurrencyResponseList(List<Currency> currencies){
        List<CurrencyResponse> currencyResponseList = new ArrayList<>();
        for (Currency currency : currencies){
            CurrencyResponse currencyResponse = currencyToCurrencyResponse(currency);
            currencyResponseList.add(currencyResponse);
            currency.setUpdateAt(LocalDateTime.now());
        }
        return currencyResponseList;
    }

    CurrencyResponse currencyToCurrencyResponse(Currency currency);
}
