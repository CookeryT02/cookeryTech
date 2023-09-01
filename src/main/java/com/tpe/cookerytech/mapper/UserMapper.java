package com.tpe.cookerytech.mapper;

import com.tpe.cookerytech.dto.domain.User;
import com.tpe.cookerytech.dto.request.RegisterRequest;
import com.tpe.cookerytech.dto.request.UserUpdateRequest;
import com.tpe.cookerytech.dto.response.UserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse userToUserResponse(User user);

    User userRequestToUser(RegisterRequest registerRequest);


    User userUpdateRequestToUser(UserUpdateRequest userUpdateRequest);


}
