package com.tpe.cookerytech.domain;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "t_shoppingCart")
public class ShoppingCart {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

//    @DateTimeFormat(pattern = "")
    @Column(nullable = false)
    private LocalDateTime createAt;

}
