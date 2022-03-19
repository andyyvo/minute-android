package com.example.minute.recording;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.minute.R;
import com.google.android.material.snackbar.Snackbar;


import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class emptytest extends AppCompatActivity {

    TextView helloText;
    private Button btnStart;
    private TextView textSnackbar;

    private Call mCall;
    private final OkHttpClient mOkHttpClient = new OkHttpClient();

    // access code
    private String accessTok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emptytest);

        helloText = (TextView) findViewById(R.id.helloText);
        helloText.setText("hi! This is a sentence I think I would maybe like to test");

        btnStart = (Button) findViewById(R.id.btnStart);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dothing();
            }
        });
    }

    public void dothing() {
        helloText.setText("thing done");

        if (accessTok == null) {
            final Snackbar snackbar = Snackbar.make(textSnackbar, "Lack Access Token", Snackbar.LENGTH_SHORT);
            snackbar.show();
            return;
        }


    }
}