package com.bscrud.enums;

public enum StatusEnum {
    SUCCESS(200), FAIL(100), ELSE_ERROR(101);
    private int code;

    StatusEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

}
