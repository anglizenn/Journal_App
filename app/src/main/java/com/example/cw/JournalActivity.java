package com.example.cw;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JournalActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.journal);

        Intent intent = getIntent();
        String journalDate=intent.getStringExtra("journalDate");

        if (journalDate!=null){
            String filePath = getApplicationContext().getFilesDir() + "/" + journalDate + ".txt";
            File journalFile = new File( filePath );

            String str=getFilename(journalDate);

            TextView textDate = (TextView) findViewById(R.id.textDate);
            textDate.setText(str);

            readJournalFile(journalFile);
        }

        ImageButton btn = (ImageButton) findViewById(R.id.buttonClose);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0,0);
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null)
            setIntent(intent);
    }

    //convert date to filename
    protected String getFilename(String journalDate){
        String inputPattern = "ddMMyy";
        String outputPattern = "d MMMM yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(journalDate);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    //read the contents of the journal file and display
    protected void readJournalFile(File journalFile){
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(journalFile));
            String receiveString=null;
            StringBuilder stringBuilder = new StringBuilder();

            String moodValue=bufferedReader.readLine();
            setMood(moodValue);

            while ( (receiveString = bufferedReader.readLine()) != null ) {
                stringBuilder.append(receiveString+"\n");
            }
            bufferedReader.close();
            TextView journalText = findViewById(R.id.journalContent);
            journalText.setText(stringBuilder.toString());
        }
        catch (FileNotFoundException e) {
            System.out.println(e);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //set the image of the mood
    protected void setMood (String moodValue){
        ImageView moodView = (ImageView) findViewById(R.id.moodView);
        switch(moodValue){
            case "1":
                moodView.setBackgroundResource(R.drawable.mood1_foreground);
                break;
            case "2":
                moodView.setBackgroundResource(R.drawable.mood2_foreground);
                break;
            case "3":
                moodView.setBackgroundResource(R.drawable.mood3_foreground);
                break;
            case "4":
                moodView.setBackgroundResource(R.drawable.mood4_foreground);
                break;
            case "5":
                moodView.setBackgroundResource(R.drawable.mood5_foreground);
                break;
        }
    }
}