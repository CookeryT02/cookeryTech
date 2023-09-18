package com.tpe.cookerytech.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tpe.cookerytech.domain.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private String address;

    private String city;

    private String country;

    private LocalDate birthDate;

    private int taxNo;

    private byte status;

    @JsonIgnore
    private String passwordHash;

    private String resetPasswordCode;

    private Boolean builtIn;

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    private Set<String> roles;

    public void setRoles(Set<Role> roles) {
        Set<String> roleStr = new HashSet<>();
        roles.forEach(r->{
            roleStr.add(r.getType().getName());
        });

        this.roles=roleStr;

    }

}
