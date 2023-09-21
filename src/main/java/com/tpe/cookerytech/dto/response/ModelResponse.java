package com.tpe.cookerytech.dto.response;


import com.tpe.cookerytech.domain.Currency;
import com.tpe.cookerytech.domain.ImageFile;
import com.tpe.cookerytech.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModelResponse {

    private Long id;

    private String title;

    private String sku;

    private int stock_amount;

    private int in_box_quantity=1;

    private int seq=0;


    private Set<ImageFileResponse> image;

    private double buying_price; //decimal

    private double tax_rate=0; //decimal

    private Boolean isActive=true;

    private Boolean built_in=false;  //default 0 diyor

    private Long currencyId;

    private Long productId;

    private LocalDateTime create_at;

    private LocalDateTime update_at;

}
