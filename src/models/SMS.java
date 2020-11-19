package models;

import java.util.Date;

public abstract class SMS {
    private int id;

    private SMSTemplate smsTemplate;

    private int modeId;

    private Date sentDate;

    public SMS() {
    }

    public SMS(int id, SMSTemplate smsTemplate, int modeName, Date sentDate) {
        this.id = id;
        this.smsTemplate = smsTemplate;
        this.modeId = modeName;
        this.sentDate = sentDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SMSTemplate getSmsTemplate() {
        return smsTemplate;
    }

    public void setSmsTemplate(SMSTemplate smsTemplate) {
        this.smsTemplate = smsTemplate;
    }

    public int getModeId() {
        return modeId;
    }

    public void setModeId(int modeId) {
        this.modeId = modeId;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public void setSentDate(Date sentDate) {
        this.sentDate = sentDate;
    }
}
