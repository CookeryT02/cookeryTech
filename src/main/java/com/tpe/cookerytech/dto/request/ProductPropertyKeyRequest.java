package com.tpe.cookerytech.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductPropertyKeyRequest {

    private String name;

    private int seq;

    private Long productId;

}