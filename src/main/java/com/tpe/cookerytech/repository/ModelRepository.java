package com.tpe.cookerytech.repository;

import com.tpe.cookerytech.domain.Model;
import com.tpe.cookerytech.dto.response.ModelResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModelRepository extends JpaRepository <Model, Long> {


    Optional<List<Model>> findByProductId(Long id);

    Model findBySku(String sku);

  

//    List<ModelResponse> findByIsActive(Integer isActive);
//
//
//    @Query("SELECT m FROM Model m WHERE m.isActive = 0")
//    List<ModelResponse> findAllModel();


}
