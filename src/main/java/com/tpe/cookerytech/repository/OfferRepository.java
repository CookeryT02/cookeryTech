package com.tpe.cookerytech.repository;

import com.tpe.cookerytech.domain.Offer;
import com.tpe.cookerytech.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {

    static boolean existByUser(User user) {
        return false;
    }


    @EntityGraph(attributePaths = "id")
    List<Offer> findByUserId(Long id);

//    @Query("SELECT o FROM Offer o " +
//            "WHERE (:query IS NULL OR o.field1 LIKE %:query% OR o.field2 LIKE %:query%) " +
//            "AND (:date1 IS NULL OR o.loanDate >= :date1) " +
//            "AND (:date2 IS NULL OR o.loanDate <= :date2) " +
//            "AND (:status IS NULL OR o.status = :status)")
//    Page<Offer> findAllAuthOffers(String q, Pageable pageable, LocalDate date1, LocalDate date2);


    @Query("SELECT o FROM Offer o WHERE (:q IS NULL OR o.code LIKE %:q%) AND " +
            "(:date1 IS NULL OR o.createAt >= :date1) AND " +
            "(:date2 IS NULL OR o.createAt <= :date2) " +
            "ORDER BY o.createAt DESC")
    Page<Offer> findAllAuthOffers(String q, Pageable pageable, LocalDate date1, LocalDate date2);


}