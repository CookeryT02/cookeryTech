package com.tpe.cookerytech.repository;

import com.tpe.cookerytech.domain.OfferItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferItemRepository extends JpaRepository<OfferItem,Long> {
    List<OfferItem> findByOfferId( Long id );
}
