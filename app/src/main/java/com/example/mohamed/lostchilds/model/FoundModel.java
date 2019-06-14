package com.example.mohamed.lostchilds.model;

public class FoundModel {

    private String child_name;
    private String phone;
    private String description;
    private String child_img;
    private String date;
    private String helper;
    private double latidude;
    private double longitude;



    public FoundModel() {
    }

    public FoundModel(String child_name, String phone, String description, String child_img, String date, String helper, double latidude, double longitude) {
        this.child_name = child_name;
        this.phone = phone;
        this.description = description;
        this.child_img = child_img;
        this.date = date;
        this.helper = helper;
        this.latidude = latidude;
        this.longitude = longitude;
    }

    public String getChild_name() {
        return child_name;
    }

    public void setChild_name(String child_name) {
        this.child_name = child_name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getChild_img() {
        return child_img;
    }

    public void setChild_img(String child_img) {
        this.child_img = child_img;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHelper() {
        return helper;
    }

    public void setHelper(String helper) {
        this.helper = helper;
    }

    public double getLatidude() {
        return latidude;
    }

    public void setLatidude(double latidude) {
        this.latidude = latidude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
