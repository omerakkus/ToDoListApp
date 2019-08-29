package com.omerakkus.todolistapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import butterknife.BindView;

public class TodoTitle implements Serializable {
    @SerializedName("todo_id")
    @Expose
    private Integer todo_id;
    @SerializedName("todo_title")
    @Expose
    private  String todo_title;

    public Integer getTodo_id() {
        return todo_id;
    }

    public void setTodo_id(Integer todo_id) {
        this.todo_id = todo_id;
    }

    public String getTodo_title() {
        return todo_title;
    }

    public void setTodo_title(String todo_title) {
        this.todo_title = todo_title;
    }

    public TodoTitle(String titleTodo){
        super();
        this.todo_title = titleTodo;
    }

}
