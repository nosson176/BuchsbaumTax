package com.buchsbaumtax.core.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

@JsonIgnoreProperties(ignoreUnknown = true)

public class Client {

    public interface BasicView {}

    @JsonView(BasicView.class)
    private int id;
    @JsonView(BasicView.class)
    private String lastName;
    private String status;
    private String owesStatus;
    private boolean archived;
    private String periodical;
    private String displayName;
    private String displayPhone;
    private Date created;
    private Date updated;
    private Long statusChangeDate;
    private Integer gFlag;
    private boolean active;
    @JsonView(BasicView.class)
    private List<ClientFlag> flags = new ArrayList<>();
    private List<Filing> filings = new ArrayList<>(); // Add a list of filings
    private List<Log> logs = new ArrayList<>();
    // Getters and Setters for Client fields

    public int getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getStatus() {
        return status;
    }

    public String getOwesStatus() {
        return owesStatus;
    }

    public boolean isArchived() {
        return archived;
    }

    public String getPeriodical() {
        return periodical;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Integer getgFlag() {
        return gFlag;
    }
    public void setFlag(Integer gFlag) {
        this.gFlag = gFlag;
    }
    public boolean getActive() {
        return active;
    }
    public void setActive(boolean active) {
        this.active = active;
    }

    public String getDisplayPhone() {
        return displayPhone;
    }

    public void setDisplayPhone(String displayPhone) {
        this.displayPhone = displayPhone;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getUpdated() {
        return updated;
    }

    public Long getStatusChangeDate() {
        return statusChangeDate;
    }

    public Long setStatusChangeDate(Long date) {
        return statusChangeDate = date;
    }

    public List<ClientFlag> getFlags() {
        return flags;
    }

    public void setFlags(List<ClientFlag> flags) {
        this.flags = flags;
    }

    // Getter and setter for filings
    public List<Filing> getFilings() {
        return filings;
    }

    public void setFilings(List<Filing> filings) {
        this.filings = filings;
    }

    public List<Log> getLogs() {
        return logs;
    }

    public void setLogs(List<Log> logs) {
        this.logs = logs;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", lastName='" + lastName + '\'' +
                ", status='" + status + '\'' +
                ", owesStatus='" + owesStatus + '\'' +
                ", archived=" + archived +
                ", periodical='" + periodical + '\'' +
                ", displayName='" + displayName + '\'' +
                ", displayPhone='" + displayPhone + '\'' +
                ", created=" + created +
                ", updated=" + updated +
                ", statusChangeDate=" + statusChangeDate +
                ", flags=" + flags +
                ", filings=" + filings + // Include filings in toString method
                ", logs=" + logs +
                ", gFlag=" + gFlag +
                '}';
    }
}
