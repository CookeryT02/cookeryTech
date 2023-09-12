package com.tpe.cookerytech.repository;

import com.tpe.cookerytech.domain.Favorites;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoritesRepository extends JpaRepository<Favorites,Long> {
    @EntityGraph(attributePaths = "id")
    List<Favorites> findByUserId(Long id);



}
