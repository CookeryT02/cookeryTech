package com.tpe.cookerytech.repository;

import com.tpe.cookerytech.domain.ProductPropertyKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductPropertyKeyRepository extends JpaRepository<ProductPropertyKey, Long>{

    List<ProductPropertyKey> findByProductId(Long product_id);

  //  @Query("CREATE SEQUENCE my_sequence START 1000 INCREMENT 10 MAXVALUE 9999;")

}
