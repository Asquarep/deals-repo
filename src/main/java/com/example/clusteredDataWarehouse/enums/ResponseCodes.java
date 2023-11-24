package com.example.clusteredDataWarehouse.enums;

public enum ResponseCodes {

    SUCCESS("00"),
    FAILURE("01");

    private final String value;

    ResponseCodes(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
