package db;

/**
 * Created by Amer on 23-Mar-16.
 * */

import android.provider.BaseColumns;

public class TaskContract {
    public static final String DB_NAME = "malmoteam.dailyactive3.db.tasks";
    public static final int DB_VERSION = 3; //version 3 I add task type to database
    public static final String TABLE = "tasks";

    public class Columns {
        public static final String TASK = "task";
        public static final String TASK_TYPE = "task_type"; //1-ID, 2-IO, 3-ND, 4-NO
        public static final String _ID = BaseColumns._ID;
    }
}