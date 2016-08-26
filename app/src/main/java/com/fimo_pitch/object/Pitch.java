package com.fimo_pitch.object;

import java.io.Serializable;

/**
 * Created by TranManhTien on 21/08/2016.
 */
public class Pitch implements Serializable {
    int image;
    String name;
    String address;
    int numberComment;
    float numberPoint;


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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getNumberComment() {
        return numberComment;
    }

    public void setNumberComment(int numberComment) {
        this.numberComment = numberComment;
    }

    public float getNumberPoint() {
        return numberPoint;
    }

    public void setNumberPoint(float numberPoint) {
        this.numberPoint = numberPoint;
    }


}
