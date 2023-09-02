package com.tpe.cookerytech.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {

    @NotNull(message = "Please provide product title")
    private String title;

    private String shortDescription;

    private String longDescription;
    @NotNull(message = "Please provide product featured")
    private boolean isFeatured = false;
    @NotNull(message = "Please provide product isnew")
    private boolean isNew = false;
    @NotNull(message = "Please provide product isActive")
    private boolean isActive = true;
//    @NotNull(message = "Please provide product brand id")
    private Long brandId;
//    @NotNull(message = "Please provide product category id")
    private Long categoryId;

//    private Set<ImageFile> image;

    @NotNull(message = "Please provide product sequence")
    private int sequence = 0;

    private boolean builtIn = false;



}
