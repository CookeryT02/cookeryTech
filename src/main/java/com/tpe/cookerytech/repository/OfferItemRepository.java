package com.tpe.cookerytech.repository;

import com.tpe.cookerytech.domain.OfferItem;
import com.tpe.cookerytech.dto.response.OfferItemResponse;
import com.tpe.cookerytech.dto.response.ReportOfferResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OfferItemRepository extends JpaRepository<OfferItem,Long> {

    @Query("SELECT o FROM OfferItem o WHERE DATE(o.createAt) BETWEEN :startDate AND :endDate ORDER BY o.createAt")
    List<ReportOfferResponse> findAllOffersBetweenD1ToD2(LocalDate startDate, LocalDate endDate);


//    List<OfferItem> getAllOfferItems();
}
