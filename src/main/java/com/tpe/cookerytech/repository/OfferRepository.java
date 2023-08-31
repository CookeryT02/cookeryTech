package com.tpe.cookerytech.repository;

import com.tpe.cookerytech.domain.Offer;
import com.tpe.cookerytech.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {

    static boolean existByUser(User user) {
        return false;
    }
}