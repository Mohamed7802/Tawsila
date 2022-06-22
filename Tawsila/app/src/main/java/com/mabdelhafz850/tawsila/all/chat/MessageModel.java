package com.mabdelhafz850.tawsila.all.chat;

public class MessageModel {
    private String message ;
    private String id ;
    private String time;

    public MessageModel(String message, String id, String time) {
        this.message = message;
        this.id = id;
        this.time = time;
    }

    public MessageModel()
    {

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
