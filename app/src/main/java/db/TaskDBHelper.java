package db;


/**
 * Created by Amer on 23-Mar-16.
 * */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import db.TaskContract;

public class TaskDBHelper extends SQLiteOpenHelper {

    public TaskDBHelper(Context context) {
        super(context, TaskContract.DB_NAME, null, TaskContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqlDB) {
        String sqlQuery =
                String.format("CREATE TABLE %s (" +
                                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " + "%s TEXT" +
                                "%s TEXT, %s INTEGER, %s TEXT)",
                        TaskContract.TABLE,
                        TaskContract.Columns._ID, //here /M
                        TaskContract.Columns.TASK,
                        TaskContract.Columns.TASK_TYPE,
                        TaskContract.Columns.TASK_DATE); //Time not working /M

        Log.d("TaskDBHelper", "Query to form table: " + sqlQuery);
        sqlDB.execSQL(sqlQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqlDB, int i, int i2) {
        sqlDB.execSQL("DROP TABLE IF EXISTS " + TaskContract.TABLE);
        onCreate(sqlDB);
    }
}