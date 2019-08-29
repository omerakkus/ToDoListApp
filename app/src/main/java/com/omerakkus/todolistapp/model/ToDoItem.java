package com.omerakkus.todolistapp.model;

import android.graphics.Color;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import static android.graphics.Color.rgb;

public class ToDoItem implements Serializable {

    @SerializedName("titleOfTodoList")
    @Expose
    private String titleOfTodoList;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("due_time")
    @Expose
    private String due_time;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("id")
    @Expose
    private int  id;
    @SerializedName("priorityColor")
    @Expose
    private int priorityColor;


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDue_time() {
        return due_time;
    }

    public void setDue_time(String due_time) {
        this.due_time = due_time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPriorityColor() {
        return priorityColor;
    }

    public void setPriorityColor(int priorityColor) {
        this.priorityColor = priorityColor;
    }

    public static int[] getColorCode() {
        return colorCode;
    }

    public static int   getColorCodeById(int id) {
        return colorCode[id];
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitleOfTodoList() {
        return titleOfTodoList;
    }

    public void setTitleOfTodoList(String titleOfTodoList) {
        this.titleOfTodoList = titleOfTodoList;
    }

    public static final int     PRIO_DATE = 0;
    public static final int     PRIO_HOUR = 1;
    public static final int     PRIO_TOOLATE = 2;

    public static final int[]   colorCode = { rgb(34,139,34), Color.rgb(255,140,0), Color.RED};

    public ToDoItem(String title, String name, String description, String due_time, String status) {
        super();
        this.titleOfTodoList = title;
        this.name = name;
        this.description = description;
        this.due_time = due_time;
        //this.id = id;
        this.status = status;
        this.priorityColor = 0;

    }
}
