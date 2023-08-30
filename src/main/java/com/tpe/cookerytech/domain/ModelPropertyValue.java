package com.tpe.cookerytech.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.Size;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name="t_model_property_value")
public class ModelPropertyValue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Size(max = 100, message = "Name must be maximum 100 characters")
    private char value;

    @ManyToOne
    @JoinColumn(name = "model_id",referencedColumnName = "id")
    private Model model;

    @OneToOne
    @JoinColumn(name = "model_property_key_id", referencedColumnName = "id")
    private ModelPropertyKey modelPropertyKey;
}
