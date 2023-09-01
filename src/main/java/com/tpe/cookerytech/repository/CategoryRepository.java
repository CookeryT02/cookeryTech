package com.tpe.cookerytech.repository;

import com.tpe.cookerytech.dto.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository  extends JpaRepository<Category,Long>{

    boolean existsByTitle(String title);

}
