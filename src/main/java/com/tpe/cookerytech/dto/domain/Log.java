package com.tpe.cookerytech.dto.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "t_log")
public class Log {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String log;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "offer_id", referencedColumnName = "id")
    private Offer offer;

    @Column(nullable = false)
    private LocalDateTime createAt;


}
