package com.tpe.cookerytech.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminUserUpdateRequest extends UserUpdateRequest{

    @NotNull
    @Max(2)
    @Min(0)
    private byte status=0;

    @NotNull
    private Boolean builtIn = false;

    private String password;

    private Set<String> roles;


}