package com.tpe.cookerytech.service;

import com.tpe.cookerytech.domain.Currency;
import com.tpe.cookerytech.dto.response.CurrencyResponse;
import com.tpe.cookerytech.utils.TCMBData;
import com.tpe.cookerytech.mapper.CurrencyMapper;
import com.tpe.cookerytech.repository.CurrencyRepository;
import org.springframework.stereotype.Service;
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

    public List<CurrencyResponse> getAllCurrencies() {

        List<Currency> currencies = currencyRepository.findAll();
        return currencyMapper.currenciesToCurrencyResponseList(currencies);
    }

    public List<CurrencyResponse> getAllCurrenciesAdmin() {

        updateCurrencies(currencyRepository.findAll());

        List<Currency> currencies = currencyRepository.findAll();

        return currencyMapper.currenciesToCurrencyResponseList(currencies);
    }

    // *******************************  YARDIMCI METOT ***************************************
    private void updateCurrencies(List<Currency> currencies) {
        for (Currency currency: currencies) {
            currency.setValue(tcmbData.getExchangeRate(currency.getCode()));
            currencyRepository.save(currency);
        }
    }



}
