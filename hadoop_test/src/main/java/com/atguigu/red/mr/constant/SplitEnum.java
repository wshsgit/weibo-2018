package com.atguigu.red.mr.constant;

public enum SplitEnum {
    TAB("\t"),
    SPACE(" ");

    private String value;

    SplitEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}