package malmoteam.dailyactive3;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
//facebook
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import db.TaskContract;
import db.TaskDBHelper;

public class MainActivity extends ListActivity {
    private TaskDBHelper helper;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        updateUI();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    private void updateUI() {
        helper = new TaskDBHelper(MainActivity.this);
        SQLiteDatabase sqlDB = helper.getReadableDatabase();
        Cursor cursor = sqlDB.query(TaskContract.TABLE,
                new String[]{TaskContract.Columns._ID, TaskContract.Columns.TASK, TaskContract.Columns.TASK_DATE, TaskContract.Columns.TASK_TYPE},
                null, null, null, null, TaskContract.Columns.TASK_TYPE);

        AdvSimpleCursorAdapter listAdapter = new AdvSimpleCursorAdapter(
                this,
                R.layout.task_view,
                cursor,
                new String[]{TaskContract.Columns.TASK, TaskContract.Columns.TASK_DATE, TaskContract.Columns.TASK_TYPE},
                new int[]{R.id.taskTextView, R.id.taskDateView, R.id.taskViewLayout},
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

        final MainActivity passableThis = this;

        switch (item.getItemId()) {
            case R.id.action_add_ID: //important dead line
                AlertDialog.Builder builderID = new AlertDialog.Builder(this);
                builderID.setTitle("Add a important task with deadline");
                builderID.setMessage("What do you want to do?");
                final EditText inputFieldID = new EditText(this);

                builderID.setView(inputFieldID);
                builderID.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        final String taskName = inputFieldID.getText().toString();

                        AlertDialog.Builder builderDateID = new AlertDialog.Builder(passableThis);
                        final DatePicker inputFieldDate = new DatePicker(passableThis);
                        builderDateID.setView(inputFieldDate);
                        builderDateID.setNegativeButton("Cancel", null);
                        builderDateID.setPositiveButton("Pick", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                AlertDialog.Builder builderTimeID = new AlertDialog.Builder(passableThis);
                                final TimePicker inputFieldTime = new TimePicker(passableThis);
                                builderTimeID.setView(inputFieldTime);
                                builderTimeID.setNegativeButton("Cancel", null);
                                builderTimeID.setPositiveButton("Pick", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        TaskDBHelper helper = new TaskDBHelper(MainActivity.this);
                                        SQLiteDatabase db = helper.getWritableDatabase();
                                        ContentValues values = new ContentValues();

//                                      TimeZone tz = TimeZone.getTimeZone("UTC");
                                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm");
//                                        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//                                      df.setTimeZone(tz);
                                        int y, m, d, h, n;

                                        y = inputFieldDate.getYear();
                                        m = inputFieldDate.getMonth();
                                        d = inputFieldDate.getDayOfMonth();
                                        h = inputFieldTime.getCurrentHour().intValue();
                                        n = inputFieldTime.getCurrentMinute().intValue();

                                        Date date = new Date(y - 1900, m, d, h, n);

                                        String taskDate = df.format(date);

                                        values.clear();
                                        values.put(TaskContract.Columns.TASK, taskName);
                                        values.put(TaskContract.Columns.TASK_TYPE, 1);
                                        values.put(TaskContract.Columns.TASK_DATE, taskDate);
                                        int notiNo = (int) Math.random() * 100000;
                                        values.put(TaskContract.Columns.TASK_NOTIFICATION, notiNo);

                                        db.insertWithOnConflict(TaskContract.TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);

                                        updateUI();

                                        //Notitification
                                        Intent myIntent = new Intent(passableThis, MainActivity.class);

                                        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                                        PendingIntent pendingIntent = PendingIntent.getService(passableThis, 0, myIntent, 0);

                                        Calendar cal = Calendar.getInstance();
                                        cal.set(Calendar.HOUR_OF_DAY, h);
                                        cal.set(Calendar.MINUTE, m);
                                        cal.set(Calendar.SECOND, 0);

                                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 0, pendingIntent);  //set repeating every 24 hours

//                                        PendingIntent pIntent = PendingIntent.getActivity(passableThis, (int) System.currentTimeMillis(), intent, 0);

// build notification
// the addAction re-use the same intent to keep the example short
                                        Notification noti = new Notification.Builder(passableThis)
                                                .setContentTitle("Daily Active")
                                                .setContentText(taskName)
                                                .setSmallIcon(R.drawable.add_red)
                                                .setAutoCancel(true).build();

                                        NotificationManager nM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                                        nM.notify(notiNo, noti);

                                    }
                                });

                                builderTimeID.create().show();
                            }
                        });

                        builderDateID.create().show();
                        Log.d("date", Integer.toString(inputFieldDate.getYear()));

                    }
                });

                builderID.setNegativeButton("Cancel", null);

                builderID.create().show();

                return true;

            case R.id.action_add_IO: //Important Open
                AlertDialog.Builder builderIO = new AlertDialog.Builder(this);
                builderIO.setTitle("Add an Important Open Task without deadline");
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
                AlertDialog.Builder builderND = new AlertDialog.Builder(this);
                builderND.setTitle("Add a non-important task with deadline");
                builderND.setMessage("What do you want to do?");
                final EditText inputFieldND = new EditText(this);
                builderND.setView(inputFieldND);

                builderND.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Log.d("MainActivity",inputField.getText().toString());
                        final String taskName = inputFieldND.getText().toString();

                        AlertDialog.Builder builderDateND = new AlertDialog.Builder(passableThis);
                        final DatePicker inputFieldDate = new DatePicker(passableThis);
                        builderDateND.setView(inputFieldDate);
                        builderDateND.setNegativeButton("Cancel", null);
                        builderDateND.setPositiveButton("Pick", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                TaskDBHelper helper = new TaskDBHelper(MainActivity.this);
                                SQLiteDatabase db = helper.getWritableDatabase();
                                ContentValues values = new ContentValues();

//                                TimeZone tz = TimeZone.getTimeZone("UTC");
//                                DateFormat df = new SimpleDateFormat("yyyy-MM-dd' 'HH:mm");
                                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//                                df.setTimeZone(tz);
                                Date d = new Date(inputFieldDate.getYear() - 1900, inputFieldDate.getMonth(), inputFieldDate.getDayOfMonth());

                                String taskDate = df.format(d);

                                values.clear();
                                values.put(TaskContract.Columns.TASK, taskName);
                                values.put(TaskContract.Columns.TASK_TYPE, 3);
                                values.put(TaskContract.Columns.TASK_DATE, taskDate);

                                db.insertWithOnConflict(TaskContract.TABLE, null, values,
                                        SQLiteDatabase.CONFLICT_IGNORE);

                                updateUI();
                            }
                        });

                        builderDateND.create().show();

                    }
                });

                builderND.setNegativeButton("Cancel", null);

                builderND.create().show();
                return true;

            case R.id.action_add_NO:
                AlertDialog.Builder builderNO = new AlertDialog.Builder(this);
                builderNO.setTitle("Add a non-important task without dealine");
                builderNO.setMessage("What do you want to do?");
                final EditText inputFieldNO = new EditText(this);
                builderNO.setView(inputFieldNO);
                builderNO.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //Log.d("MainActivity",inputField.getText().toString());
                        String task = inputFieldNO.getText().toString();
                        Log.d("MainActivity", task);

                        TaskDBHelper helper = new TaskDBHelper(MainActivity.this);
                        SQLiteDatabase db = helper.getWritableDatabase();
                        ContentValues values = new ContentValues();

                        values.clear();
                        values.put(TaskContract.Columns.TASK, task);
                        values.put(TaskContract.Columns.TASK_TYPE, 4);

                        db.insertWithOnConflict(TaskContract.TABLE, null, values,
                                SQLiteDatabase.CONFLICT_IGNORE);

                        updateUI();
                    }
                });

                builderNO.setNegativeButton("Cancel", null);

                builderNO.create().show();
                return true;

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
        //know exactly where the problem is /Marija

        helper = new TaskDBHelper(MainActivity.this);
        SQLiteDatabase sqlDB = helper.getWritableDatabase();
        sqlDB.execSQL(sql);
        updateUI();
    }

    public void onShareButtonClick(View view) {
        View v = (View) view.getParent();
        TextView taskTextView = (TextView) v.findViewById(R.id.taskTextView);
        String task = taskTextView.getText().toString();

        //TODO: share this task to facebook
        Log.d("Facebook Share", "Test facebook share:" + task);
    }
}
