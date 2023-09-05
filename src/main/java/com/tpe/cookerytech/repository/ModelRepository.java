package com.tpe.cookerytech.repository;


import com.tpe.cookerytech.domain.Model;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModelRepository extends JpaRepository <Model, Long> {
}
