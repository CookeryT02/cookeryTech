package com.tpe.cookerytech.dto.response;

import com.tpe.cookerytech.domain.Brand;
import com.tpe.cookerytech.domain.Category;
import com.tpe.cookerytech.domain.ImageFile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {


    private Long id;

    @NotNull(message = "Please provide product title")
    private String title;

    private String shortDescription;

    private String longDescription;

    private Boolean isFeatured = false;

    private Boolean isNew = false;

    private Boolean isActive = true;

    private Long brandId;

    private Long categoryId;

    private ImageFileResponse image;

    private int sequence = 0;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
