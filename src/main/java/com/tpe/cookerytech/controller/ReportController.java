package com.tpe.cookerytech.controller;

import com.tpe.cookerytech.dto.response.ReportOfferResponse;
import com.tpe.cookerytech.dto.response.ReportResponse;
import com.tpe.cookerytech.service.ReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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

    @GetMapping("/offers")
    @PreAuthorize("hasRole('ADMIN')")
    public List<ReportOfferResponse> getOffersSummaries(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @PathVariable String type) {

        return reportService.getOffersSummaries(startDate, endDate, type);


    }

}
