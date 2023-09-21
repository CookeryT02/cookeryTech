package com.tpe.cookerytech.domain.enums;

public enum LogType {

    CREATED("User create a offer"),

    UPDATED("User updated the offer"),
    DELETED("User deleted the offer"),

    APPROVED("Sales Manager has approved the offer"),

    DECLINED("Sales Manager has declined the offer");

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
