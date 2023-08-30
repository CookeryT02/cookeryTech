package com.tpe.cookerytech.domain;

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
@Table(name = "t_category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    @Size(min = 2, max = 80, message = "Title must be between 2 and 80 characters")
    private char title;

    @Column(nullable = false)
    private byte status;

    @Column(nullable = false)
    private char description;

    private Boolean built_in;

    @Column(nullable = false)
    private int seq;

    @Column(nullable = false)
    @Size(min = 5, max = 200, message = "Slag mast be between 5 and 200")
    private char slag;

    @Column(nullable = false)
    private  Boolean isActive = true;

    @Column(nullable = false)
    private LocalDateTime create_at;

    private LocalDateTime update_at;






}
