package com.tpe.cookerytech.dto.domain.enums;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

public enum RoleType {

    ROLE_CUSTOMER("Customer"),
    ROLE_PRODUCT_MANAGER("Product Manager"),
    ROLE_SALES_SPECIALIST("Sales Specialist"),
    ROLE_SALES_MANAGER("Sales Manager"),
    ROLE_ADMIN("Administrator");

    private String name;

    private RoleType(String name) {
        this.name=name;
    }

    public String getName() {
        return name;
    }

}
