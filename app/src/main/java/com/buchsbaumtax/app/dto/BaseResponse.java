package com.buchsbaumtax.app.dto;

public class BaseResponse {
    private String success;

    public BaseResponse(boolean success) {
        this.success = success ? "Success" : "Failure";
    }
}
