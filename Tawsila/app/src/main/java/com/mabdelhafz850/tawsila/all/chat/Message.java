package com.mabdelhafz850.tawsila.all.chat;

public class Message {

    private String Id ;
    private String Content ;
    private String CurrentTime ;

    public Message() {
    }

    public Message(String id, String content, String currentTime) {
        Id = id;
        Content = content;
        CurrentTime = currentTime;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getCurrentTime() {
        return CurrentTime;
    }

    public void setCurrentTime(String currentTime) {
        CurrentTime = currentTime;
    }
}
