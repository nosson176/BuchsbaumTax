package com.buchsbaumtax.core.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Log {
    private static final Logger logger = LoggerFactory.getLogger(Log.class);
    private static final ObjectMapper objectMapper = new ObjectMapper();

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
    private String historyLogJson = "[]"; // New field to store JSON string

    public int getId() {
        return id;
    }

    public boolean isAlerted() {
        return alerted;
    }

    public int getPriority() {
        return priority;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public int getClientId() {
        return clientId;
    }

    public int getSecondsSpent() {
        return secondsSpent;
    }

    public String getAlarmUserName() {
        return alarmUserName;
    }

    public void setAlarmUserName(String alarmUserName) {
        this.alarmUserName = alarmUserName;
    }

    public Integer getAlarmUserId() {
        return alarmUserId;
    }

    public boolean isAlarmComplete() {
        return alarmComplete;
    }

    public boolean isAlert() {
        return alert;
    }

    public Date getAlarmDate() {
        return alarmDate;
    }
    public Long getAlarmCreateChange() {
        return alarmCreateChange;
    }

    public Date getLogDate() {
        return logDate;
    }

    public String getNote() {
        return note;
    }

    public boolean isArchived() {
        return archived;
    }

    public String getYears() {
        return years;
    }
    public String getCreatedBy() {
        return createdBy;
    }

    public List<HistoryLog> getHistoryLog() {
        if (historyLogJson == null || historyLogJson.isEmpty() || historyLogJson.equals("[]")) {
            return new ArrayList<>();
        }
        try {
            return objectMapper.readValue(historyLogJson, new TypeReference<List<HistoryLog>>(){});
        } catch (JsonProcessingException e) {
            logger.error("Error converting JSON to historyLog", e);
            return new ArrayList<>();
        }
    }

    public void setHistoryLog(List<HistoryLog> historyLog) {
        try {
            this.historyLogJson = (historyLog == null || historyLog.isEmpty())
                    ? "[]"
                    : objectMapper.writeValueAsString(historyLog);
        } catch (JsonProcessingException e) {
            logger.error("Error converting historyLog to JSON", e);
            this.historyLogJson = "[]";
        }
    }

    public String getHistoryLogJson() {
        return historyLogJson;
    }

    public void setHistoryLogJson(String historyLogJson) {
        this.historyLogJson = (historyLogJson == null || historyLogJson.isEmpty()) ? "[]" : historyLogJson;
    }



    @Override
    public String toString() {
        return "Log{" +
                "id=" + id +
                ", alerted=" + alerted +
                ", priority=" + priority +
                ", alarmTime='" + alarmTime + '\'' +
                ", clientId=" + clientId +
                ", secondsSpent=" + secondsSpent +
                ", alarmUserName='" + alarmUserName + '\'' +
                ", alarmUserId=" + alarmUserId +
                ", alarmComplete=" + alarmComplete +
                ", alert=" + alert +
                ", alarmDate=" + alarmDate +
                ", alarmCreateChange=" + alarmCreateChange +
                ", logDate=" + logDate +
                ", note='" + note + '\'' +
                ", archived=" + archived +
                ", years='" + years + '\'' +
                ", createdBy='" + createdBy + '\'' +
                '}';
    }

}
