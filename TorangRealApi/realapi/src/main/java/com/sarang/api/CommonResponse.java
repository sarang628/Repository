package com.sarang.api;

public class CommonResponse<V> {
    private final V model;

    public CommonResponse(V model) {
        this.model = model;
    }

    public ApiResult getResult() {
        return model == null ? ApiResult.ERROR : ApiResult.OK;
    }

    public V getModel() {
        return model;
    }
}
