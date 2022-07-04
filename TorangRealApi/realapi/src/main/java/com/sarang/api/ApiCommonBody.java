package com.sarang.api;

import com.google.gson.Gson;

public class ApiCommonBody {
    private String result;
    private String errCode;
    private String reason;

    public static ApiCommonBody parse(String result) {
        return new Gson().fromJson(result, ApiCommonBody.class);
    }

    public boolean resultOk() {
        return result != null && result.equals("0");
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
