package com.enums;

/**
 * Created with IDEA
 * user: zhangwu
 * Date: 2018/3/12
 * Time: 13:06
 **/
public enum BusinessEnum {

     PC("pc"),
     WAP("wap"),
     POPUP("popup");

    private String value;

    BusinessEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
