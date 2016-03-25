package malmoteam.dailyactive3;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import db.TaskContract;
import db.TaskDBHelper;

public class MainActivity extends ListActivity {
    private TaskDBHelper helper;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        updateUI();
    }

    private void updateUI() {
        helper = new TaskDBHelper(MainActivity.this);
        SQLiteDatabase sqlDB = helper.getReadableDatabase();
        Cursor cursor = sqlDB.query(TaskContract.TABLE,
                new String[]{TaskContract.Columns._ID, TaskContract.Columns.TASK, TaskContract.Columns.TASK_TYPE},
                null, null, null, null, null);

        AdvSimpleCursorAdapter listAdapter = new AdvSimpleCursorAdapter(
                this,
                R.layout.task_view,
                cursor,
                new String[]{TaskContract.Columns.TASK, TaskContract.Columns.TASK_TYPE},
                new int[]{R.id.taskTextView, R.id.taskViewLayout},
                0
        );
        
        this.setListAdapter(listAdapter);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("menu", "onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_ID: //important dead line
                AlertDialog.Builder builderID = new AlertDialog.Builder(this);
                builderID.setTitle("Add a task");
                builderID.setMessage("What do you want to do?");
                final EditText inputField = new EditText(this);
                builderID.setView(inputField);
                builderID.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Log.d("MainActivity",inputField.getText().toString());
                        String task = inputField.getText().toString();
                        Log.d("MainActivity", task);

                        TaskDBHelper helper = new TaskDBHelper(MainActivity.this);
                        SQLiteDatabase db = helper.getWritableDatabase();
                        ContentValues values = new ContentValues();

                        values.clear();
                        values.put(TaskContract.Columns.TASK, task);
                        values.put(TaskContract.Columns.TASK_TYPE, 1);

                        db.insertWithOnConflict(TaskContract.TABLE, null, values,
                                SQLiteDatabase.CONFLICT_IGNORE);

                        updateUI();
                    }
                });

                builderID.setNegativeButton("Cancel", null);

                builderID.create().show();

//                final DatePicker dateFieldIO = new DatePicker(this);
//                builderID.setView(dateFieldIO);
//                builderID.create().show();
//
                return true;

            case R.id.action_add_IO: //Important Open
                AlertDialog.Builder builderIO = new AlertDialog.Builder(this);
                builderIO.setTitle("Add a Important Open Task");
                builderIO.setMessage("What do you want to do?");
                final EditText inputFieldIO = new EditText(this);
                builderIO.setView(inputFieldIO);
                builderIO.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Log.d("MainActivity",inputField.getText().toString());
                        String task = inputFieldIO.getText().toString();
                        Log.d("MainActivity", task);

                        TaskDBHelper helper = new TaskDBHelper(MainActivity.this);
                        SQLiteDatabase db = helper.getWritableDatabase();
                        ContentValues values = new ContentValues();

                        values.clear();
                        values.put(TaskContract.Columns.TASK, task);
                        values.put(TaskContract.Columns.TASK_TYPE, 2); // for IO important open

                        db.insertWithOnConflict(TaskContract.TABLE, null, values,
                                SQLiteDatabase.CONFLICT_IGNORE);

                        updateUI();
                    }
                });


                builderIO.setNegativeButton("Cancel", null);
                builderIO.create().show();
                return true;

            case R.id.action_add_ND:
                //TODO:

            case R.id.action_add_NO:
                //TODO:

            default:
                return false;
        }
    }

    public void onDoneButtonClick(View view) {
        View v = (View) view.getParent();
        TextView taskTextView = (TextView) v.findViewById(R.id.taskTextView);
        String task = taskTextView.getText().toString();

        String sql = String.format("DELETE FROM %s WHERE %s = '%s'",
                TaskContract.TABLE,
                TaskContract.Columns.TASK,
                task); //it has small bug the all task with same name will be deleted


        helper = new TaskDBHelper(MainActivity.this);
        SQLiteDatabase sqlDB = helper.getWritableDatabase();
        sqlDB.execSQL(sql);
        updateUI();
    }
}
