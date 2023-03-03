package com.example.cw;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    protected int total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get values of the mood in the statistics section
        String statsPath = getApplicationContext().getFilesDir() + "/stats.txt";
        File statsFile = new File( statsPath );
        moodStats(statsFile);

        CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            //check what day was selected
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                //get filename of date selected by user
                String fileName=selectJournalDate(dayOfMonth, year, month);
                String filePath = getApplicationContext().getFilesDir() + "/" + fileName + ".txt";
                System.out.println(filePath);
                File journalFile = new File( filePath );

                //check if the file exists
                if (journalFile.exists()){ //start the journalactivity if it exists
                    Intent i = new Intent(MainActivity.this, JournalActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    i.putExtra("journalDate",fileName);
                    startActivity(i);
                    overridePendingTransition(0,0);
                }
                else {
                    Toast.makeText(getBaseContext(), "No journal entry.", Toast.LENGTH_SHORT).show();}
                }
        });

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottomNavigation);
        //set the current page
        bottomNavigationView.setSelectedItemId(R.id.buttonCalendar);

        //check which button on the menu pressed
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId())
                {
                    case R.id.buttonWrite:
                        startActivity(new Intent(getApplicationContext(),WriteActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.buttonCalendar:
                        return true;
                    case R.id.buttonSettings:
                        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

    //count the number of times each mood appeared
    public int countChar(String str, char c)
    {
        int count = 0;

        for(int i=0; i < str.length(); i++)
        {    if(str.charAt(i) == c)
            count++;
        }

        total+=count;
        return count;
    }

    //read file to get value of each mood
    protected void moodStats(File statsFile){
        try{
            BufferedReader bufferedReader = new BufferedReader(new FileReader(statsFile));
            String receiveString;
            StringBuilder statsString = new StringBuilder();
            while ( (receiveString = bufferedReader.readLine()) != null ) {
                statsString.append(receiveString+"\n");
            }
            bufferedReader.close();
            TextView mood1 = (TextView) findViewById(R.id.valueMood1);
            mood1.setText(String.valueOf(countChar(statsString.toString(), '1')));
            TextView mood2 = (TextView) findViewById(R.id.valueMood2);
            mood2.setText(String.valueOf(countChar(statsString.toString(), '2')));
            TextView mood3 = (TextView) findViewById(R.id.valueMood3);
            mood3.setText(String.valueOf(countChar(statsString.toString(), '3')));
            TextView mood4 = (TextView) findViewById(R.id.valueMood4);
            mood4.setText(String.valueOf(countChar(statsString.toString(), '4')));
            TextView mood5 = (TextView) findViewById(R.id.valueMood5);
            mood5.setText(String.valueOf(countChar(statsString.toString(), '5')));
            TextView totalJournal = (TextView) findViewById(R.id.valueJournal);
            totalJournal.setText(String.valueOf(total));
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    //get the filename of the selected date
    protected String selectJournalDate(int day, int year, int month){
        String  dateOfMonth = String.valueOf(day);
        String  Year = String.valueOf(year);
        String  Month = String.valueOf(month+1);
        String str = dateOfMonth+"/"+Month+"/"+Year;
        System.out.println(str);
        SimpleDateFormat inputFormat = new SimpleDateFormat("d/M/yyyy");
        SimpleDateFormat outputFormat = new SimpleDateFormat("ddMMyy");

        Date date = null;
        String fileName = null;

        try {
            date = inputFormat.parse(str);
            fileName = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return fileName;
    }

}