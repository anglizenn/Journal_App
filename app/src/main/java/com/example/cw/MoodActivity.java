package com.example.cw;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class MoodActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mood);

        ImageButton btn = (ImageButton) findViewById(R.id.buttonClose);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ImageButton mood1 = (ImageButton) findViewById(R.id.mood1Button);
        ImageButton mood2 = (ImageButton) findViewById(R.id.mood2Button);
        ImageButton mood3 = (ImageButton) findViewById(R.id.mood3Button);
        ImageButton mood4 = (ImageButton) findViewById(R.id.mood4Button);
        ImageButton mood5 = (ImageButton) findViewById(R.id.mood5Button);

        mood1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMood(1);
            }
        });

        mood2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMood(2);
            }
        });

        mood3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMood(3);
            }
        });

        mood4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMood(4);
            }
        });

        mood5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectMood(5);
            }
        });
    }

    //return to previous activity and display mood selected by user
    protected void selectMood(int mood){
        Intent i = new Intent(MoodActivity.this, WriteActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        i.putExtra("moodValue",mood);
        finish();
        startActivity(i);
    }
}
