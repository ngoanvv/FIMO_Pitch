package com.fimo_pitch.model;

import java.io.Serializable;

/**
 * Created by diep1 on 1/8/2017.
 */

public class Price implements Serializable {
    String time;
    String dayOfWeek;
    String price;
    String description;
    String timeStart;
    String timeEnd;
    String status;
    String systemId;
    String id;
    String pitchId;
    String pitchName;

    public String getPitchName() {
        return pitchName;
    }

    public void setPitchName(String pitchName) {
        this.pitchName = pitchName;
    }

    public String getPitchId() {
        return pitchId;
    }

    public void setPitchId(String pitchId) {
        this.pitchId = pitchId;
    }

    @Override
    public String toString() {
        return "Price{" +
                "time='" + time + '\'' +
                ", dayOfWeek='" + dayOfWeek + '\'' +
                ", price='" + price + '\'' +
                ", description='" + description + '\'' +
                ", timeStart='" + timeStart + '\'' +
                ", timeEnd='" + timeEnd + '\'' +
                ", status='" + status + '\'' +
                ", systemId='" + systemId + '\'' +
                ", id='" + id + '\'' +
                ", pitchId='" + pitchId + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
