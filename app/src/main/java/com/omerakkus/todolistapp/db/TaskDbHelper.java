package com.omerakkus.todolistapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.omerakkus.todolistapp.model.ToDoItem;
import com.omerakkus.todolistapp.model.TodoTitle;
import com.omerakkus.todolistapp.model.Users;

import java.util.ArrayList;
import java.util.List;

public class TaskDbHelper extends SQLiteOpenHelper {


    SQLiteDatabase db;
    public TaskDbHelper(Context context) {
        super(context, TaskContract.DB_NAME, null, TaskContract.DB_VERSION);
    }

    String createTaskTable = "CREATE TABLE " + TaskContract.TaskEntry.TASK_TABLE + " ( " +
            TaskContract.TaskEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TaskContract.TaskEntry.COL_TODO_TITLE + " TEXT NOT NULL, " +
            TaskContract.TaskEntry.COL_TASK_NAME + " TEXT NOT NULL, " +
            TaskContract.TaskEntry.COL_TASK_DESC + " TEXT NOT NULL, " +
            TaskContract.TaskEntry.COL_TASK_STATUS + " TEXT, " +
            TaskContract.TaskEntry.COL_TASK_DUE_TIME + " INTEGER);";

    String createUserTable = "CREATE TABLE " + TaskContract.UserEntry.USER_TABLE + " ( " +
            TaskContract.UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TaskContract.UserEntry.COL_USER_EMAIL_ + " TEXT NOT NULL, " +
            TaskContract.UserEntry.COL_USER_PASS  + " TEXT NOT NULL);";

    String createTodoTitle = "CREATE TABLE " + TaskContract.TodoEntry.TODO_TITLE_TABLE + " ( " +
            TaskContract.TodoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TaskContract.TodoEntry.COL_TITLE  + " TEXT NOT NULL);";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createTaskTable);
        db.execSQL(createUserTable);
        db.execSQL(createTodoTitle);
        this.db = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Log.d(TAG, "Upgrade Database, clear "+ TaskContract.TaskEntry.TASK_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TaskContract.TaskEntry.TASK_TABLE);
        onCreate(db);
    }

    public void insertUser(Users users){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TaskContract.UserEntry.COL_USER_EMAIL_, users.getEmail());
        values.put(TaskContract.UserEntry.COL_USER_PASS, users.getPassword());
        db.insert(TaskContract.UserEntry.USER_TABLE,null, values);
        db.close();

    }

    public void insertTodoTitle(TodoTitle title){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TaskContract.TodoEntry.COL_TITLE, title.getTodo_title());
        db.insert(TaskContract.TodoEntry.TODO_TITLE_TABLE, null,values);
        db.close();
    }

    public void insertTodoList(ToDoItem toDoItem){
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TaskContract.TaskEntry.COL_TODO_TITLE, toDoItem.getTitleOfTodoList());
        values.put(TaskContract.TaskEntry.COL_TASK_NAME, toDoItem.getName());
        values.put(TaskContract.TaskEntry.COL_TASK_DESC, toDoItem.getDescription());
        values.put(TaskContract.TaskEntry.COL_TASK_STATUS, toDoItem.getStatus());
        values.put(TaskContract.TaskEntry.COL_TASK_DUE_TIME, toDoItem.getDue_time());
        db.insert(TaskContract.TaskEntry.TASK_TABLE, null, values);
        db.close();
    }

    public int deleteTodoItem(String name){
        db = this.getWritableDatabase();
        Integer numberOFEntriesDeleted= db.delete(TaskContract.TaskEntry.TASK_TABLE, " name= '" + name + "'" , null) ;
        return numberOFEntriesDeleted;
    }

    public int deleteTodoList(String title){
        db = this.getWritableDatabase();
        Integer numberOFEntriesDeleted= db.delete(TaskContract.TodoEntry.TODO_TITLE_TABLE, " todo_title= '" + title + "'" , null) ;
        return numberOFEntriesDeleted;
    }

    public List<ToDoItem> listOfTodo(String title, boolean searchItem){
        db = this.getReadableDatabase();
        String query = "SELECT * FROM "+TaskContract.TaskEntry.TASK_TABLE;
        if(!searchItem){
            query += " WHERE title= '" + title + "'";
        }else{
            query += " WHERE name LIKE '%" + title + "%'";
        }
        Cursor cursor = db.rawQuery(query, null);
        List<ToDoItem> todoLists = new ArrayList<>();
        while(cursor.moveToNext()){
           ToDoItem toDoItem = new ToDoItem(cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COL_TODO_TITLE)),
                   cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_NAME)),
                   cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_DESC)),
                   Integer.toString(cursor.getInt(cursor.getColumnIndex(
                           TaskContract.TaskEntry.COL_TASK_DUE_TIME))),
                   cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_STATUS)));
                todoLists.add(toDoItem);
        }

        return todoLists;
    }


    public List<TodoTitle> listTodoTitle(){
        db = this.getReadableDatabase();
        String query = "SELECT todo_title from "+TaskContract.TodoEntry.TODO_TITLE_TABLE;
        Cursor cursor = db.rawQuery(query, null);
        List<TodoTitle> todoItems = new ArrayList<>();
        while(cursor.moveToNext()){
            TodoTitle  todoTitle = new TodoTitle(cursor.getString(cursor.getColumnIndex(TaskContract.TodoEntry.COL_TITLE)));
            todoItems.add(todoTitle);
        }

        return todoItems;
    }

    public String searchPass(String email) {
        db = this.getReadableDatabase();
        String query = "SELECT email, password from "+TaskContract.UserEntry.USER_TABLE;
        Cursor cursor = db.rawQuery(query, null);
        String a, b;
        b = " Not Found";
        if(cursor.moveToFirst()){
            do{
                a = cursor.getString(0);
                if(a.equals(email)){
                    b = cursor.getString(1);
                    break;
                }
            }
            while(cursor.moveToNext());
        }
        return b;
    }

    public List<ToDoItem> filterItems(String filterName, String filterStatus){
        db = this.getReadableDatabase();
        String query = "SELECT * FROM "+TaskContract.TaskEntry.TASK_TABLE;
        if(filterStatus!= null){
            query += " WHERE status= '" + filterStatus + "'";
        }
        if(filterName.equals("ASC")){
            query += " ORDER BY name ASC";
        }else{
            query += " ORDER BY name DESC";
        }
        Cursor cursor = db.rawQuery(query, null);
        List<ToDoItem> todoLists = new ArrayList<>();
        while(cursor.moveToNext()){
            ToDoItem toDoItem = new ToDoItem(cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COL_TODO_TITLE)),
                    cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_NAME)),
                    cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_DESC)),
                    Integer.toString(cursor.getInt(cursor.getColumnIndex(
                            TaskContract.TaskEntry.COL_TASK_DUE_TIME))),
                    cursor.getString(cursor.getColumnIndex(TaskContract.TaskEntry.COL_TASK_STATUS)));
            todoLists.add(toDoItem);
        }
        return todoLists;
    }

}