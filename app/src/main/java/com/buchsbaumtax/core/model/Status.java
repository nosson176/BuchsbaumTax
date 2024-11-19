package com.buchsbaumtax.core.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
//import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
//@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonInclude(JsonInclude.Include.ALWAYS)

//@JsonSerialize(using = StatusSerializer.class)
public class Status {
    private Long date;
    private String value;

    // Default constructor
    public Status() {}

    // Constructor with parameters
    public Status(Long date, String value) {
        this.date = date;
        this.value = value;
    }

    // Getters and setters
    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    // Method to convert Unix timestamp to human-readable Date
//    public String getFormattedDate() {
//        return date != null ? new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss")
//                .format(new java.util.Date(date)) : null;
//    }

    @Override
    public String toString() {
        return "Status{" +
                "date=" + (date) +
                ", value='" + (value != null ? value : "null") + '\'' +
                '}';
    }
}