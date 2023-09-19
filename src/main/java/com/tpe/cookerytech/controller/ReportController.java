package com.tpe.cookerytech.controller;

import com.tpe.cookerytech.dto.response.ProductResponse;
import com.tpe.cookerytech.dto.response.ProductResponsePDF;
import com.tpe.cookerytech.dto.response.ReportResponse;
import com.lowagie.text.DocumentException;
import com.tpe.cookerytech.dto.response.ProductResponse;
import com.tpe.cookerytech.utils.PDFGenerator;
import com.tpe.cookerytech.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.*;

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


    @GetMapping("/most-popular-products/{amount}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<Map<Long, ProductResponse>> getMostPopularProducts(@PathVariable int amount){

        Map<Long,ProductResponse> listMostPopularProducts=reportService.getReportMostPopularProduct(amount);

        //Set<ProductResponse> keys = listMostPopularProducts.keySet();

        return ResponseEntity.ok(listMostPopularProducts);
    }

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
}
