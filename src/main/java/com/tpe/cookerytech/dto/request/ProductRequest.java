package com.tpe.cookerytech.dto.request;

import com.tpe.cookerytech.domain.Brand;
import com.tpe.cookerytech.domain.Category;
import com.tpe.cookerytech.domain.ImageFile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {

    private String title;

    private String shortDescription;

    private String longDescription;

    private boolean isFeatured = false;

    private boolean isNew = false;

    private boolean isActive = true;

    private Brand brand;

    private Category category;

    private Set<ImageFile> image;

    private int sequence = 0;


}
