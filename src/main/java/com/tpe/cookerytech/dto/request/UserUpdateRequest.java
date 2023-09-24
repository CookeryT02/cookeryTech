package com.tpe.cookerytech.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserUpdateRequest {

    @Size(min = 2, max = 30)
    private String firstName;


    @Size(min = 2, max = 30)
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
    private String address;

    @Size(max = 100)
    private String city;

    @Size(max = 100)
    private String country;

    private LocalDate birthDate;

    private int taxNo;


}
