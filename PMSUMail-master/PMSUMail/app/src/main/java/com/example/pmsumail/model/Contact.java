package com.example.pmsumail.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Contact implements Parcelable {


    public enum Format {
        PLAIN,
        HTML
    }

    //Anotacije sluze za serijalizovanje, java polje je reprezentovano kao prosledjen parametar u JSON-u

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("firstname")
    @Expose
    private String firstname;
    @SerializedName("lastname")
    @Expose
    private String lastname;
    @SerializedName("display")
    @Expose
    private String display;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("photo")
    @Expose
    private Photo photo;
    @SerializedName("f")
    @Expose
    private Format f;
    private String note;


    public Contact() {
    }

    public Contact(int id, String firstname, String lastname, String display, String email, Photo photo, Format f) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.display = display;
        this.email = email;
        this.photo = photo;
        this.f = f;
    }


    public Contact(String firstname, String lastname, String display, String email, Photo photo) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.display = display;
        this.email = email;
        this.photo = photo;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public Photo getPhoto() {
        return photo;
    }

    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    public Format getF() {
        return f;
    }

    public void setF(Format f) {
        this.f = f;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                '}';
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
        dest.writeString(this.firstname);
        dest.writeString(this.lastname);
        dest.writeString(this.display);
        dest.writeString(this.email);
        //dest.writeParcelable(this.photo, flags);
        dest.writeInt(this.f == null ? -1 : this.f.ordinal());
    }

    protected Contact(Parcel in) {
        this.id = in.readInt();
        this.firstname = in.readString();
        this.lastname = in.readString();
        this.display = in.readString();
        this.email = in.readString();
        //this.photo = in.readParcelable(Photo.class.getClassLoader());
        int tmpF = in.readInt();
        this.f = tmpF == -1 ? null : Format.values()[tmpF];
    }

    public static final Parcelable.Creator<Contact> CREATOR = new Parcelable.Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel source) {
            return new Contact(source);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };


}
