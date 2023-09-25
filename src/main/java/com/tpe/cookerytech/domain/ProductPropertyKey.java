package com.tpe.cookerytech.domain;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;


@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "t_product_property_key")
public class ProductPropertyKey {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    @Size(min = 2, max = 80, message = "Name must be between 2 and 80 characters")
    private String name;


    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_generator")
    @SequenceGenerator(name = "seq_generator", sequenceName = "product_property_seq", allocationSize = 1)
    private int seq;

    private Boolean builtIn = false;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

}