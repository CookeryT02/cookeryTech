package com.tpe.cookerytech.controller;

import com.tpe.cookerytech.dto.response.ProductResponse;
import com.tpe.cookerytech.dto.response.ReportResponse;
import com.tpe.cookerytech.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/report")
public class ReportController {


    private final ReportService reportService;


    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST') or hasRole('PRODUCT_MANAGER')")
    public String generateStatistics(){

        return reportService.getReport().toString();
    }


    @GetMapping("/most-popular-products/{amount}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<Map<Long,ProductResponse>> getMostPopularProducts(@PathVariable int amount){

        Map<Long,ProductResponse> listMostPopularProducts=reportService.getReportMostPopularProduct(amount);

        //Set<ProductResponse> keys = listMostPopularProducts.keySet();

        return ResponseEntity.ok(listMostPopularProducts);
    }
}
