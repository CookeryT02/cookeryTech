package com.tpe.cookerytech.controller;

import com.tpe.cookerytech.dto.request.RegisterRequest;
import com.tpe.cookerytech.dto.response.UserResponse;
import com.tpe.cookerytech.dto.request.LoginRequest;
import com.tpe.cookerytech.dto.response.LoginResponse;
import com.tpe.cookerytech.security.jwt.JwtUtils;
import com.tpe.cookerytech.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserJwtController {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")


    public ResponseEntity<UserResponse> registerUser(@Valid

                                                     @RequestBody RegisterRequest registerRequest){

        UserResponse userResponse =userService.saveUser(registerRequest);


        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);


    }


    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@Valid @RequestBody LoginRequest loginRequest) {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                        loginRequest.getPassword());

        Authentication authentication =
                authenticationManager.authenticate(usernamePasswordAuthenticationToken);


        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwtToken = jwtUtils.generateJWTToken(userDetails);

        LoginResponse loginResponse = new LoginResponse(jwtToken);


        return new ResponseEntity<>(loginResponse,HttpStatus.OK);

    }

}
