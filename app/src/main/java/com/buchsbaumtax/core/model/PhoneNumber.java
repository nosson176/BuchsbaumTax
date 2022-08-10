package com.buchsbaumtax.core.model;

public class PhoneNumber {
    private int id;
    private String phoneNumber;
    private String name;

    public PhoneNumber() {

    }

    public PhoneNumber(String phoneNumber, String name) {
        this.phoneNumber = phoneNumber;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getName() {
        return name;
    }
}
