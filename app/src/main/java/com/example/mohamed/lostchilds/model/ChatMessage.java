package com.example.mohamed.lostchilds.model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ChatMessage {

private String comment;
private  String publisher;

    public ChatMessage(String comment, String publisher) {
        this.comment = comment;
        this.publisher = publisher;
    }

    public ChatMessage() {

    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}