package com.fimo_pitch.model;

import java.io.Serializable;

/**
 * Created by diep1 on 1/9/2017.
 */

public class TimeTable implements Serializable {
    private int image;
    private String name;
    private String id;
    private String type;
    private String description;
    private String size;
    private String start_time;
    private String end_time;
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public TimeTable()
    {

    }

    public String getId() {
        return id;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
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

    public TimeTable(int image, String name, String id, int numberComment, float rating) {
        this.image = image;
        this.name = name;
        this.id = id;
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

    public String getid() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
