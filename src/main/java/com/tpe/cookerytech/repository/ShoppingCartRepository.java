package com.tpe.cookerytech.repository;

import com.tpe.cookerytech.domain.ShoppingCart;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart,Long> {

    @EntityGraph(attributePaths = "id")
    Optional<ShoppingCart> findByUserId(Long id);
}
