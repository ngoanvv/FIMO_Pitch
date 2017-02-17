package com.fimo_pitch.model;

import java.io.Serializable;

/**
 * Created by diep1_000 on 9/19/2016.
 */
public class Pitch implements Serializable {
    private int image;
    private String name;
    private String id;
    private String type;
    private String description;
    private String size;
    private String phone;
    private String systemId;

    public String getSystemId() {
        return systemId;
    }

    public void setSystemId(String systemId) {
        this.systemId = systemId;
    }

    public String getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Pitch()
    {

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "Pitch{" +
                "image=" + image +
                ", name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }



    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public void setId(String id) {
        this.id = id;
    }




}