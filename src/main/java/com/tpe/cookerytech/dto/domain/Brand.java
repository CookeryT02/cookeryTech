package com.tpe.cookerytech.dto.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "t_brand")
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", nullable = false)
    @Size(min = 4, max = 70)
    private String name;

    @Column(name = "profit_Rate", nullable = false)
    private double profitRate = 0;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "builtIn", nullable = false)
    private Boolean builtIn = false;

    @Column(name = "create_at", nullable = false)
    private LocalDateTime createAt;

    private LocalDateTime updateAt;


}
