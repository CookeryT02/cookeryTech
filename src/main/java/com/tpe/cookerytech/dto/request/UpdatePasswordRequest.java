package com.tpe.cookerytech.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePasswordRequest {

    @Size(min = 8,max = 20,message = "Please provide Correct Size of Password")
    @NotBlank(message = "Please provide your old password")
    private String oldPassword;

    @Size(min = 8,max = 20,message = "Please provide Correct Size of Password")
    @NotBlank(message = "Please provide your new password")
    private String newPassword;
}
