package com.tpe.cookerytech.repository;

import com.tpe.cookerytech.domain.Favorites;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface FavoritesRepository  extends JpaRepository<Favorites, Long > {

    @EntityGraph(attributePaths = "id")
    List<Favorites> findByUserId(Long id);


    void deleteByModelId(Long id);
}
