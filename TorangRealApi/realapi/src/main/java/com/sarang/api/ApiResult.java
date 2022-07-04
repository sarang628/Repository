package com.sarang.api;

public enum ApiResult {

    OK(-1),
    ERROR(0);

    private final int value;

    ApiResult(int i) {
        value = i;
    }

    public int getValue() {
        return value;
    }
}
