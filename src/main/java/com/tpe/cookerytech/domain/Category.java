package com.tpe.cookerytech.domain;

import com.tpe.cookerytech.dto.response.ProductResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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
    private String title;

    @Column(nullable = false)
    private byte status;

    @Column(nullable = false)
    private String description;

    private Boolean built_in=false;

    @Column(nullable = false)
    private int seq;

    @Column(nullable = false)
    @Size(min = 5, max = 200, message = "Slug mast be between 5 and 200")
    private String slug;

    @Column(nullable = false)
    private  Boolean isActive = true;

    @Column(nullable = false)
    private LocalDateTime createAt;

    private LocalDateTime updateAt;


    public Category(Long id, String title, Boolean built_in) {
        this.id = id;
        this.title = title;
        this.built_in = built_in;
    }

}
