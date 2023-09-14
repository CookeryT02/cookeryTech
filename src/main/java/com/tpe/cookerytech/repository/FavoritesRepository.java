package com.tpe.cookerytech.repository;

import com.tpe.cookerytech.domain.Favorites;
import com.tpe.cookerytech.domain.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;

public interface FavoritesRepository  extends JpaRepository<Favorites, Long > {


    Favorites findByUser(User authUser);

    @EntityGraph(attributePaths = "id")
    List<Favorites> findByUserId(Long id);



}
