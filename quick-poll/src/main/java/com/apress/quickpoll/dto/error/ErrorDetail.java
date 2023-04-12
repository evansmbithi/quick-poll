package com.apress.quickpoll.dto.error;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ErrorDetail {
    
    private String title;
    private int status;
    private String detail;
    private Long timestamp;
    private String path;
    private String developerMessage;

    private Map<String, List<ValidationError>> errors = new HashMap<String, List<ValidationError>>();
    
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public Map<String, List<ValidationError>> getErrors() {
        return errors;
    }
    public void setErrors(Map<String, List<ValidationError>> errors) {
        this.errors = errors;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public int getStatus() {
        return status;
    }
    public void setStatus(int status) {
        this.status = status;
    }
    public String getDetail() {
        return detail;
    }
    public void setDetail(String detail) {
        this.detail = detail;
    }
    public Long getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
    public String getDeveloperMessage() {
        return developerMessage;
    }
    public void setDeveloperMessage(String developerMessage) {
        this.developerMessage = developerMessage;
    }

    
}
