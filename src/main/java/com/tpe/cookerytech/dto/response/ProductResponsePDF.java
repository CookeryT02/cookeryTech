package com.tpe.cookerytech.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponsePDF {


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

//    private Set<ImageFile> image;

    private int sequence = 0;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Long count;

}
