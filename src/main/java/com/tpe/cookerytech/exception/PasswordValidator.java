package com.tpe.cookerytech.exception;

import java.util.regex.Pattern;

public class PasswordValidator {

    private static final Pattern SPECIAL_CHARS_PATTERN = Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+-=<>]).{8,20}$");


    public static boolean containsSpecialChars(String password) {
        return SPECIAL_CHARS_PATTERN.matcher(password).find();
    }
}


