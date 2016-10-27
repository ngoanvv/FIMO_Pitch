package com.fimo_pitch.model;

/**
 * Created by Diep_Chelsea on 22/08/2016.
 */
public class SystemPitch {
    private String id;
    private String name;
    private String address;
    private String ownerID;
    private String contact;
    private String comment;
    private String rating;
    private String ownerName;
//    private String id;
//    private String id;
//    private String id;
//    private String id;
//    private String id;
//    private String id;
//    private String id;
//    private String id;
//    private String id;

    @Override
    public String toString() {
        return "SystemPitch{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", ownerID='" + ownerID + '\'' +
                ", contact='" + contact + '\'' +
                '}';
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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }
}
