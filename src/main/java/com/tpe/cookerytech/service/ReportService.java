package com.tpe.cookerytech.service;

import com.tpe.cookerytech.domain.User;
import com.tpe.cookerytech.dto.response.ReportResponse;
import com.tpe.cookerytech.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final ReportRepository reportRepository;
    private final ProductRepository productRepository;
    private final BrandRepository brandRepository;
    private final OfferRepository offerRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    public ReportService(ReportRepository reportRepository, ProductRepository productRepository, BrandRepository brandRepository, OfferRepository offerRepository, CategoryRepository categoryRepository, UserRepository userRepository) {

        this.reportRepository = reportRepository;
        this.productRepository = productRepository;
        this.brandRepository = brandRepository;
        this.offerRepository = offerRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
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
}
