package com.buchsbaumtax.app.dto;

public class BaseResponse {
    private String success; // "Success" or "Failure"
    private String msg;     // Optional message
    private Object data;    // Optional additional data

    // Constructor with message and data
    public BaseResponse(String success, String msg, Object data) {
        this.success = success;
        this.msg = msg;
        this.data = data;
    }

    // Constructor with message only
    public BaseResponse(boolean success, String msg) {
        this.success = success ? "Success" : "Failure";
        this.msg = msg;
        this.data = null;
    }

    // Constructor with success only
    public BaseResponse(boolean success) {
        this.success = success ? "Success" : "Failure";
        this.msg = null;
        this.data = null;
    }

    // Getters and Setters
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
