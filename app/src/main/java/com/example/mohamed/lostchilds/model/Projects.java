package com.example.mohamed.lostchilds.model;

import java.util.ArrayList;

/**
 * Created by mohamed on 4/21/18.
 */

public class Projects {

    private String username;
    private String image;
    private String name;
    private String title;
    private String category;
    private String description;
    private String skill;
    private String skill_number;
    private ArrayList<String> followers;
    private ArrayList<String> members;

    public Projects(String Username, String image, String name, String title, String category, String description, String skill, String skill_nomber, ArrayList<String> followers,ArrayList<String> members) {
        this.username = Username;
        this.image = image;
        this.name = name;
        this.title = title;
        this.category = category;
        this.description = description;
        this.skill = skill;
        this.skill_number = "1";
        this.followers =followers;
        this.members = members;
    }


    public Projects() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getSkill_number() {
        return skill_number;
    }

    public void setSkill_number(String skill_number) {
        this.skill_number = skill_number;
    }

    public ArrayList<String> getFollowers() {
        return followers;
    }

    public void setFollowers(ArrayList<String> followers) {
        this.followers = followers;
    }

    public ArrayList<String> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<String> members) {
        this.members = members;
    }
}
