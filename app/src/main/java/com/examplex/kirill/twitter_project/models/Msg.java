package com.examplex.kirill.twitter_project.models;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class Msg implements Serializable {
    public long id;
    public String msgText;

    public Msg(String msgText) {
        this.msgText = msgText;
    }

    public Msg() {

    }

    public String getMsgText() {
        return msgText;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setMsgText(String msgText) {
        this.msgText = msgText;
    }

    public Msg(long id, String msgText) {
        this.id = id;
        this.msgText = msgText;
    }
}
