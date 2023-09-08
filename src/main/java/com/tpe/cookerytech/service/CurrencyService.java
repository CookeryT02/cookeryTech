package com.tpe.cookerytech.service;

import com.tpe.cookerytech.domain.Currency;
import com.tpe.cookerytech.dto.response.CurrencyResponse;
import com.tpe.cookerytech.exception.ResourceNotFoundException;
import com.tpe.cookerytech.init.TCMBData;
import com.tpe.cookerytech.mapper.CurrencyMapper;
import com.tpe.cookerytech.repository.CurrencyRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CurrencyService {
    private final CurrencyRepository currencyRepository;

    private final TCMBData tcmbData;

    private final CurrencyMapper currencyMapper;


    public CurrencyService(CurrencyRepository currencyRepository, TCMBData tcmbData, CurrencyMapper currencyMapper) {
        this.currencyRepository = currencyRepository;
        this.tcmbData = tcmbData;
        this.currencyMapper = currencyMapper;
    }

    public List<CurrencyResponse> getCurrenciesTCMB() {
       // double usd = tcmbData.getExchangeRate();

        List<Currency> currencyList = currencyRepository.findAll();
//        currency.setValue(26.7);
//        currency.setUpdateAt(LocalDateTime.now());
//        currencyRepository.save(currency);

        return currencyMapper.currencyListToCurrencyResponseList(currencyList);
    }
}
