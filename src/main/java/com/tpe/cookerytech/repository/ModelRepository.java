package com.tpe.cookerytech.repository;


import com.tpe.cookerytech.domain.Model;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModelRepository extends JpaRepository <Model, Long> {
}
