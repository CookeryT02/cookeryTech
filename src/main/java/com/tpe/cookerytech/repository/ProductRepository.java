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


    @Query("SELECT p FROM Product p WHERE " +
            "(LOWER(p.title) LIKE CONCAT('%', LOWER(:q), '%') OR LOWER(p.short_desc) LIKE CONCAT('%', LOWER(:q), '%') OR LOWER(p.long_desc) LIKE CONCAT('%', LOWER(:q), '%')) " +
            "AND (:cId IS NULL OR p.category.id = :cId) " +
            "AND (:bId IS NULL OR p.brand.id = :bId) " )
    Page<Product> findProductsByCriteria( Pageable pageable,
                                          @Param("q") String query, @Param("cId") Long categoryId,
                                          @Param("bId") Long brandId);


//    Product findByBrandId(Long id);


}
