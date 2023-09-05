package com.tpe.cookerytech.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductPropertyKeyResponse {

    private Long id;

    @Min(2)
    @Max(80)
    private String name;

    private int seq;

    private Boolean builtIn= false;

    private Long productId;



}