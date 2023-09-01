package com.tpe.cookerytech.dto.domain.enums;

public enum LogType {

    CREATED("User create a offer"),
    UPDATED,
    DELETED,
    APPROVED,
    DECLINED;

    private String message;

    LogType() {
    }

    LogType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
