package com.example.cw;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class WriteActivity extends AppCompatActivity {

    private String moodValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write);

        String fileName=getFilename();
        readJournalFile(fileName);

        ImageButton btnMood = (ImageButton) findViewById(R.id.moodButton);

        btnMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WriteActivity.this, MoodActivity.class));
            }
        });

        BottomNavigationView bottomNavigationView=findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.buttonWrite);

        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId())
                {
                    case R.id.buttonCalendar:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.buttonWrite:
                        return true;
                    case R.id.buttonSettings:
                        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        ImageButton btnSave = findViewById(R.id.buttonSubmit);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                EditText journalText = findViewById(R.id.editJournalContent);
                hideSoftKeyboard(WriteActivity.this, v);
                if(journalText.getText().toString().trim().length() > 0){
                    if(moodValue!=null){
                        String filePath = getApplicationContext().getFilesDir() + "/" + fileName + ".txt";
                        Boolean update=checkFileExists(filePath);
                        try {
                            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput(fileName+".txt", Context.MODE_PRIVATE));
                            outputStreamWriter.write(moodValue + "\n");
                            outputStreamWriter.write(journalText.getText().toString());
                            outputStreamWriter.close();
                            if (update){
                                writeMood(updateMood(), Context.MODE_PRIVATE);
                            }
                            else{
                                writeMood(moodValue+"\n", Context.MODE_APPEND);
                            }
                            Toast.makeText(getBaseContext(), "The journal entry has been saved.", Toast.LENGTH_LONG).show();
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        Toast.makeText(getBaseContext(), "Please select a mood.", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getBaseContext(), "Please type something.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null)
            setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();
        int mood=intent.getIntExtra("moodValue", 0);
        if (mood!=0){
            moodValue=Integer.toString(mood);
            setMood();
        }
    }

    //hide keyboard when user selects other buttons
    public static void hideSoftKeyboard (Activity activity, View view)
    {
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
    }

    //get the filename for today
    protected String getFilename(){
        TextView displayDate = (TextView) findViewById(R.id.textDate);
        long date = System.currentTimeMillis();

        SimpleDateFormat sdf = new SimpleDateFormat("d MMMM");
        String dateString = sdf.format(date);
        displayDate.setText(dateString);

        sdf = new SimpleDateFormat("ddMMyy");
        return sdf.format(date);
    }

    //read the file and display it
    protected void readJournalFile(String fileName){
        try {
            EditText journalText = findViewById(R.id.editJournalContent);
            InputStream inputStream = openFileInput(fileName+".txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString=null;
                StringBuilder stringBuilder = new StringBuilder();
                moodValue=bufferedReader.readLine();
                setMood();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString+"\n");
                }

                inputStream.close();
                journalText.setText(stringBuilder.toString(), TextView.BufferType.EDITABLE);
            }
        }
        catch (FileNotFoundException e) {

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //set the image for the mood
    protected void setMood(){
        ImageButton btnMood = (ImageButton) findViewById(R.id.moodButton);
        switch(moodValue){
            case "1":
                btnMood.setBackgroundResource(R.drawable.mood1_foreground);
                break;
            case "2":
                btnMood.setBackgroundResource(R.drawable.mood2_foreground);
                break;
            case "3":
                btnMood.setBackgroundResource(R.drawable.mood3_foreground);
                break;
            case "4":
                btnMood.setBackgroundResource(R.drawable.mood4_foreground);
                break;
            case "5":
                btnMood.setBackgroundResource(R.drawable.mood5_foreground);
                break;
        }

    }

    //update the mood in the stats file
    protected String updateMood(){
        try {
            String statsPath = getApplicationContext().getFilesDir() + "/stats.txt";
            File statsFile = new File( statsPath );
            BufferedReader bufferedReader = new BufferedReader(new FileReader(statsFile));
            String receiveString;
            String previousString=null;
            StringBuilder statsString = new StringBuilder();
            while ( (receiveString = bufferedReader.readLine()) != null ) {
                if (previousString != null){
                    statsString.append(previousString+"\n");
                }
                previousString=receiveString;
            }
            bufferedReader.close();
            statsString.append(moodValue+"\n");
            return(statsString.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //edit the stats file
    protected void writeMood(String str, int mode){
        try {
            OutputStreamWriter osw = new OutputStreamWriter(openFileOutput("stats.txt", mode));
            osw.write(str);
            osw.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    //check if a journal file exists already
    protected boolean checkFileExists(String filePath){
        File journalFile = new File( filePath );
        if (journalFile.exists()){
            return true;
        }
        else{
            return false;
        }
    }

}