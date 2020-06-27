package com.example.pmsumail.model;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;

public class Message implements Parcelable {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("from")
    @Expose
    private String from;
    @SerializedName("to")
    @Expose
    private String to;
    @SerializedName("cc")
    @Expose
    private String cc;
    @SerializedName("bcc")
    @Expose
    private String bcc;
    @SerializedName("account")
    @Expose
    private Account account;
    @SerializedName("dateTime")
    @Expose
    private Date dateTime;
    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("attachments")
    @Expose
    private ArrayList<Attachment> attachments;
    @SerializedName("unread")
    @Expose
    private boolean messageRead;
    @SerializedName("tags")
    @Expose
    private ArrayList<Tag> tags;

    public Message() {
    }

    public Message(int id, String from, String to, String cc, String bcc, Account account, Date dateTime, String subject, String content, ArrayList<Attachment> attachments, ArrayList<Tag> tags) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.cc = cc;
        this.bcc = bcc;
        this.account = account;
        this.dateTime = dateTime;
        this.subject = subject;
        this.content = content;
        this.attachments = attachments;
        this.tags = tags;
        this.account=account;
    }

    public Message(String from, String to, String cc, String bcc, Account account, Date dateTime, String subject, String content, ArrayList<Attachment> attachments, ArrayList<Tag> tags) {
        this.from = from;
        this.to = to;
        this.cc = cc;
        this.bcc = bcc;
        this.account = account;
        this.dateTime = dateTime;
        this.subject = subject;
        this.content = content;
        this.attachments = attachments;
        this.tags = tags;
    }

    public boolean isMessageRead() {
        return messageRead;
    }

    public void setMessageRead(boolean messageRead) {
        this.messageRead = messageRead;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }


    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ArrayList<Attachment> getAttachments() {
        return attachments;
    }

    public void setAttachments(ArrayList<Attachment> attachments) {
        this.attachments = attachments;
    }

    public ArrayList<Tag> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Tag> tags) {
        this.tags = tags;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.from);
        dest.writeString(this.to);
        dest.writeString(this.cc);
        dest.writeString(this.bcc);
        dest.writeSerializable(this.account);
        dest.writeLong(this.dateTime != null ? this.dateTime.getTime() : -1);
        dest.writeString(this.subject);
        //dest.writeByte((byte) (messageRead ? 1 : 0));
        dest.writeString(this.content);
        dest.writeList(this.attachments);
        dest.writeList(this.tags);
    }

    protected Message(Parcel in) {
        this.id = in.readInt();
        this.from = in.readString();
        this.to = in.readString();
        this.cc = in.readString();
        this.bcc = in.readString();
        this.account = (Account) in.readSerializable();
        long tmpDateTime = in.readLong();
        this.dateTime = tmpDateTime == -1 ? null : new Date(tmpDateTime);
        this.subject = in.readString();
        this.content = in.readString();
        //this.messageRead = in.readByte() != 0;
        this.attachments = new ArrayList<Attachment>();
        in.readList(this.attachments, Attachment.class.getClassLoader());
        this.tags = new ArrayList<Tag>();
        in.readList(this.tags, Tag.class.getClassLoader());
    }

    public static final Parcelable.Creator<Message> CREATOR = new Parcelable.Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel source) {
            return new Message(source);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };
}