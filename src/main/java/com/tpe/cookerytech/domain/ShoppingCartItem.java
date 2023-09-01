package com.tpe.cookerytech.domain;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "t_shoppingCrtItem")
public class ShoppingCartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false,unique = true)
    private Integer cart_id;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "model_id", referencedColumnName = "id")
    private Model model;

    private double amount;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "")
    private LocalDateTime create_at;

    @Column(nullable = false)
    private LocalDateTime update_at;


}
