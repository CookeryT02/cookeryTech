package com.tpe.cookerytech.service;

import com.tpe.cookerytech.domain.Currency;
import com.tpe.cookerytech.dto.response.CurrencyResponse;
import com.tpe.cookerytech.exception.BadRequestException;
import com.tpe.cookerytech.exception.message.ErrorMessage;
import com.tpe.cookerytech.utils.TCMBData;
import com.tpe.cookerytech.mapper.CurrencyMapper;
import com.tpe.cookerytech.repository.CurrencyRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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


    //L01
    public List<CurrencyResponse> getAllCurrencies() {
        List<Currency> currencies = currencyRepository.findAll();
        return currencyMapper.currenciesToCurrencyResponseList(currencies);
    }



    //L02
    public List<CurrencyResponse> getAllCurrenciesAdmin() {

        LocalDate lastUpdateDate = LocalDate.from(currencyRepository.findById(2L).orElseThrow(
                () -> new BadRequestException(ErrorMessage.CURRENCY_NOT_FOUND_EXCEPTION)).getUpdateAt());

        if (lastUpdateDate != null && lastUpdateDate.isEqual(LocalDate.now())) {
            throw new BadRequestException(ErrorMessage.SAME_DAY_EXCEPTION);
        }

        updateCurrencies(currencyRepository.findAll());

        List<Currency> currencies = currencyRepository.findAll();

        return currencyMapper.currenciesToCurrencyResponseList(currencies);
    }







    // *******************************  HELPER METHODS ***************************************


    private void updateCurrencies(List<Currency> currencies) {
        for (Currency currency : currencies) {
            currency.setValue(tcmbData.getExchangeRate(currency.getCode()));
            currencyRepository.save(currency);
        }
    }
}