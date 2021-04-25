package com.example.challenge;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class Message {
    private String name;
    private String content;
    private String data;

    public Message(String name, String content, String data) {
        this.name = name;
        this.content = content;
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getData() {
        String[] splited = data.split("T");
        String year = splited[0].split("-")[0];
        String month = splited[0].split("-")[1];
        String day = splited[0].split("-")[2];
        String hour = splited[1].split(":")[0];
        String minute = splited[1].split(":")[1];
        String formated = hour + ":" + minute + ", " + day + " " + month +" " + year;
        return formated;
    }

    public void setData(String data) {
        this.data = data;
    }
}
