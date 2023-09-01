package com.tpe.cookerytech.dto.domain;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "t_offerItem")
public class OfferItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    @Size(min = 10,max = 100)
    private String sku;

    private int quantity;

    private double selling_price;
    private double tax;

    private double sub_total=selling_price*quantity*(1+tax/100);

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "offer_id", referencedColumnName = "id")
    private Offer offer;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "")
    private LocalDateTime create_at;

    @Column(nullable = false)
    private LocalDateTime update_at;

}
