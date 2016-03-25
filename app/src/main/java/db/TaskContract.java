package db;

/**
 * Created by Amer on 23-Mar-16.
 * */

import android.provider.BaseColumns;

public class TaskContract {
    public static final String DB_NAME = "malmoteam.dailyactive3.db.tasks";
    public static final int DB_VERSION = 4; //version 3 I add task type to database, 4- add task date
    public static final String TABLE = "tasks";

    public class Columns {
        public static final String TASK = "task";
        public static final String TASK_TYPE = "task_type"; //1-ID(important with deadlines), 2-IO(important open), 3-ND(not important but deadlines), 4-NO(not important open)
        public static final String TASK_DATE= "task_date"; //ver 4 date and time of task text format  ISO8601 strings ("YYYY-MM-DD HH:MM:SS.SSS")
        public static final String _ID = BaseColumns._ID;
    }
}