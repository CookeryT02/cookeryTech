package com.tpe.cookerytech.domain;

import com.tpe.cookerytech.domain.enums.LogType;
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

    @Enumerated(EnumType.STRING)
    private LogType log;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "offer_id", referencedColumnName = "id")
    private Offer offer;

    @Column(nullable = false)
    private LocalDateTime createAt;


}
