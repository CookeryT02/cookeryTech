package com.tpe.cookerytech.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrandRequest {


    @Size(min = 4, max = 70)
    private String name;

    private double profitRate = 0;

    private Boolean isActive = true;

    private Boolean builtIn = false;


}
