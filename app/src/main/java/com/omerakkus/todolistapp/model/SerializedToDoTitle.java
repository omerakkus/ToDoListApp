package com.omerakkus.todolistapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SerializedToDoTitle implements Serializable {

    @SerializedName("todoTitles")
    @Expose
    private List<TodoTitle> todoTitles = null;

    public List<TodoTitle> getTodoTitles() {
        return todoTitles;
    }

    public void setTodoTitles(List<TodoTitle> todoTitles) {
        this.todoTitles = todoTitles;
    }


}
