package com.tpe.cookerytech.repository;

import com.tpe.cookerytech.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    List<Product> findByIsActive(boolean isActive);

    List<Product> findByCategoryIdAndIsActiveTrue(Long categoryId);



}
