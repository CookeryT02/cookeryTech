package com.tpe.cookerytech.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {


    @Size(min = 2, max = 30)
    @NotNull
    private String firstName;


    @Size(min = 2, max = 30)
    @NotNull
    private String lastName;


    @Size(min = 10, max = 80)
    @Email(message = "Geçerli bir e-posta adresi giriniz")
    private String email;

    @Pattern(regexp = "^((\\(\\d{3}\\))|\\d{3})[- .]?\\d{3}[- .]?\\d{4}$", //(541) 317-8828
            message = "Please provide valid phone number")
    @Size(min=14, max=14)
    @NotBlank(message = "Please provide your phone number")
    private String phone;

    @Size(max = 150)
    @NotNull
    private String address;

    @Size(max = 100)
    @NotNull
    private String city;

    @Size(max = 100)
    @NotNull
    private String country;

    @NotNull
    private LocalDate birthDate;

    private int taxNo;

    @Size(min = 8,max = 20,message = "Please provide Correct Size of Password")
    @NotBlank(message = "Please provide your password")
    private String password;


}
