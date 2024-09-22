package com.buchsbaumtax.core.model;

public class CustomerContactInfo {
    private int id;
    private String name;
    private String contactType;
    private String memo;
    private String mainDetail;

    public CustomerContactInfo() {}

    public CustomerContactInfo(int id, String name, String contactType, String memo,String mainDetail) {
        this.id = id;
        this.name = name;
        this.contactType = contactType;
        this.memo = memo;
        this.mainDetail = mainDetail;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getContactType() {
        return contactType;
    }

    public String getMemo() {
        return memo;
    }

    public String getMainDetail() {
        return mainDetail;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public void setMainDetail(String mainDetail) {
        this.mainDetail = mainDetail;
    }

    @Override
    public String toString() {
        return "CustomerContactInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", contactType='" + contactType + '\'' +
                ", memo='" + memo + '\'' +
                ", mainDetail='" + mainDetail + '\'' +
                '}';
    }
}

