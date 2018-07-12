package com.examplex.kirill.twitter_project.models;


import java.util.Date;

import io.realm.Realm;
import io.realm.RealmObject;

public class Messages extends RealmObject{

    public static final String MSG_ID = "msgId";
    public static final String MSG_Text = "msgText";
    public static final String MSG_msgDate = "msgDate";


    long msgId;
    String msgText;
    Date msgDate;

    public Messages() {
    }

    public long getMsgId() {
        return msgId;
    }

    public void setMsgId(long msgId) {
        this.msgId = msgId;
    }

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

    public int idNextVal (Realm realm)

    {
        Number maxId;
        try {
            maxId = (long) (realm.where(Messages.class).max(Messages.MSG_ID));
        }catch (NullPointerException e)
        {
           maxId = null;
        }
        int nextId = (maxId == null) ? 1 : maxId.intValue() + 1;
        return  nextId;

    }
}
