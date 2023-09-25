package com.tpe.cookerytech.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetRequestPassword {

    @NotNull
    private String resetPasswordCode;

    @Size(min = 8,max = 20,message = "Please provide Correct Size of Password")
    @NotBlank(message = "Please provide your password")
    private String newPassword;

}
