package com.tpe.cookerytech.repository;

import com.tpe.cookerytech.domain.OfferItem;
import com.tpe.cookerytech.dto.response.OfferItemResponse;
import com.tpe.cookerytech.dto.response.ReportOfferResponse;
import com.tpe.cookerytech.domain.Product;
import com.tpe.cookerytech.dto.response.ReportOfferResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface OfferItemRepository extends JpaRepository<OfferItem,Long> {

//    @Query("SELECT o FROM OfferItem o WHERE DATE(o.createAt) BETWEEN :startDate AND :endDate ORDER BY o.createAt")

//    @Query("SELECT period, totalProduct, totalAmount FROM your_table_name WHERE period BETWEEN '2023-09-01' AND '2023-10-01' ORDER BY period;")

    /*SELECT
    period,
    SUM(totalProduct) AS totalProduct,
    SUM(totalAmount) AS totalAmount
FROM
    your_table_name
WHERE
    period BETWEEN '2023-09-01' AND '2023-10-01'
GROUP BY
    period
ORDER BY
    period;*/
//    @Query("SELECT o FROM OfferItem o WHERE DATE(o.createAt) BETWEEN :startDate AND :endDate ORDER BY o.createAt"
//            + "AND "
//    )

//    @Query("SELECT r FROM ReportOffer r WHERE DATE(r.createAt) BETWEEN :startDate AND :endDate ORDER BY r.createAt"
//            + "AND  "
//                    )
    List<ReportOfferResponse> findAllOffersBetweenD1ToD2(LocalDate startDate, LocalDate endDate, String type);


//    List<OfferItem> getAllOfferItems();
    List<OfferItem> findByOfferId( Long id );

    List<OfferItem> findByProductId(Long productId);

    @Query(value = "SELECT oi.product_id, COUNT(oi.id) AS offer_count " +
            "FROM t_offer_item oi " +
            "GROUP BY oi.product_id " +
            "ORDER BY offer_count DESC " +
            "LIMIT :amount", nativeQuery = true)
    List<Object[]> findMostPopularProducts(@Param("amount") int amount);



}
