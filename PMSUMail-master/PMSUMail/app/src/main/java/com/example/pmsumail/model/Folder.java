package com.example.pmsumail.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Folder implements Parcelable {

    //Anotacije sluze za serijalizovanje, java polje je reprezentovano kao prosledjen parametar u JSON-u

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("rule")
    @Expose
    private Rule rule;
    @SerializedName("subfolder")
    @Expose
    private Folder subfolder;
    @SerializedName("messages")
    @Expose
    private ArrayList<Message> messages;


    public Folder() {
    }

    public Folder(int id, String name, ArrayList<Message> messages, Folder subfolder, Rule rule) {
        this.id = id;
        this.name = name;
        this.messages = messages;
        this.subfolder = subfolder;
        this.rule = rule;
    }



    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

    public Folder getSubfolder() {
        return subfolder;
    }

    public void setSubfolder(Folder subfolder) {
        this.subfolder = subfolder;
    }

    public ArrayList<Message> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<Message> messages) {
        this.messages = messages;
    }

    public Folder(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }


    // Parcelable interfejs predstavlja android implementaciju java serijalizovanja
    // Sluzi da bi se odredjeni objekat mogao parsirati u neku drugu komponentu
    // nama sluzi umesto fragmenata da bismo izlistali konkretnu poruku, folder i kontakta
    // ove metode se automatski generisu kada dodamo implements Parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeParcelable(this.rule, flags);
        dest.writeParcelable(this.subfolder, flags);
        dest.writeTypedList(this.messages);
    }

    protected Folder(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.rule = in.readParcelable(Rule.class.getClassLoader());
        this.subfolder = in.readParcelable(Folder.class.getClassLoader());
        this.messages = in.createTypedArrayList(Message.CREATOR);
    }

    public static final Parcelable.Creator<Folder> CREATOR = new Parcelable.Creator<Folder>() {
        @Override
        public Folder createFromParcel(Parcel source) {
            return new Folder(source);
        }

        @Override
        public Folder[] newArray(int size) {
            return new Folder[size];
        }
    };
}
