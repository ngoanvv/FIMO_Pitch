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
