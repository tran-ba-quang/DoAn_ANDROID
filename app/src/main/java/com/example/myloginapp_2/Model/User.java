package com.example.myloginapp_2.Model;

public class User {
    private String id;
    private  String username;
    private String imageURL;
    private String status;
    private  int unseenMessages;
    public User(String id, String username, String imageURL, String status, int unseenMessages){
        this.id = id;
        this.username = username;
        this.imageURL = imageURL;
        this.status = status;
        this.unseenMessages = unseenMessages;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getUnseenMessages() {
        return unseenMessages;
    }

    public void setUnseenMessages(int unseenMessages) {
        this.unseenMessages = unseenMessages;
    }

    public User(){

    }


}
