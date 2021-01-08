package com.example.chatapp;

import java.util.Date;

//клас для сообщений
public class Message {

    public String userName;
    public String textMessage;
    private long messageTime;

    public Message() {
    }


    public Message(String userName, String textMessage) {
        this.userName = userName;
        this.textMessage = textMessage;

        //для установки времени
        this.messageTime = new Date().getTime();
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }
}
