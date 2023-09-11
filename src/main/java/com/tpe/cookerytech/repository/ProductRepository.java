package com.tpe.cookerytech.repository;

import com.tpe.cookerytech.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.tpe.cookerytech.domain.ProductPropertyKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    List<Product> findByIsActive(boolean isActive);

    List<Product> findByCategoryIdAndIsActiveTrue(Long categoryId);

//    @Query("SELECT p FROM Product p WHERE  p.title LIKE %:q%")
//    Page<Product> findProductsByCriteria(Pageable pageable, String q);


    @Query("SELECT p FROM Product p WHERE (LOWER(p.title) LIKE CONCAT('%', LOWER(:q), '%') OR "
            + "LOWER(p.shortDescription) LIKE CONCAT('%', LOWER(:q), '%') OR "
            + "LOWER(p.longDescription) LIKE CONCAT('%', LOWER(:q), '%')) AND "
            + "(:cId IS NULL OR p.category.id = :cId) AND " + "(:bId IS NULL OR p.brand.id = :bId)")
    Page<Product> findProductsByCriteria(Pageable pageable, @Param("q") String query, @Param("bId") Long brandId,
                                         @Param("cId") Long categoryId);

    @Query("SELECT p FROM Product p WHERE (LOWER(p.title) LIKE CONCAT('%', LOWER(:q), '%') OR "
            + "LOWER(p.shortDescription) LIKE CONCAT('%', LOWER(:q), '%') OR "
            + "LOWER(p.longDescription) LIKE CONCAT('%', LOWER(:q), '%')) AND "
            + "(:cId IS NULL OR p.category.id = :cId) AND " + "(:bId IS NULL OR p.brand.id = :bId)")
    Page<Product> findProductsByCriteriaForAdmin(Pageable pageable, @Param("q") String query, @Param("bId") Long brandId,
                                                 @Param("cId") Long categoryId);


    @Query("SELECT p FROM Product p WHERE (p.isActive = true) AND " +
            "(LOWER(p.title) LIKE CONCAT('%', LOWER(:q), '%') OR " +
            "LOWER(p.shortDescription) LIKE CONCAT('%', LOWER(:q), '%') OR " +
            "LOWER(p.longDescription) LIKE CONCAT('%', LOWER(:q), '%')) AND " +
            "(:cId IS NULL OR p.category.id = :cId) AND " +
            "(:bId IS NULL OR p.brand.id = :bId)")
    Page<Product> findProductsByCriteriaForCustomer(
            Pageable pageable,
            @Param("q") String query,
            @Param("bId") Long brandId,
            @Param("cId") Long categoryId);

//    Product findByBrandId(Long id);
 //    Product findByBrandId(Long id);

    // Products A11 - /products/:id/models/get product ın id sine gore model getirme

}
