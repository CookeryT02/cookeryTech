package com.tpe.cookerytech.controller;

import com.tpe.cookerytech.dto.response.*;
import com.tpe.cookerytech.dto.response.ReportOfferResponse;
import com.tpe.cookerytech.dto.response.ReportResponse;
import com.lowagie.text.DocumentException;
import com.tpe.cookerytech.dto.response.ProductResponse;
import com.tpe.cookerytech.utils.PDFGenerator;
import com.tpe.cookerytech.service.ReportService;
import com.tpe.cookerytech.utils.PDFGeneratorReportSummery;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/report")
public class ReportController {

    private final ReportService reportService;


    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }



    //G01 -> It will get some statistics        Page:80
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST') or hasRole('PRODUCT_MANAGER')")
    public String generateStatistics(){

        return reportService.getReport().toString();
    }



    //G02 -> It will get reports
    @GetMapping("/offers")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<List<ReportOfferResponse>> getOffersSummaries(
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam("type") String type) {

        return ResponseEntity.ok(reportService.getOffersSummaries(startDate, endDate, type));
    }

    //G02
    @GetMapping("/pdf/offerSummery")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<List<ReportOfferResponse>> generateOfferSummeryPdf(HttpServletResponse response,
                                                                             @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                                                             @RequestParam("endDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
                                                                             @RequestParam("type") String type) throws DocumentException, IOException {

        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD:HH:MM:SS");
        String currentDateTime = dateFormat.format(new Date());
        String headerkey = "Content-Disposition";
        String headervalue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
        response.setHeader(headerkey, headervalue);

        List<ReportOfferResponse> reportOfferResponseList = reportService.getOffersSummaries(startDate, endDate, type);

        PDFGeneratorReportSummery pdfGeneratorReportSummery = new PDFGeneratorReportSummery();
        pdfGeneratorReportSummery.setReportOfferResponseList(reportOfferResponseList);
        int count =0;
        String title = "List of Offer Summery";
        pdfGeneratorReportSummery.generate(response,title,count);

        return ResponseEntity.ok(reportOfferResponseList);
    }




    //G03 -> It will get reports -LIST
    @GetMapping("/most-popular-products/{amount}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<Map<Long, ProductResponse>> getMostPopularProducts(@PathVariable int amount){

        Map<Long,ProductResponse> listMostPopularProducts=reportService.getReportMostPopularProduct(amount);

        //Set<ProductResponse> keys = listMostPopularProducts.keySet();

        return ResponseEntity.ok(listMostPopularProducts);
    }




    //G03 -> PDF
    @GetMapping("/pdf/products/mostPopular/{amount}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<Map<Long,ProductResponse>> generatePdfMostPopularOffers(@PathVariable int amount,HttpServletResponse response) throws DocumentException, IOException {

        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD:HH:MM:SS");
        String currentDateTime = dateFormat.format(new Date());
        String headerkey = "Content-Disposition";
        String headervalue = "attachment; filename=pdf_" + currentDateTime + ".pdf";
        response.setHeader(headerkey, headervalue);

        Map<Long, ProductResponse> listMostPopularProducts = reportService.getReportMostPopularProduct(amount);

        List<Long> idList = listMostPopularProducts.keySet().stream().collect(Collectors.toList());
        List<ProductResponse> productResponseList = listMostPopularProducts.values().stream().collect(Collectors.toList());

        List<ProductResponsePDF> productResponsePDFS = new ArrayList<>();
        int i =0;
        for (ProductResponse productResponse:productResponseList){
            ProductResponsePDF productResponsePDF = new ProductResponsePDF();
            productResponsePDF.setId(productResponse.getId());
            productResponsePDF.setTitle(productResponse.getTitle());
            productResponsePDF.setBrandId(productResponse.getBrandId());
            productResponsePDF.setCategoryId(productResponse.getCategoryId());
            productResponsePDF.setCount(idList.get(i));
            i++;
            productResponsePDFS.add(productResponsePDF);
        }

        PDFGenerator generator = new PDFGenerator();
        generator.setProductResponsePDFS(productResponsePDFS);
        int count =0;
        String title = "List Of Most Popular Products";
        int a =2;
        generator.generate(response,title,count,a);

        return ResponseEntity.ok(listMostPopularProducts);
    }




    //G04 -> It will get reports - LIST
    @GetMapping("/unoffered-products")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<List<ProductResponse>> getUnOfferedProducts(){

       List<ProductResponse> productResponseList = reportService.getUnOfferedProducts();

       return ResponseEntity.ok(productResponseList);
    }


//    @GetMapping("/offers")
//    @PreAuthorize("hasRole('ADMIN')")
//    public List<ReportOfferResponse> getOffersSummaries(
//            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
//            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
//            @RequestParam String type) {
//
//        return reportService.getOffersSummaries(startDate, endDate, type);
//
//
//    }


    //G04 - PDF
    @GetMapping("/pdf/products")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<List<ProductResponse>> generatePdf(HttpServletResponse response) throws DocumentException, IOException {

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
        int a=1;
        generator.generate(response,title,count,a);

        return ResponseEntity.ok(productResponseList);
    }
}
