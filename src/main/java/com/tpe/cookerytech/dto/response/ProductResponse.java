package com.tpe.cookerytech.dto.response;

import com.tpe.cookerytech.domain.Brand;
import com.tpe.cookerytech.domain.Category;
import com.tpe.cookerytech.domain.ImageFile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {

    private Long id;

    private String title;

    private String shortDescription;

    private String longDescription;

    private String slug;

    private int sequence = 0;

    private boolean isNew = false;

    private boolean isFeatured = false;

    private boolean isActive = true;

    private Set<ImageFile> image;

    private boolean builtIn = false;

    private Brand brand;

    private Category category;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
