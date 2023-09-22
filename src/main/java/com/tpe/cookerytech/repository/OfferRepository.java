package com.tpe.cookerytech.repository;

import com.tpe.cookerytech.domain.Offer;
import com.tpe.cookerytech.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;


@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {

    static boolean existByUser(User user) {
        return false;
    }


    @EntityGraph(attributePaths = "id")
    List<Offer> findByUserId(Long id);


    @Query("SELECT o FROM Offer o WHERE (:q IS NULL OR o.code LIKE %:q%) AND " +
            "DATE(o.createAt) BETWEEN :date1 AND :date2 ORDER BY o.createAt")
    Page<Offer> findByCreateAtBetweenOrderByCreateAt(String q, LocalDate date1, LocalDate date2, Pageable pageable);


    @Query("SELECT o FROM Offer o WHERE " +
            "(:status IS NULL OR o.status = :status) AND " +
            "(:id IS NULL OR o.user.id = :id ) AND "  +
            "DATE(o.createAt) BETWEEN :date1 AND :date2 ORDER BY o.createAt")
    Page<Offer> findByUserIdBetweenOrderByCreateAt(Long id, Pageable pageable, Byte status, LocalDate date1, LocalDate date2);

    @Query("SELECT o " +
            "FROM Offer o " +
            "JOIN User u ON o.user.id = u.id " +
            "WHERE " +
            "(:q IS NULL OR u.firstName LIKE CONCAT('%', :q, '%') OR u.lastName LIKE CONCAT('%', :q, '%') OR o.code LIKE CONCAT('%', :q, '%')) ")
    Page<Offer> findFilteredOffers(
            @Param("q") String q,
            Pageable pageable
    );
}