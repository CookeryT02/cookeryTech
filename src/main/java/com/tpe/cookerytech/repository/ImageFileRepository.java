package com.tpe.cookerytech.repository;


import com.tpe.cookerytech.domain.ImageFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ImageFileRepository extends JpaRepository<ImageFile, String> {

//    List<ImageFile> findAllByModelId(Long id);

    List<ImageFile> findAllByModelId(Long model);

}
