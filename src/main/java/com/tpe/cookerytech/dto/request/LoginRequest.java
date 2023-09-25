package com.tpe.cookerytech.dto.request;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {

    @Email(message = "Please provide a valid email")
    private String email;

    @NotBlank(message = "Please provide a password")
    private String password;

}

