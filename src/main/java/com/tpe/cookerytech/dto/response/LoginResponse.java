package com.tpe.cookerytech.dto.response;

import lombok.AllArgsConstructor;


import lombok.Data;
import lombok.Getter;

import lombok.NoArgsConstructor;
import lombok.Setter;



@Data

@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String token;


}
