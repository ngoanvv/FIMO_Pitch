package com.fimo_pitch.model;

/**
 * Created by diep1 on 1/10/2017.
 */

public class Order {
    String id;
    String userName;
    String userAddress;
    String userPhone;
    String timeId;
    String pitchId;
    String systempitchId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getTimeId() {
        return timeId;
    }

    public void setTimeId(String timeId) {
        this.timeId = timeId;
    }

    public String getPitchId() {
        return pitchId;
    }

    public void setPitchId(String pitchId) {
        this.pitchId = pitchId;
    }

    public String getSystempitchId() {
        return systempitchId;
    }

    public void setSystempitchId(String systempitchId) {
        this.systempitchId = systempitchId;
    }
}
