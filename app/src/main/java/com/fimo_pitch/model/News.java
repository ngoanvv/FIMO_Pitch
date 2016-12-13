package com.fimo_pitch.model;

import java.io.Serializable;

/**
 * Created by Diep_Chelsea on 13/07/2016.
 */
public class News implements Serializable {
    private String id;
    private String hostID;
    private String time;
    private  String hostName;
    private  String title;
    private  String description;
    private  String location;
    private  String money;


    public News()
    {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHostID() {
        return hostID;
    }

    public void setHostID(String hostID) {
        this.hostID = hostID;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "News{" +
                "hostID='" + hostID + '\'' +
                ", time='" + time + '\'' +
                ", hostName='" + hostName + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", money='" + money + '\'' +
                '}';
    }
}
