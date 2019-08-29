package com.omerakkus.todolistapp.db;

import android.provider.BaseColumns;

public class TaskContract {
    public static final String DB_NAME = "todolist.db";
    public static final int DB_VERSION = 8;

    public class TaskEntry implements BaseColumns {
        public static final String TASK_TABLE = "tasksDb";

        public static final String _ID = "_id";
        public static final String COL_TODO_TITLE = "title";
        public static final String COL_TASK_NAME = "name";
        public static final String COL_TASK_DESC = "description";
        public static final String COL_TASK_STATUS = "status";
        public static final String COL_TASK_DUE_TIME = "due_time";
    }

    public class UserEntry implements BaseColumns {
        public static final String USER_TABLE = "userDb";

        public static final String COL_USER_EMAIL_ = "email";
        public static final String COL_USER_PASS = "password";

    }

    public class TodoEntry implements BaseColumns {
        public static final String TODO_TITLE_TABLE = "todoTitleDb";

        public static final String COL_TITLE = "todo_title";

    }
}