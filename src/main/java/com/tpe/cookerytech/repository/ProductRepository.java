package com.tpe.cookerytech.repository;

import com.tpe.cookerytech.domain.Product;
import com.tpe.cookerytech.dto.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.tpe.cookerytech.domain.ProductPropertyKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    List<Product> findByIsActive(boolean isActive);

    List<Product> findByCategoryIdAndIsActiveTrue(Long categoryId);

    //    @Query("SELECT p FROM Product p WHERE (LOWER(p.title) LIKE CONCAT('%', LOWER(:q), '%') OR "
//            + "LOWER(p.shortDescription) LIKE CONCAT('%', LOWER(:q), '%') OR "
//            + "LOWER(p.longDescription) LIKE CONCAT('%', LOWER(:q), '%')) AND "
//            + "(:cId IS NULL OR p.category.id = :cId) AND " + "(:bId IS NULL OR p.brand.id = :bId)")


    @Query("SELECT p FROM Product p WHERE (:q IS NULL OR LOWER(p.title) LIKE CONCAT('%', LOWER(:q), '%')) "
            + "AND (p.isActive = true) "
            + "AND (:cId IS NULL OR p.category.isActive = true AND p.category.id = :cId) "
            + "AND (:bId IS NULL OR p.brand.isActive = true AND p.brand.id = :bId)")
    Page<Product> getAllProductsIsActiveTrue( @Param("q") String query, Pageable pageable, @Param("bId") Long brandId, @Param("cId") Long categoryId );

//    @Query("SELECT p FROM Product p WHERE (LOWER(p.title) LIKE CONCAT('%', LOWER(:q), '%') OR "
//            + "LOWER(p.isActive=false) LIKE CONCAT('%', LOWER(:q), '%') OR  "
//            + "LOWER(p.shortDescription) LIKE CONCAT('%', LOWER(:q), '%') OR "
//            + "LOWER(p.longDescription) LIKE CONCAT('%', LOWER(:q), '%')) AND "
//            + "(:cId IS NULL OR p.category.id = :cId) AND " + "(:bId IS NULL OR p.brand.id = :bId)")
    // selam

    @Query("SELECT p FROM Product p WHERE (:q IS NULL OR LOWER(p.title) LIKE CONCAT('%', LOWER(:q), '%')) "
            + "AND (p.isActive = false) "
            + "AND (:cId IS NULL OR p.category.isActive = false AND p.category.id = :cId) "
            + "AND (:bId IS NULL OR p.brand.isActive = false AND p.brand.id = :bId)")
    Page<Product> getAllProductsIsActiveFalse( @Param("q") String query, Pageable pageable, @Param("bId") Long brandId, @Param("cId") Long categoryId );





    /*
    @Query("SELECT p FROM Product p WHERE (:q IS NULL OR LOWER(p.title) LIKE CONCAT('%', LOWER(:q), '%')) "
        + "AND (:adminUser IS NULL OR p.isActive = true) "
        + "AND (:cId IS NULL OR p.category.isActive = true AND p.category.id = :cId) "
        + "AND (:bId IS NULL OR p.brand.isActive = true AND p.brand.id = :bId)")
Page<Product> getAllProducts(
        @Param("q") String query,
        @Param("adminUser") Boolean adminUser,
        Pageable pageable,
        @Param("bId") Long brandId,
        @Param("cId") Long categoryId);
     */



//    @Query("SELECT p FROM Product p WHERE (:q IS NULL OR LOWER(p.title) LIKE CONCAT('%', LOWER(:q), '%')) "
//            + "AND (:adminUser IS NULL OR p.isActive = true) "
//            + "AND (:cId IS NULL OR p.category.isActive = true AND p.category.id = :cId) "
//            + "AND (:bId IS NULL OR p.brand.isActive = true AND p.brand.id = :bId)")


//    @Query("SELECT p FROM Product p WHERE  p.title LIKE %:q%")
//    Page<Product> findProductsByCriteria(Pageable pageable, String q);


//    @Query("SELECT p FROM Product p WHERE (LOWER(p.title) LIKE CONCAT('%', LOWER(:q), '%') OR "
//            + "LOWER(p.shortDescription) LIKE CONCAT('%', LOWER(:q), '%') OR "
//            + "LOWER(p.longDescription) LIKE CONCAT('%', LOWER(:q), '%')) AND "
//            + "(:cId IS NULL OR p.category.id = :cId) AND " + "(:bId IS NULL OR p.brand.id = :bId)")
//    Page<Product> findProductsByCriteria(Pageable pageable, @Param("q") String query, @Param("bId") Long brandId,
//                                         @Param("cId") Long categoryId);
//
//    @Query("SELECT p FROM Product p WHERE (LOWER(p.title) LIKE CONCAT('%', LOWER(:q), '%') OR "
//            + "LOWER(p.shortDescription) LIKE CONCAT('%', LOWER(:q), '%') OR "
//            + "LOWER(p.longDescription) LIKE CONCAT('%', LOWER(:q), '%')) AND "
//            + "(:cId IS NULL OR p.category.id = :cId) AND " + "(:bId IS NULL OR p.brand.id = :bId)")
//    Page<Product> findProductsByCriteriaForAdmin(Pageable pageable, @Param("q") String query, @Param("bId") Long brandId,
//                                                 @Param("cId") Long categoryId);
//
//
//    @Query("SELECT p FROM Product p WHERE (p.isActive = true) AND " +
//            "(LOWER(p.title) LIKE CONCAT('%', LOWER(:q), '%') OR " +
//            "LOWER(p.shortDescription) LIKE CONCAT('%', LOWER(:q), '%') OR " +
//            "LOWER(p.longDescription) LIKE CONCAT('%', LOWER(:q), '%')) AND " +
//            "(:cId IS NULL OR p.category.id = :cId) AND " +
//            "(:bId IS NULL OR p.brand.id = :bId)")
//    Page<Product> findProductsByCriteriaForCustomer(
//            Pageable pageable,
//            @Param("q") String query,
//            @Param("bId") Long brandId,
//            @Param("cId") Long categoryId);

//    @Query("SELECT p FROM Product p WHERE (LOWER(p.title) LIKE CONCAT('%', LOWER(:q), '%') OR "
//            + "LOWER(p.shortDescription) LIKE CONCAT('%', LOWER(:q), '%') OR "
//            + "LOWER(p.longDescription) LIKE CONCAT('%', LOWER(:q), '%')) AND "
//            + "(:cId IS NULL OR p.category.id = :cId) AND " + "(:bId IS NULL OR p.brand.id = :bId)")

//    @Query("SELECT p FROM Product p" + " WHERE (:q IS NULL OR " + "p.title LIKE %:q% OR "
//            + "p.shortDescription LIKE %:q% OR " + "p.longDescription LIKE %:q% ")
//    @Query("SELECT p FROM Product p WHERE (LOWER(p.title) LIKE CONCAT('%', LOWER(:q), '%') OR "
//            + "LOWER(p.shortDescription) LIKE CONCAT('%', LOWER(:q), '%') OR "
//            + "LOWER(p.longDescription) LIKE CONCAT('%', LOWER(:q), '%')) AND "
//            + "(:cId IS NULL OR p.category.id = :cId) AND " + "(:bId IS NULL OR p.brand.id = :bId)")
//    Page<Product> getAllProducts(@Param("q")  String q, Pageable pageable, Long brandId, Long categoryId);



//    Product findByBrandId(Long id);
    //    Product findByBrandId(Long id);

    // Products A11 - /products/:id/models/get product ın id sine gore model getirme



}
