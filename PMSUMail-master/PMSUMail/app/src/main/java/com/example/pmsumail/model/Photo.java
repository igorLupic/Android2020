package com.example.pmsumail.model;

import android.graphics.Bitmap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Photo {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("path")
    @Expose
    private String path;
    @SerializedName("bitmap")
    @Expose
    private Bitmap bitmap;

    public Photo() {
    }

    public Photo(int id,String path) {
        this.id = id;
        this.path = path;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
