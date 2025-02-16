package com.buchsbaumtax.core.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

public class AlarmMessageWebSocket {
    private static final Logger logger = LoggerFactory.getLogger(AlarmMessageWebSocket.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private String type;
    private int id;
    private boolean alerted;
    private int priority;
    private int clientId;
    private int secondsSpent;
    private String alarmUserName;
    private Integer alarmUserId;
    private boolean alarmComplete;
    private boolean alert;
    private Date alarmDate;
    private String alarmTime;
    private Long alarmCreateChange;
    private Date logDate;
    private String note;
    private boolean archived;
    private String years;
    private String createdBy;
    private Long createdTime;
    private Long noteDate;

    // Getters and setters for each field

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isAlerted() {
        return alerted;
    }

    public void setAlerted(boolean alerted) {
        this.alerted = alerted;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getSecondsSpent() {
        return secondsSpent;
    }

    public void setSecondsSpent(int secondsSpent) {
        this.secondsSpent = secondsSpent;
    }

    public String getAlarmUserName() {
        return alarmUserName;
    }

    public String getType() {
        return type;
    }

    public void setAlarmUserName(String alarmUserName) {
        this.alarmUserName = alarmUserName;
    }

    public Integer getAlarmUserId() {
        return alarmUserId;
    }

    public void setAlarmUserId(Integer alarmUserId) {
        this.alarmUserId = alarmUserId;
    }

    public boolean isAlarmComplete() {
        return alarmComplete;
    }

    public void setAlarmComplete(boolean alarmComplete) {
        this.alarmComplete = alarmComplete;
    }

    public boolean isAlert() {
        return alert;
    }

    public void setAlert(boolean alert) {
        this.alert = alert;
    }

    public Date getAlarmDate() {
        return alarmDate;
    }

    public void setAlarmDate(Date alarmDate) {
        this.alarmDate = alarmDate;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public Long getAlarmCreateChange() {
        return alarmCreateChange;
    }

    public void setAlarmCreateChange(Long alarmCreateChange) {
        this.alarmCreateChange = alarmCreateChange;
    }

    public Date getLogDate() {
        return logDate;
    }

    public void setLogDate(Date logDate) {
        this.logDate = logDate;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public String getYears() {
        return years;
    }

    public void setYears(String years) {
        this.years = years;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public Long getNoteDate() {
        return noteDate;
    }

    @Override
    public String toString() {
        return "Alarm{" +
                "id=" + id +
                ", alerted=" + alerted +
                ", priority=" + priority +
                ", clientId=" + clientId +
                ", secondsSpent=" + secondsSpent +
                ", alarmUserName='" + alarmUserName + '\'' +
                ", alarmUserId=" + alarmUserId +
                ", alarmComplete=" + alarmComplete +
                ", alert=" + alert +
                ", alarmDate=" + alarmDate +
                ", alarmTime='" + alarmTime + '\'' +
                ", alarmCreateChange=" + alarmCreateChange +
                ", logDate=" + logDate +
                ", note='" + note + '\'' +
                ", archived=" + archived +
                ", years='" + years + '\'' +
                ", createdBy='" + createdBy + '\'' +
                '}';
    }

}
