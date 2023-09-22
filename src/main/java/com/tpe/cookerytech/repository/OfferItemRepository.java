package com.tpe.cookerytech.repository;

import com.tpe.cookerytech.domain.OfferItem;
import com.tpe.cookerytech.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OfferItemRepository extends JpaRepository<OfferItem,Long> {
    List<OfferItem> findByOfferId( Long id );

    List<OfferItem> findByProductId(Long productId);

    @Query(value = "SELECT oi.product_id, COUNT(oi.id) AS offer_count " +
            "FROM t_offer_item oi " +
            "GROUP BY oi.product_id " +
            "ORDER BY offer_count DESC " +
            "LIMIT :amount", nativeQuery = true)
    List<Object[]> findMostPopularProducts(@Param("amount") int amount);


    @Query(value = "SELECT\n" +
            "    DATE_TRUNC(:type, create_at) AS period,\n" +
            "    COUNT(*) AS total_product,\n" +
            "    SUM(sub_total) AS total_amount\n" +
            "FROM t_offer_item\n" +
            "WHERE create_at BETWEEN :startDate AND :endDate\n" +
            "GROUP BY DATE_TRUNC(:type, create_at), period\n" +
            "ORDER BY period;\n",
            nativeQuery = true)
    List<Object[]> findAllOffersBetweenD1ToD2(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate, @Param("type") String type);


}
