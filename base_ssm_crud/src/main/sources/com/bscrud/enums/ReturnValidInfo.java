package com.bscrud.enums;

public enum ReturnValidInfo {
    NAMEOK(100, "用户名可用"), NAMEEXISTS(101, "用户名已存在"), NAMENOTMATCH(102, "只接受2-6位的中文和数字或3-12位的英文和数字"),
    EMAILOK(200, "邮箱可用"), EMAILEXISTS(201, "邮箱已存在"), EMAILNOTMATCH(202, "邮箱格式错误");
    private final String info;
    private final int code;

    ReturnValidInfo(int code, String info) {
        this.code = code;
        this.info = info;
    }

    public int getCode() {
        return this.code;
    }

    public String getInfo() {
        return this.info;
    }
}
