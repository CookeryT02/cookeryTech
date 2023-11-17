package com.tpe.cookerytech.dto.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModelGenereteResponse {

    private Long id;

    private String title;

    private String sku;

    private int stock_amount;

    private int in_box_quantity=1;

    private int seq=0;

    private Set<ImageFileResponse> image;

    private double buying_price;

    private double tax_rate=0;

    private Boolean isActive;

    private Boolean built_in;

    private String currencyCode;

    private Long productId;

    private LocalDateTime create_at;

    private LocalDateTime update_at;

    private Map<String,String> properties;

    private Boolean favorite;

    private double cartItemAmount;

}
