package com.fimo_pitch.model;

import java.sql.Date;

/**
 * Created by Diep_Chelsea on 28/03/2016.
 */
public class NotificationModel {
    private String id;
    private String fieldId;
    private String name;
    private String avatar;
    private String message;
    private String date;
    private String type;
    private boolean isRead;

    public NotificationModel(String name, String avatar, String message, String date, String type, boolean isRead) {
        this.name = name;
        this.avatar = avatar;
        this.message = message;
        this.date = date;
        this.type = type;
        this.isRead = isRead;
    }

    public String getFieldId() {
        return fieldId;
    }

    public void setFieldId(String fieldId) {
        this.fieldId = fieldId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }


    }