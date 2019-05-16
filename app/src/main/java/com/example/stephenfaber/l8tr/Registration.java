package com.example.stephenfaber.l8tr;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Registration extends AppCompatActivity implements View.OnClickListener
{
    // initializing button to create account
    private Button createAccount;
    // initializing emailEditText, passwordEditText, and confirm passwordEditText fields
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private EditText nameEditText;
    private EditText personalEmailEditText;
    private EditText phoneEditText;

    //progress bar to show account being created loading
    ProgressBar progress;

    //declares an instance of Firebase Firestore database
    private FirebaseFirestore db;
    // declares an instance of Firebase Authentication
    private FirebaseAuth mAuth;
    String newUserUID;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // getting id for emailEditText
        emailEditText = (EditText) findViewById(R.id.email);
        // getting id for button to create account
        createAccount = (Button) findViewById(R.id.createAccount);
        // getting id for passwordEditText
        passwordEditText = (EditText) findViewById(R.id.password);
        // getting id for confirm passwordEditText
        confirmPasswordEditText = (EditText) findViewById(R.id.confirmPassword);
        //getting id for users name
        nameEditText = (EditText) findViewById(R.id.name);
        // getting id for users personal emailEditText
        personalEmailEditText = (EditText) findViewById(R.id.personalEmail);
        //getting id for users phone number
        phoneEditText = (EditText) findViewById(R.id.phone);
        // getting id for progress bar
        progress = (ProgressBar) findViewById(R.id.progressbar);

        //mFunctions = FirebaseFunctions.getInstance();
        db = FirebaseFirestore.getInstance();
        // Initializes Firebase Authentication instance
        mAuth = FirebaseAuth.getInstance();

        // sets an on click listener for create account button
        createAccount.setOnClickListener(this);

    }
    // on click listener for button that creates
    @Override
    public void onClick(View view){
        switch(view.getId())
        {
            case R.id.createAccount:
                registerUser();
                break;

        }

    }

    // registers user with their emailEditText and passwordEditText
    private void registerUser(){

        // getting sting value for users emailEditText
        final String userEmail = emailEditText.getText().toString().trim();
        // getting string value for users passwordEditText
        String userPassword = passwordEditText.getText().toString().trim();
        // getting string value for the users confirmed passwordEditText
        String userConfirmedPassword = confirmPasswordEditText.getText().toString().trim();
        // getting string value for the users name
        final String name = nameEditText.getText().toString().trim();
        // getting string value for the users personal email
        final String personalEmail = personalEmailEditText.getText().toString().trim();
        //getting string value for users phone number
        final String phone = phoneEditText.getText().toString().trim();

        // checking if emailEditText field is empty
        if (userEmail.isEmpty())
        {
            emailEditText.setError("Email is required");
            emailEditText.requestFocus();
            return;
        }
        // checking for a valid emailEditText
        if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches())
        {
            emailEditText.setError("Invalid Email ");
            emailEditText.requestFocus();
            return;

        }
        // checking if passwordEditText field is empty
        if (userPassword.isEmpty())
        {
            passwordEditText.setError("Password is required.");
            passwordEditText.requestFocus();
            return;
        }
        // checking if passwordEditText is at least 6 characters
        if (userPassword.length() < 6)
        {
            passwordEditText.setError("Password must be at least 6 characters");
            passwordEditText.requestFocus();
        }
        // checking if the passwords match
        if(!userPassword.equals(userConfirmedPassword))
        {
            confirmPasswordEditText.setError("Passwords do not match.");
            confirmPasswordEditText.requestFocus();
            return;
        }

        progress.setVisibility(View.VISIBLE);

        // If all fields are filled out correctly, account is being created
        mAuth.createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {

                progress.setVisibility(View.GONE);
                if (task.isSuccessful())
                {
                    Toast.makeText(getApplicationContext(), "Account Successfully Created.", Toast.LENGTH_SHORT).show();
                    FirebaseUser newUser = task.getResult().getUser();

                    //String with the Users UID
                    newUserUID = newUser.getUid();

                    createNewDocument(newUserUID, name,personalEmail,phone);
                    openActivityLogin();
                    finish();
                }
                else
                    Toast.makeText(getApplicationContext(), "Failed to create account. Please try again.", Toast.LENGTH_SHORT).show();

            }
        });


    }
    // Redirects back to login page once account is created
    public void openActivityLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    // creates a new document in "Users" collection with the new Users UID
    public void createNewDocument(String newUserUID, String name, String personalEmail, String phone)
    {
        Map<String, Object> user = new HashMap<>();
        user.put("Name", name);
        user.put("Email", personalEmail);
        user.put("Phone", phone);

        db.collection("users").document(newUserUID).set(user);
    }
}
