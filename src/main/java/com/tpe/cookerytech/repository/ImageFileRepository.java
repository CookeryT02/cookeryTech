package com.tpe.cookerytech.repository;

import com.tpe.cookerytech.domain.ImageFile;
import com.tpe.cookerytech.domain.Model;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ImageFileRepository extends JpaRepository<ImageFile,String>{


    @EntityGraph(attributePaths = "id")
    List<ImageFile> findAllByModelId(Long id);

    Set<ImageFile> findAllByModel(Model model);

    Set<ImageFile> findAllByName(String s);
}
