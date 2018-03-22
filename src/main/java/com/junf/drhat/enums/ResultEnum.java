package com.junf.drhat.enums;

public enum ResultEnum {

    UNKONW_ERROR(-1, "未知错误"),
    SUCCESS(0, "成功"),
    SEARCH(100, "查找失败"),
    NUMDUPLICATE(101, "号码重复"),
    NOTFOUND(102,"用户不存在")

    ;

    private Integer code;

    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
