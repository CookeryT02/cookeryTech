package com.tpe.cookerytech.repository;

import com.tpe.cookerytech.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    List<Product> findByIsActive(boolean isActive);

    List<Product> findByCategoryIdAndIsActiveTrue(Long categoryId);

    @Query("SELECT p FROM Product p WHERE (:q IS NULL OR LOWER(p.title) LIKE CONCAT('%', LOWER(:q), '%')) "
            + "AND (p.isActive = true) ")
    Page<Product> getAllProductsIsActiveTrue( @Param("q") String query, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE (:q IS NULL OR LOWER(p.title) LIKE CONCAT('%', LOWER(:q), '%')) "
            + "AND (p.isActive = false)")
    Page<Product> getAllProductsIsActiveFalse( @Param("q") String query, Pageable pageable);




}
