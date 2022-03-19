package com.example.minute.recording;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.minute.R;

public class emptytest extends AppCompatActivity {

    TextView helloText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emptytest);

        helloText = (TextView) findViewById(R.id.helloText);
        helloText.setText("Hello World!");
    }
}