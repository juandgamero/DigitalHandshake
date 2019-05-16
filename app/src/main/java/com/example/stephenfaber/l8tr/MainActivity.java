package com.example.stephenfaber.l8tr;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    //initialize the two buttons
    private Button buttonLogin;
    private Button buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // get the button id for login
        buttonLogin = (Button) findViewById(R.id.mainLogin);
        // set on click listener for login button
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to login activity
                openActivityLogin();
            }
        });
        // get button id for Register
        buttonRegister = (Button) findViewById(R.id.mainRegister);
        // set on click listener for register button
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to register activity
                openActivityRegister();
            }
        });
    }

    // go to login form
    public void openActivityLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    // go to register form
    public void openActivityRegister(){

        Intent intent = new Intent(this, Registration.class);
        startActivity(intent);
    }
}
