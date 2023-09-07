package com.tpe.cookerytech.service;

import com.tpe.cookerytech.repository.CurrencyRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CurrencyService {
    private final CurrencyRepository currencyRepository;

    private final RestTemplate restTemplate;

    public CurrencyService(CurrencyRepository currencyRepository, RestTemplate restTemplate) {
        this.currencyRepository = currencyRepository;
        this.restTemplate = restTemplate;
    }
}
