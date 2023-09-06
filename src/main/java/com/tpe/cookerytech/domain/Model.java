package com.tpe.cookerytech.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name="t_model")
public class Model {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    @Size(min = 5, max = 150, message = "Title must be between 5 and 150 characters")
    private String title;

    @Column(nullable = false)
    private String sku;

    @Column(nullable = false)
    private int stock_amount;

    @Column(nullable = false)
    private int in_box_quantity=1;

    @Column(nullable = false)
    private int seq=0;

    @OneToMany(orphanRemoval = true)
    @JoinColumn(name="model_id")
    private Set<ImageFile> image;

    @Column(nullable = false)
    private double buying_price; //decimal

    @Column(nullable = false)
    private double tax_rate=0; //decimal

    @Column(nullable = false)
    private Boolean isActive=true;

    @Column(nullable = false)
    private Boolean built_in=false;  //default 0 diyor

    @OneToOne
    @JoinColumn(name = "currency_id", referencedColumnName = "id")
    private Currency currency;

    @ManyToOne
    @JoinColumn(name = "product_id",referencedColumnName = "id")
    private Product product;

    @Column(nullable = false)
    private LocalDateTime create_at;

    @Column(nullable = true)
    private LocalDateTime update_at;


}
