package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class questionFall extends AppCompatActivity {

    boolean stoped;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.audio);

        mediaPlayer.start();


        setContentView(R.layout.activity_question_fall);

        ConstraintLayout bgElement = (ConstraintLayout) findViewById(R.id.container);
        bgElement.setBackgroundColor(Color.RED);

        // Initializing thetextView
        TextView textView;
        textView = findViewById (R.id.textView2);

        Intent callIntent = new Intent(
                this, call.class
        );

        stoped = false;

        new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                // Used for formatting digit to be in 2 digits only
                NumberFormat f = new DecimalFormat("00");
                long hour = (millisUntilFinished / 3600000) % 24;
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                textView.setText(f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));
            }
            // When the task is over it will print 00:00:00 there
            public void onFinish() {
                textView.setText("00:00:00");
                if(!stoped){
                    startActivity(callIntent);
                    mediaPlayer.stop();
                }
            }
        }.start();


        Intent mainActivittyIntent = new Intent(
                this, MainActivity.class
        );

        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                startActivity(mainActivittyIntent);
                stoped = true;
                mediaPlayer.stop();

            }
        });



    }
}