package com.example.kirk;

import com.example.kirk.Model.User;

public class Message extends OneClass{
    public User user;

    public Message(User user, String message) {
        this.user = user;
        this.message = message;
    }

    public String message;
}
