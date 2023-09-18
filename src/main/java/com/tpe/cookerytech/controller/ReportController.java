package com.tpe.cookerytech.controller;

import com.tpe.cookerytech.dto.response.ReportOfferResponse;
import com.tpe.cookerytech.dto.response.ReportResponse;
import com.lowagie.text.DocumentException;
import com.tpe.cookerytech.dto.response.ProductResponse;
import com.tpe.cookerytech.utils.PDFGenerator;
import com.tpe.cookerytech.service.ReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    @GetMapping("/unoffered-products")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<List<ProductResponse>> getUnOfferedProducts(){

       List<ProductResponse> productResponseList = reportService.getUnOfferedProducts();

       return ResponseEntity.ok(productResponseList);
    }

    @GetMapping("/pdf/products")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST') or hasRole('PRODUCT_MANAGER')")
    public void generatePdf(HttpServletResponse response) throws DocumentException, IOException {

        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD:HH:MM:SS");
        String currentDateTime = dateFormat.format(new Date());
        String headerkey = "Content-Disposition";
        String headervalue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
        response.setHeader(headerkey, headervalue);

        List<ProductResponse> productResponseList = reportService.getUnOfferedProducts();

        PDFGenerator generator = new PDFGenerator();
        generator.setProductList(productResponseList);
        int count =0;
        String title = "List Of Unoffered Products";
        generator.generate(response,title,count);

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
