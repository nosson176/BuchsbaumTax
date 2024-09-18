package com.buchsbaumtax.core.model;

public class StatusDetail {
    private Long date;
    private String statusDetail;

    public StatusDetail() {}

    public StatusDetail(Long date, String statusDetail) {
        this.date = date;
        this.statusDetail = statusDetail;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getStatusDetail() {
        return statusDetail;
    }

    public void setStatusDetail(String statusDetail) {
        this.statusDetail = statusDetail;
    }
}

