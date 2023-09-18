package com.tpe.cookerytech.service;

import com.tpe.cookerytech.domain.OfferItem;
import com.tpe.cookerytech.domain.Product;
import com.tpe.cookerytech.domain.User;
import com.tpe.cookerytech.dto.response.ProductResponse;
import com.tpe.cookerytech.dto.response.ReportResponse;
import com.tpe.cookerytech.mapper.ProductMapper;
import com.tpe.cookerytech.repository.*;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final OfferRepository offerRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final OfferItemRepository offerItemRepository;
    private final ProductService productService;
    private final ProductMapper productMapper;
    public ReportService(ReportRepository reportRepository, ProductRepository productRepository, BrandRepository brandRepository, OfferRepository offerRepository, CategoryRepository categoryRepository, UserRepository userRepository, OfferItemRepository offerItemRepository, ProductService productService, ProductMapper productMapper) {

        this.reportRepository = reportRepository;
        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
        this.offerRepository = offerRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.offerItemRepository = offerItemRepository;
        this.productService = productService;
        this.productMapper = productMapper;
    }

    public ReportResponse getReport() {
        ReportResponse reportResponse=new ReportResponse();
        reportResponse.setCategories((int)categoryRepository.count());
        reportResponse.setBrands((int)brandRepository.count());
        reportResponse.setProducts((int) productRepository.count());
        reportResponse.setOffers((int) offerRepository.count());
        List<User> users = userRepository.findAll();
        users.stream().filter(t->t.getRoles().equals("Customer")).collect(Collectors.toList());
        reportResponse.setCustomers(users.size());
        return reportResponse ;
    }

//    public List<ProductResponse> getReportMostPopularProduct(int amount) {
//
//
//            // Tüm teklif öğelerini al
//            List<OfferItem> offerItems = offerItemRepository.findAll();
//
//            // Ürünlerin kimliklerini ve teklif sayılarını bir haritada saklayın
//            Map<Long, Long> productOfferCounts = offerItems.stream()
//                    .collect(Collectors.groupingBy(
//                            offerItem -> offerItem.getProduct().getId(),
//                            Collectors.counting()
//                    ));
//
//            // Haritayı sıralayın ve en fazla teklif alınan ürünleri alın
//            List<Long> popularProductIds = productOfferCounts.entrySet().stream()
//                    .sorted((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()))
//                    .limit(amount)
//                    .map(Map.Entry::getKey)
//                    .collect(Collectors.toList());
//
//            // En fazla teklif alınan ürün kimliklerini kullanarak ürünleri getirin
//            List<ProductResponse> popularProducts = popularProductIds.stream()
//                    .map(productService::getProductById)
//                    .collect(Collectors.toList());
//
//            return popularProducts;
//    }

    public Map<Long,ProductResponse> getReportMostPopularProduct(int amount) {
        List<Object[]> popularProductData = offerItemRepository.findMostPopularProducts(amount);

        List<ProductResponse> popularProducts = popularProductData.stream()
                .map(rowData -> ((BigInteger) rowData[0]).longValue()) // BigInteger'ı Long'a dönüştürme
                .map(productService::getProductById)
                .collect(Collectors.toList());
        List<Long> offerCount=popularProductData.stream()
                .map(rowData -> ((BigInteger) rowData[1]).longValue())
                .collect(Collectors.toList());
        Comparator<Long> reverseOrder = Collections.reverseOrder();
        Map<Long,ProductResponse> productAndCount= new TreeMap<>(reverseOrder);
        int i=0;
        do{
            productAndCount.put(offerCount.get(i),popularProducts.get(i));
            i++;
        }while(offerCount.size()>i);

        return productAndCount;
    }

}
