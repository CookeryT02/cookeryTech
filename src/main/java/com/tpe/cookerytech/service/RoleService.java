package com.tpe.cookerytech.service;


import com.tpe.cookerytech.domain.Role;
import com.tpe.cookerytech.domain.enums.RoleType;
import com.tpe.cookerytech.exception.ResourcesNotFoundException;
import com.tpe.cookerytech.exception.message.ErrorMessage;
import com.tpe.cookerytech.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;


    public Role findByType(RoleType roleType){
        Role role = roleRepository.findByType(roleType).orElseThrow(()->
                new ResourcesNotFoundException(
                        String.format(ErrorMessage.ROLE_NOT_FOUND_EXCEPTION, roleType.name())));
        return role;
    }
}
