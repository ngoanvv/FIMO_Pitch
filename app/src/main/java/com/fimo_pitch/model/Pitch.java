package com.fimo_pitch.model;

import java.io.Serializable;

/**
 * Created by diep1_000 on 9/19/2016.
 */
public class Pitch implements Serializable {
    private int image;
    private String name;
    private String address;
    private int numberComment;
    private float rating;

    public float getRating() {
        return rating;
    }
    public Pitch()
    {

    }
    public Pitch(int image, String name, String address, int numberComment, float rating) {
        this.image = image;
        this.name = name;
        this.address = address;
        this.numberComment = numberComment;
        this.rating = rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
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



}