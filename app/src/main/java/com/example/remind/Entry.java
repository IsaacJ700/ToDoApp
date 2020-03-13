package com.example.remind;

import com.google.firebase.firestore.Exclude;

public class Entry {

    private String title;
    private String description;
    private String entryID;

    public Entry(){

    }

    public Entry(String title, String description){
        this.title = title;
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    @Exclude
    public String getEntryID(){
        return entryID;
    }

    public void setEntryID(String entryID){
        this.entryID = entryID;
    }

}
