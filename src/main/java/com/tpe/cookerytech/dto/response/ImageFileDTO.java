package com.tpe.cookerytech.dto.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageFileDTO {

    private String name ;

    private String url;

    private String type;

    private long size ;
}
