package com.tpe.cookerytech.repository;

import com.tpe.cookerytech.domain.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModelRepository extends JpaRepository <Model, Long> {


    Optional<List<Model>> findByProductId(Long id);

    Model findBySku(String sku);
}
