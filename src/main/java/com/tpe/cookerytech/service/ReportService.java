package com.tpe.cookerytech.service;

import com.tpe.cookerytech.domain.*;
import com.tpe.cookerytech.dto.response.OfferResponse;
import com.tpe.cookerytech.dto.response.ReportOfferResponse;
import com.tpe.cookerytech.domain.OfferItem;
import com.tpe.cookerytech.domain.Product;
import com.tpe.cookerytech.domain.User;
import com.tpe.cookerytech.dto.response.ProductResponse;
import com.tpe.cookerytech.dto.response.ReportOfferResponse;
import com.tpe.cookerytech.dto.response.ReportResponse;
import com.tpe.cookerytech.mapper.OfferItemMapper;
import com.tpe.cookerytech.exception.ResourceNotFoundException;
import com.tpe.cookerytech.exception.message.ErrorMessage;
import com.tpe.cookerytech.mapper.ProductMapper;
import com.tpe.cookerytech.repository.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {


    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final OfferRepository offerRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final OfferItemRepository offerItemRepository;
    private final ProductService productService;
    private final ProductMapper productMapper;

    private final OfferItemMapper offerItemMapper;

    private EntityManager entityManager;

    public ReportService( ProductRepository productRepository, BrandRepository brandRepository, OfferRepository offerRepository, CategoryRepository categoryRepository, UserRepository userRepository, ProductService productService, OfferItemRepository offerItemRepository, ProductMapper productMapper, OfferItemMapper offerItemMapper, EntityManager entityManager) {
        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
        this.offerRepository = offerRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.offerItemRepository = offerItemRepository;
        this.productService = productService;
        this.productMapper = productMapper;
        this.offerItemMapper = offerItemMapper;
        this.entityManager = entityManager;
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

    public List<ProductResponse> getUnOfferedProducts() {
        List<Product> productList = productRepository.findAll();

        List<OfferItem> offerItemList = offerItemRepository.findAll();

        List<Product> productHasOfferList = offerItemList.stream()
                .map(OfferItem::getProduct)
                .collect(Collectors.toList());

        List<Product> nonMatchingProducts = productList.stream()
                .filter(product -> !productHasOfferList.contains(product))
                .collect(Collectors.toList());

        return nonMatchingProducts.stream()
                .map(product -> {
                    ProductResponse productResponse = productMapper.productToProductResponse(product);
                    productResponse.setBrandId(product.getBrand().getId());
                    productResponse.setCategoryId(product.getCategory().getId());
                    return productResponse;
                })
                .collect(Collectors.toList());
    }



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


    public List<ReportOfferResponse> getOffersSummaries(LocalDate startDate, LocalDate endDate, String type) {

        List<OfferItem> allOfferItemsList = offerItemRepository.findAll();

        List<ReportOfferResponse> offerResponse = offerItemRepository.findAllOffersBetweenD1ToD2(startDate, endDate, type);

        return offerResponse;

    }


}

