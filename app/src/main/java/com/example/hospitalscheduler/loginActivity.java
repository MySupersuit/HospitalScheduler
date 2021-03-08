package com.example.hospitalscheduler;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class loginActivity extends AppCompatActivity {

    //Variables needed for the login screen

    //Login Button on screen
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //XML variable linked to Java variable for each element
        loginButton = findViewById(R.id.button);

        // For now - the login button moves to the Overview screen
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Change screen to overview
                Intent toOverview = new Intent(loginActivity.this,Overview.class);
                //Switch screen
                startActivity(toOverview);
            }
        });
    }
}