package com.tpe.cookerytech.repository;

import com.tpe.cookerytech.domain.Category;
import com.tpe.cookerytech.dto.response.CategoryResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Locale;

@Repository
public interface CategoryRepository  extends JpaRepository<Category,Long>{

    List<Category> findByIsActive(boolean isActive);
    boolean existsByTitle(String title);

    List<Category> findByIsActive(boolean isActive);
}
