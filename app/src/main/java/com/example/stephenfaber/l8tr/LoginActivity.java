package com.example.stephenfaber.l8tr;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    // initialize login button
    private Button buttonLogin;
    // initialize email and password edit text
    private EditText emailEditText;
    private EditText passwordEditText;
    // initialize the firebase authentication
    private FirebaseAuth firebaseAuth;
    private String testUser;

    //progress bar to show account being created loading
    private ProgressBar progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // start new firebase authentication instance
        firebaseAuth = FirebaseAuth.getInstance();
        // get the id for email edit text
        emailEditText = (EditText) findViewById(R.id.emailLoginEditText);
        // get the id for the password edit text
        passwordEditText = (EditText) findViewById(R.id.passwordLoginEditText);

        progress = (ProgressBar) findViewById(R.id.progressbar);

        // get the id for the login button
        buttonLogin = (Button)findViewById(R.id.loginButton);
        // set the on click listener for the login button
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to the loginUser function
                loginUser();
            }
        });

    }

    // login the user
    public void loginUser() {
        // get the string value in the email edit text field
        String email = emailEditText.getText().toString().trim();
        // get the string value in the password edit text field
        String password = passwordEditText.getText().toString().trim();

        // initialize the error String to empty
        String loginError = "";
        // check if email field is empty
        if (TextUtils.isEmpty(email)) {
            //update the login error
            loginError += "Please enter email.";

        }
        // check if password field is empty
        if (TextUtils.isEmpty(password)) {
            //update the login error
            loginError += "Please enter password.";

        }

        // check if there were any login error
        if (loginError.compareTo("") != 0) {
            //display login error
            Toast.makeText(this, loginError, Toast.LENGTH_LONG).show();
        }
        else {
            progress.setVisibility(View.VISIBLE);

            // there are no login error, attempt to login
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progress.setVisibility(View.GONE);
                            // check if login was successful
                            if (task.isSuccessful()) {
                                // display Authentication was successful
                                //Toast.makeText(getApplicationContext(), "Authentication Success.", Toast.LENGTH_SHORT).show();
                                // get user information
                                FirebaseUser user = firebaseAuth.getCurrentUser();
                                Toast.makeText(getApplicationContext(), "Authentication Success", Toast.LENGTH_SHORT).show();

                                testUser = user.getUid();
                                //call profileUser method with user information as parameter
                                profileUser();

                            } else {
                                // Login failure
                                // display Authentication was unsuccessful
                                Toast.makeText(getApplicationContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();

                            }

                        }
                    });


        }

    }

    // open up the user profile activity
    public void profileUser()
    {
        Intent intent = new Intent(this, profile.class);
        startActivity(intent);
    }
}


