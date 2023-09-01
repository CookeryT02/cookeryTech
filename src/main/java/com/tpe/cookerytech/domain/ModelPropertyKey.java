package com.tpe.cookerytech.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name="t_model_property_key")
public class ModelPropertyKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(min = 2, max = 80, message = "Name must be between 2 and 80 characters")
    private char name;

    private int seq;

    @Column(nullable = false)
    private int stock_amount;

    @Column(nullable = false)
    private int in_box_quantity=1;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @Column(nullable = false)
    LocalDateTime create_at;

    @Column(nullable =true)
    LocalDateTime update_at;



}
