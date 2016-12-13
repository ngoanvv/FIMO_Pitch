package com.fimo_pitch.model;

import java.io.Serializable;

/**
 * Created by Diep_Chelsea on 22/08/2016.
 */
public class SystemPitch implements Serializable {
    private String id;
    private String name;
    private String address;
    private String ownerID;
    private String description;
    private String phone;
    private String lat;
    private String lng;
    private String comment;
    private String rating;
    private String ownerName;

    @Override
    public String toString() {
        return "SystemPitch{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", ownerID='" + ownerID + '\'' +
                ", description='" + description + '\'' +
                ", phone='" + phone + '\'' +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                ", comment='" + comment + '\'' +
                ", rating='" + rating + '\'' +
                ", ownerName='" + ownerName + '\'' +
                '}';
    }

    public String getPhone() {
        return phone;
    }
    public String getDescription() {
        return description;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(String ownerID) {
        this.ownerID = ownerID;
    }



    public void setPhone(String phone) {
        this.phone = phone;
    }
}
