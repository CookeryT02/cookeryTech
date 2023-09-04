package com.tpe.cookerytech.service;

import com.tpe.cookerytech.controller.CurrencyController;
import com.tpe.cookerytech.repository.CurrencyRepository;
import org.springframework.stereotype.Service;

@Service
public class CurrencyService {
    private final CurrencyRepository currencyRepository;

    public CurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }
}
