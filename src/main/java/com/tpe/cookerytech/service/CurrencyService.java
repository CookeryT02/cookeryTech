package com.tpe.cookerytech.service;

import com.tpe.cookerytech.TCMBapi;
import com.tpe.cookerytech.domain.Currency;
import com.tpe.cookerytech.dto.response.CurrencyResponse;
import com.tpe.cookerytech.mapper.CurrencyMapper;
import com.tpe.cookerytech.repository.CurrencyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CurrencyService {
    private final CurrencyRepository currencyRepository;

    private final CurrencyMapper currencyMapper;

    private final TCMBapi tcmbApi;

    public CurrencyService(CurrencyRepository currencyRepository, CurrencyMapper currencyMapper, TCMBapi tcmbApi) {
        this.currencyRepository = currencyRepository;
        this.currencyMapper = currencyMapper;
        this.tcmbApi = tcmbApi;
    }

    public List<CurrencyResponse> getAllCurrencies() {

        List<Currency> currencies = currencyRepository.findAll();
        List<CurrencyResponse> currencyResponseList =currencies.stream()
                .map(currencyMapper::currencyToCurrencyResponse)
                .collect(Collectors.toList());

        return currencyResponseList;
    }

    public List<CurrencyResponse> getAllCurrenciesAdmin() {

        updateCurrencies(currencyRepository.findAll());

        List<Currency> currencies = currencyRepository.findAll();

        List<CurrencyResponse> currencyResponseList= currencyMapper.currenciesToCurrencyResponseList(currencies);
//        List<CurrencyResponse> currencyResponseList =currencies.stream()
//                .map(currencyMapper::currencyToCurrencyResponse)
//                .collect(Collectors.toList());

        return currencyResponseList;
    }

    // *******************************  YARDIMCI METOT ***************************************
    private void updateCurrencies(List<Currency> currencies) {
        for (Currency currency: currencies) {
            currency.setValue(tcmbApi.getExchangeRate(currency.getCode()));
        currencyRepository.save(currency);
        }
    }


}
