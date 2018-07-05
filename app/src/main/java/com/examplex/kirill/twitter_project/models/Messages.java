package com.examplex.kirill.twitter_project.models;

import java.util.Date;

import io.realm.RealmObject;

public class Messages extends RealmObject {
    String msgText;
    Date msgDate;

    public String getMsgText() {
        return msgText;
    }

    public void setMsgText(String msgText) {
        this.msgText = msgText;
    }

    public Date getMsgDate() {
        return msgDate;
    }

    public void setMsgDate(Date msgDate) {
        this.msgDate = msgDate;
    }
}
