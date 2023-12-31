package com.tpe.cookerytech.exception.message;

public class ErrorMessage {


    public final static String CATEGORY_ALREADY_EXIST_EXCEPTION = "This category %s is already exist";
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
    public static final String PRODUCT_PROPERTY_KEY_NOT_FOUND = "Product Property Key %s not found";
    public static final String MODEL_NOT_FOUND_EXCEPTION = "Model with id: %s not found";
    public static final String CURRENCY_NOT_FOUND_EXCEPTION = "Currency with id: %s not found";
    public static final String MODEL_NOT_FOUND_BY_PRODUCT_ID_EXCEPTION = "Model with ProductId: %s not found";

    public static final String PPK_ALREADY_EXIST_EXCEPTION = "Product Property Key with name: %s is already exist";

    public static final String MODEL_ALREADY_EXIST_EXCEPTION = "Model name : %s field already exist";
    public static final String MODEL_FIELD_ALREADY_EXIST_EXCEPTION = "Model Field: %s already exist";
    public static final String CUSTOMER_NOT_FOUND_EXCEPTION = "The customer is not authorized";
    public static final String IMAGE_NOT_FOUND_MESSAGE = "ImageFile with id %s not found";
    public static final String NOT_CREATED_SKU_MESSAGE = "Model sku not unique";

    public static final String USER_FAVORITES_NOT_FOUND_EXCEPTION = "User dont have favorites";

    public static final String SHOPPING_CART_ITEMS_NOT_FOUND = "There is no item in shopping cart";
    public static final String PRODUCT_NOT_FOUND_BY_MODEL_ID_EXCEPTION = "Product with ModelId: %s not found" ;
    public static final String SHOPPING_CART_NOT_FOUND = "There is no shopping cart";

    public static final String OFFER_NOT_FOUND_EXCEPTION = "Offer with id: %s not found";
    public static final String OFFER_ITEM_NOT_FOUND_EXCEPTION = "Offer Item with id: %s not found";

    public static final String OFFER_ITEM_COULD_NOT_BE_DELETED = "Could not be deleted because offer status is not 0 or 3";

    public static final String SAME_DAY_EXCEPTION = "This process cannot be done on the same day";
    public static final String CATEGORY_CANNOT_DELETE_EXCEPTION = "Category cannot be deleted";

    public static final String MODEL_PROPERTY_VALUE_NOT_FOUND = "Model Property Value not found";
    public static final String MODEL_PROPERY_VALUE_CAN_NOT_DELETE = "Product Property Key can not deleted, related to Model Property Value";
}
