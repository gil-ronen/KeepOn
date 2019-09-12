package com.gil_shiran_or.keepon.chat;

import java.util.HashMap;
import java.util.Map;

public class Message {

    private String message;
    private String author;
    private String time;

    public Message(String message, String author, String time) {
        this.message = message;
        this.author = author;
        this.time = time;
    }

    public Message() {
    }

    public String getMessage() {
        return message;
    }

    public String getAuthor() {
        return author;
    }

    public String getTime() {
        return time;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();

        result.put("message", message);
        result.put("author", author);
        result.put("time", time);

        return result;
    }
}
