package com.tpe.cookerytech.repository;

import com.tpe.cookerytech.domain.OfferItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferItemRepository extends JpaRepository<OfferItem,Long> {

    @Query(value = "SELECT product_id, COUNT(*) AS offer_count " +
            "FROM OfferItem " +
            "GROUP BY product_id " +
            "ORDER BY offer_count DESC " +
            "LIMIT :amount", nativeQuery = true)
    List<Long> findMostPopularProducts(@Param("amount")int amount);
}
