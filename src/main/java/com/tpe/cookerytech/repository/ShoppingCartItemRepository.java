package com.tpe.cookerytech.repository;

import com.tpe.cookerytech.domain.ShoppingCartItem;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingCartItemRepository extends JpaRepository<ShoppingCartItem,Long> {
    @EntityGraph(attributePaths = "id")
    List<ShoppingCartItem> findByShoppingCartId(Long id);
}
