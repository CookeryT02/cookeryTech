package com.tpe.cookerytech.repository;

import com.tpe.cookerytech.domain.ModelPropertyValue;
import com.tpe.cookerytech.domain.ProductPropertyKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ModelPropertyValueRepository extends JpaRepository<ModelPropertyValue,Long> {

    @Query("SELECT mv FROM ModelPropertyValue mv WHERE mv.productPropertyKey = :propertyKey")
    ModelPropertyValue findByProductPropertyKey(ProductPropertyKey propertyKey);
}
