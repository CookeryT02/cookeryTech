package com.tpe.cookerytech.controller;
import com.tpe.cookerytech.dto.request.AdminUserUpdateRequest;
import com.tpe.cookerytech.dto.request.UpdatePasswordRequest;
import com.tpe.cookerytech.dto.response.CkResponse;
import com.tpe.cookerytech.dto.response.ResponseMessage;
import com.tpe.cookerytech.dto.request.UserUpdateRequest;
import com.tpe.cookerytech.dto.response.UserResponse;
import com.tpe.cookerytech.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }


    @PatchMapping("/auth")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<CkResponse>
    updateUserPassword(@Valid @RequestBody UpdatePasswordRequest updatePasswordRequest) {
        userService.updateUserPassword(updatePasswordRequest);


        CkResponse ckResponse = new CkResponse();
        ckResponse.setMessage(ResponseMessage.PASSWORD_CHANGED_RESPONSE_MESSAGE);
        ckResponse.setSuccess(true);

        return ResponseEntity.ok(ckResponse);
    }
    @GetMapping("/auth")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST') or hasRole('PRODUCT_MANAGER')")
    public ResponseEntity<UserResponse> getAuthUser(){
        UserResponse userResponse = userService.getPrincipal();

        return ResponseEntity.ok(userResponse);
    }

    //UPDATE
    @PutMapping("/auth")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST') or hasRole('PRODUCT_MANAGER') or hasRole('CUSTOMER')")
    public ResponseEntity<UserResponse> updateUser(@Valid @RequestBody UserUpdateRequest userUpdateRequest){

        UserResponse userResponse = userService.updateUser(userUpdateRequest);

        return ResponseEntity.ok(userResponse);

    }


    // USERBYID
    @GetMapping("/{id}/auth")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<UserResponse> getUserById(@PathVariable Long id){

        UserResponse userResponse = userService.getUserById(id);


            return ResponseEntity.ok(userResponse);


    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST')")
    public ResponseEntity<Page<UserResponse>> getAllUsersByPage(
            @RequestParam("q") String q,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam(value = "sort",defaultValue = "create_at") String prop,
            @RequestParam(value="type", required = false, defaultValue = "ASC") Sort.Direction direction) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, prop));
        Page<UserResponse> userResponsePage = userService.getUserPage(q,pageable);
        return ResponseEntity.ok(userResponsePage);
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST')")
    public ResponseEntity<UserResponse> updateUserById(@PathVariable Long id,
                                                       @Valid @RequestBody AdminUserUpdateRequest adminUserUpdateRequest){
        UserResponse userResponse = userService.updateUserById(id,adminUserUpdateRequest);

        return ResponseEntity.ok(userResponse);
    }


    @DeleteMapping("/auth")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST') or hasRole('PRODUCT_MANAGER') or hasRole('CUSTOMER')")
    public void deleteAuthUser(@RequestParam("password") String password){
        userService.deleteCurrentUser(password);

    }


    //Delete user
    @DeleteMapping("/{id}/admin")
    @PreAuthorize("hasRole('ADMIN') or hasRole('SALES_MANAGER') or hasRole('SALES_SPECIALIST')")
    public ResponseEntity<UserResponse> deleteUser(@PathVariable Long id){
        UserResponse userResponse = userService.deleteUser(id);
        return ResponseEntity.ok(userResponse);
    }

}
