package com.tpe.cookerytech.repository;

import com.tpe.cookerytech.domain.ModelPropertyValue;
import com.tpe.cookerytech.domain.ProductPropertyKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProductPropertyKeyRepository extends JpaRepository<ProductPropertyKey, Long>{

    List<ProductPropertyKey> findByProductId(Long product_id);


    void deleteByProductId(Long id);
}
