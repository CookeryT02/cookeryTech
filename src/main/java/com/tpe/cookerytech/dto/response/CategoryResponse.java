package com.tpe.cookerytech.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {

    private Long id;

    private String title;

    private byte status;

    private String description;

    private Boolean built_in;

    private int seq;

    private String slag;

    private  Boolean isActive = true;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;


}
