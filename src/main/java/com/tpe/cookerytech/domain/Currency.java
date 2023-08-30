package com.tpe.cookerytech.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "t_currency")
public class Currency {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(length = 10)
    private String code;

    @Column(length = 3)
    private String symbol;

    private double value;

    @Column(nullable = false)
    private LocalDateTime dateTime;


}
