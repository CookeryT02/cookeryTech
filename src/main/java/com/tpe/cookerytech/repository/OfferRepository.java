package com.tpe.cookerytech.repository;

import com.tpe.cookerytech.domain.Offer;
import com.tpe.cookerytech.domain.User;
import com.tpe.cookerytech.dto.response.OfferResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {

    static boolean existByUser(User user) {
        return false;
    }


    @EntityGraph(attributePaths = "id")
    List<Offer> findByUserId(Long id);

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