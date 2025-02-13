package com.example.common.enums;

public enum StatusEnum {
    CHECKING("checking"),
    CHECK_OK("check passed"),
    CHECK_NOT_OK("check failed"),
    ;

    public String status;

    StatusEnum(String status) {
        this.status = status;
    }
}
