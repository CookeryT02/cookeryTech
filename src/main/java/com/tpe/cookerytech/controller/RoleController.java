package com.tpe.cookerytech.controller;

import com.tpe.cookerytech.domain.Role;
import com.tpe.cookerytech.domain.enums.RoleType;
import com.tpe.cookerytech.dto.response.CkResponse;
import com.tpe.cookerytech.repository.RoleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/createRole")
public class RoleController {

    private final RoleRepository roleRepository;

    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public ResponseEntity<CkResponse> createRole(){
        createRoleIfNotExists("Customer",RoleType.ROLE_CUSTOMER);
        createRoleIfNotExists("Product Manager",RoleType.ROLE_PRODUCT_MANAGER);
        createRoleIfNotExists("Sales Specialist",RoleType.ROLE_SALES_SPECIALIST);
        createRoleIfNotExists("Sales Manager",RoleType.ROLE_SALES_MANAGER);
        createRoleIfNotExists("Administrator", RoleType.ROLE_ADMIN);

        return  ResponseEntity.ok(new CkResponse());
    }


    private void createRoleIfNotExists(String roleName,  RoleType roleType) {
        if (!(roleRepository.findAll().isEmpty())) {
            Role role = new Role();
            role.setName(roleName);
            role.setType(roleType);
            roleRepository.save(role);
        }
    }
}
