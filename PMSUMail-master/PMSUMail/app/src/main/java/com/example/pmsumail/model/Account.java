package com.example.pmsumail.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Account implements Serializable {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("smtp")
    @Expose
    private String smtp;
    @SerializedName("pop3")
    @Expose
    private String pop3;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("display")
    @Expose
    private String display;

    public Account() {
    }

    public Account(int id, String smtp, String pop3, String username, String password, String email, String display) {
        this.id = id;
        this.smtp = smtp;
        this.pop3 = pop3;
        this.username = username;
        this.password = password;
        this.email = email;
        this.display = display;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSmtp() {
        return smtp;
        }

        public void setSmtp(String smtp) {
            this.smtp = smtp;
        }

        public String getPop3() {
            return pop3;
        }

        public void setPop3(String pop3) {
            this.pop3 = pop3;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
