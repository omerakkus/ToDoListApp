package com.omerakkus.todolistapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SerializedToDoItem implements Serializable {

    @SerializedName("toDoItems")
    @Expose
    private List<ToDoItem> toDoItems = null;


    public List<ToDoItem> getToDoItems() {
        return toDoItems;
    }

    public void setToDoItems(List<ToDoItem> toDoItems) {
        this.toDoItems = toDoItems;
    }
}
