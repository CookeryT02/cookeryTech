package com.tpe.cookerytech.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "t_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    @Size(min = 2, max = 30)
    private String firstName;

    @Column(nullable = false)
    @Size(min = 2, max = 30)
    private String lastName;

    @Column(nullable = false, unique = true)
    @Size(min = 10, max = 80)
    @Email(message = "Geçerli bir e-posta adresi giriniz")
    private String email;

    @Column(length = 14,nullable = false)
    private String phone;

    @Column(length = 150,nullable = false)
    private String address;

    @Column(length = 100,nullable = false)
    private String city;

    @Column(length = 100,nullable = false)
    private String country;

    @Column(nullable = false)
    private LocalDate birthDate;


    private int taxNo;

    private byte status;

    @Column(nullable = false)
    private String passwordHash;

    private String resetPasswordCode;

    @Column(nullable = false)
    private Boolean builtIn=false;


    // Date Pattern eklenecek!!!
    @Column(name = "create_at", nullable = false)
    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    @ManyToMany
    @JoinTable(name = "t_user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

}