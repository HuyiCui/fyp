package com.example.common.enums;

public enum ResultCodeEnum {
    SUCCESS("200", "success"),

    PARAM_ERROR("400", "Parameter error"),
    TOKEN_INVALID_ERROR("401", "Invalid token"),
    TOKEN_CHECK_ERROR("401", "Token check error"),
    PARAM_LOST_ERROR("4001", "Parameter lost"),

    SYSTEM_ERROR("500", "System error"),
    USER_EXIST_ERROR("5001", "Exist username"),
    USER_NOT_LOGIN("5002", "User is not logged in"),
    USER_ACCOUNT_ERROR("5003", "Wrong account or password"),
    USER_NOT_EXIST_ERROR("5004", "User does not exist"),
    PARAM_PASSWORD_ERROR("5005", "The original password was entered incorrectly."),
    COLLECT_ALREADY_ERROR("5006", "Already collected"),
    ;

    public String code;
    public String msg;

    ResultCodeEnum(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
