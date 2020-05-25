package com.example.apptodo.Object;

public class AddToList {

    private String listID;
    private String ToDo;

    public AddToList() {

    }

    public AddToList(String listID, String toDo) {
        this.listID = listID;
        ToDo = toDo;
    }

    public String getListID() {
        return listID;
    }

    public void setListID(String listID) {
        this.listID = listID;
    }

    public String getToDo() {
        return ToDo;
    }

    public void setToDo(String toDo) {
        ToDo = toDo;
    }
}
