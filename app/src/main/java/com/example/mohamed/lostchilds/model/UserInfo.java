package com.example.mohamed.lostchilds.model;

public class UserInfo {
    private String userName;
    private String firstName;
    private String lastName;
    private String jobtitle;
    private String password;
    private String image;

    private String facebook;
    private String github;
    private String linkedin;


    private String Accounts;


    public UserInfo(String userName,String firstName, String lastName, String jobtitle, String password, String image, String facebook, String github, String linkedin) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.jobtitle = jobtitle;
        this.password = password;
        this.image = image;

        this.facebook = "";
        this.github = "";
        this.linkedin = "";
    }



    public UserInfo() {
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getGithub() {
        return github;
    }

    public void setGithub(String github) {
        this.github = github;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public void setLinkedin(String linkedin) {
        this.linkedin = linkedin;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getJobtitle() {
        return jobtitle;
    }

    public void setJobtitle(String jobtitle) {
        this.jobtitle = jobtitle;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
