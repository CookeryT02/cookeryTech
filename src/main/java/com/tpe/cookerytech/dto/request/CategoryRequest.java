package com.tpe.cookerytech.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest {


    @Size(min = 2, max = 80, message = "Title must be between 2 and 80 characters")
    private String title;

    private byte status;

    private String description;

    private Boolean built_in=false;

    @NotNull(message = "Please provide category seq")
    private int seq=0;

    @Size(min = 5, max = 200, message = "Slug mast be between 5 and 200")
    //@NotNull(message = "Please provide category slug")
    private String slug;

    private  Boolean isActive = true;



}
