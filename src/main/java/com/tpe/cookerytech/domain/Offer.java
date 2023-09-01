package com.tpe.cookerytech.domain;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "t_offer")
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false,length = 8,unique = true)
    private String code;

    @Column(nullable = false)
    private byte status=0;

    private double sub_total;

    private double discount;

    private double grand_total;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "currency_id", referencedColumnName = "id")
    private Currency currency;

    private LocalDateTime delivery_at;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "")
    private LocalDateTime create_at;

    @Column(nullable = false)
    private LocalDateTime update_at;

}
