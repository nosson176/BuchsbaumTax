package com.buchsbaumtax.app.dto;

public class BaseResponse {
    private String success;
    private String msg;

    // Constructor with message
    public BaseResponse(boolean success, String msg) {
        this.success = success ? "Success" : "Failure";
        this.msg = msg;
    }

    // Constructor without message
    public BaseResponse(boolean success) {
        this.success = success ? "Success" : "Failure";
        this.msg = null; // or provide a default value, e.g., "No message provided"
    }

    // Getters and Setters (optional)
    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
