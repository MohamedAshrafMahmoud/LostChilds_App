package com.example.mohamed.lostchilds.model;

/**
 * Created by mohamed on 4/14/18.
 */

public class User {

    public String name;
    public String phone;
    public String email;
    public String password;
    public String p_image;



    public User(String phone, String email, String password,String P_image) {
       // this.name = name;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.p_image=P_image;
    }

    public User() {
    }

    public String getP_image() {
        return p_image;
    }

    public void setP_image(String p_image) {
        p_image = p_image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
