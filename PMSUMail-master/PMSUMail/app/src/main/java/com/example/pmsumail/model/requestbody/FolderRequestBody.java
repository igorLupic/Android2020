package com.example.pmsumail.model.requestbody;

public class FolderRequestBody {

    private String name;
    private String destination;
    private int parentFolder;

    public FolderRequestBody(String name, String destination, int parentFolder) {
        this.name = name;
        this.destination = destination;
        this.parentFolder = parentFolder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getParentFolder() {
        return parentFolder;
    }

    public void setParentFolder(int parentFolder) {
        this.parentFolder = parentFolder;
    }

}
