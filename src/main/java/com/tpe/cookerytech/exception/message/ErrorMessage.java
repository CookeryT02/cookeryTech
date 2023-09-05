package com.tpe.cookerytech.exception.message;

public class ErrorMessage {
    public final static String CATEGORY_ALREADY_EXIST_EXCEPTION = "This category is already exist";
    public final static String PASSWORD_NOT_MATCHED_MESSAGE = "Your passwords are not matched";
    public final static String NOT_PERMITTED_METHOD_MESSAGE = "You don't have any permission to change this data";
    public final static String PRINCIPAL_FOUND_MESSAGE = "User not found";
    public final static String JWTTOKEN_ERROR_MESSAGE = "JWT Token Validation Error: %s";
    public static final String USER_NOT_FOUND_EXCEPTION = "User with email : %s not found";
    public static final String EMAIL_ALREADY_EXIST_MESSAGE = "Email: %s already exist";
    public static final String ROLE_NOT_FOUND_EXCEPTION = "Role : %s not found";
    public static final String USER_PASSWORD_CONTROL = "Password field should be at least 8 characters long,include at least one letter,one number and one special character";
    public static final String CODE_NOT_VALID = "Code is not valid";
    public static final String RESOURCE_NOT_FOUND_EXCEPTION = "Resource with id %s not found";
    public static final String WRONG_PASSWORD_EXCEPTION = "Wrong Password";
    public static final String BRAND_NOT_FOUND_EXCEPTION = "Brand with id: %s not found";
    public static final String BRAND_CANNOT_DELETE_EXCEPTION = "Brand with this id %s cannot updatable";
    public static final String PRODUCT_USED_MESSAGE = "Product is used by another category";
    public static final String CATEGORY_NOT_FOUND_EXCEPTION = "Category with id: %s not found";
    public static final String PRODUCT_NOT_FOUND_EXCEPTION = "Product with id: %s not found";
    public static final String PRODUCT_CANNOT_DELETE_EXCEPTION = "Product with this id %s cannot deleted";
    public static final String PRODUCT_PROPERTY_KEY_NOT_FOUND = "Product Property Key not found";
    public static final String CURRENCY_NOT_FOUND_EXCEPTION = "Currency not found";
    public static final String MODEL_NOT_FOUND_EXCEPTION = "Model not found";
}
