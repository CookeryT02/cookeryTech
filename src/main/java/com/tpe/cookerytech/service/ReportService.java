package com.tpe.cookerytech.service;

import com.tpe.cookerytech.domain.OfferItem;
import com.tpe.cookerytech.domain.Product;
import com.tpe.cookerytech.domain.User;
import com.tpe.cookerytech.dto.response.ProductResponse;
import com.tpe.cookerytech.dto.response.ReportOfferResponse;
import com.tpe.cookerytech.dto.response.ReportResponse;
import com.tpe.cookerytech.mapper.ProductMapper;
import com.tpe.cookerytech.repository.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
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


    public ReportService( ProductRepository productRepository, BrandRepository brandRepository, OfferRepository offerRepository, CategoryRepository categoryRepository, UserRepository userRepository, ProductService productService, OfferItemRepository offerItemRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
        this.offerRepository = offerRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.offerItemRepository = offerItemRepository;
        this.productService = productService;
        this.productMapper = productMapper;
    }



    //G01
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




    //G02
    public List<ReportOfferResponse> getOffersSummaries(LocalDate startDate, LocalDate endDate, String type) {

        LocalDateTime D1 = startDate.atStartOfDay();
        LocalDateTime D2 = LocalDateTime.of(endDate.getYear(), endDate.getMonth(), endDate.getDayOfMonth(), 23, 59, 59, 999999);

        List<Object[]> allOffersSummaries = offerItemRepository.findAllOffersBetweenD1ToD2(D1, D2, type);

        List<ReportOfferResponse> reportList = new ArrayList<>();

        for (Object[] row : allOffersSummaries) {
            Timestamp timestamp = (Timestamp) row[0];
            LocalDateTime dailyDate = timestamp.toLocalDateTime();
            Long totalProduct = ((BigInteger) row[1]).longValue();
            Double totalAmount = (Double) row[2]; // Değişiklik burada

            // Her döngü adımında yeni bir ReportOfferResponse nesnesi oluşturun ve listeye ekleyin.
            ReportOfferResponse report = new ReportOfferResponse();
            report.setPeriod(dailyDate.toString()); // Varsayılan olarak LocalDateTime'ı string olarak ayarladık.
            report.setTotalProduct(totalProduct);
            report.setTotalAmount(totalAmount);
            reportList.add(report);
        }

        return reportList;
    }





    //G03
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




    //G04
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
}
