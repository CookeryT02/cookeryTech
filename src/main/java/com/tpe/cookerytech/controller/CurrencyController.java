package com.tpe.cookerytech.controller;

import com.tpe.cookerytech.dto.response.CurrencyResponse;
import com.tpe.cookerytech.service.CurrencyService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;



@RestController
@RequestMapping("/currencies")
public class CurrencyController {
    private final CurrencyService currencyService;

    public CurrencyController(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }



    //L01 -> It will getgetcurrent currency rates for TRY, USD, EUR from currencies table    Page:96
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST') or hasRole('PRODUCT_MANAGER') or hasRole('CUSTOMER')")
    public ResponseEntity<List<CurrencyResponse>> getAllCurrencies() {
        List<CurrencyResponse> allCurrencies = currencyService.getAllCurrencies();
        return ResponseEntity.ok(allCurrencies);
    }




    //L02 -> It will getgetcurrent currency rates for TRY, USD, EUR from
    // https://www.tcmb.gov.tr/kurlar/today.xml and update currencies table
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<List<CurrencyResponse>> getAllCurrenciesAdmin() {
        List<CurrencyResponse> allCurrencies = currencyService.getAllCurrenciesAdmin();
        return ResponseEntity.ok(allCurrencies);
    }
}
