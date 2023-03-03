package com.example.cw;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;
import java.util.Date;

public class SettingsActivity extends AppCompatActivity {

    private PendingIntent pendingIntent;
    private AlarmManager alarmManager;
    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        createNotifChannel();

        setDisplay();

        // Initialize and assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottomNavigation);

        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.buttonSettings);

        // Perform item selected listener
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.buttonWrite:
                        startActivity(new Intent(getApplicationContext(),WriteActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.buttonSettings:
                        return true;
                    case R.id.buttonCalendar:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        Button btnSave = (Button) findViewById(R.id.saveButton);
        Switch btnSwitch = (Switch) findViewById(R.id.switchNotif);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnSwitch.isChecked()){
                    setAlarm();
                    Toast.makeText(getBaseContext(), "Preferences updated.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btnSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("switchkey", isChecked);
                editor.apply();
                if (isChecked){
                    setAlarm();
                    Toast.makeText(getBaseContext(), "Notifications turned on.", Toast.LENGTH_SHORT).show();
                }
                else{
                    cancelAlarm();
                    Toast.makeText(getBaseContext(), "Notifications turned off.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    //set the notification to appear at the specified time every day
    public void start(int hour, int min) {
        Intent intent = new Intent(SettingsActivity.this, NotificationReceiver.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            pendingIntent = PendingIntent.getBroadcast(SettingsActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        }else {
            pendingIntent = PendingIntent.getBroadcast(SettingsActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        }

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.getTime().compareTo(new Date()) < 0)
            calendar.add(Calendar.DAY_OF_MONTH, 1);


        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        }

    }

    //stop all notifications
    public void cancelAlarm(){
        if (alarmManager!=null) {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }

    //create the notification channel
    public void createNotifChannel(){
        String channelId = "journalReminder";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel(channelId, "Reminder", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    //set the display of the notification page to what was previously set by the user
    protected void setDisplay(){
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        boolean silent = settings.getBoolean("switchkey", false);
        int savedHour = settings.getInt("hourkey", 18);
        int savedMin = settings.getInt("minkey", 0);
        Switch btnSwitch = (Switch) findViewById(R.id.switchNotif);
        btnSwitch.setChecked(silent);
        TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
        timePicker.setCurrentHour(savedHour);
        timePicker.setCurrentMinute(savedMin);
    }

    //set the alarm
    protected void setAlarm(){
        cancelAlarm();
        TimePicker timePicker = (TimePicker) findViewById(R.id.timePicker);
        int hour = timePicker.getCurrentHour();
        int min = timePicker.getCurrentMinute();
        start(hour, min);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("hourkey", hour);
        editor.putInt("minkey", min);
        editor.apply();
    }

    }