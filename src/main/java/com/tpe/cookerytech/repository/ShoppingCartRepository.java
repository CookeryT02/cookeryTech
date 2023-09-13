package com.tpe.cookerytech.repository;

import com.tpe.cookerytech.domain.ShoppingCart;
import com.tpe.cookerytech.domain.ShoppingCartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {


    List<ShoppingCartItem> findByUserId(Long id);

//    @EntityGraph(attributePaths = "id")
//    Optional<ShoppingCart> findByUserId(Long id);
}
