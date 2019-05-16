package com.example.stephenfaber.l8tr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class profile extends AppCompatActivity{

    /*
    @BindView(R.id.text_current_sort_by)
    TextView mCurrentSortByView;
    */

    //keep track of checked fields Name, Email, Phone USE THIS TO GET CURRENT STATE
    boolean[] checked = new boolean[3];
    HashMap<String, Object> selectedMap = new HashMap<String, Object>(10);

    Switch switchName;
    Switch switchEmail;
    Switch switchPhone;
    EditText nameField;
    EditText phoneField;
    EditText emailField;
    Button generateButton;
    Button contact;
    Button logout;
    String userUID;

    FirebaseFirestore db = FirebaseFirestore.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        // getting the userUID
        userUID = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();



        switchName = findViewById(R.id.switchName);
        switchEmail = findViewById(R.id.switchEmail);
        switchPhone= findViewById(R.id.switchPhone);
        nameField = findViewById(R.id.nameField);
        phoneField = findViewById(R.id.phoneField);
        emailField= findViewById(R.id.emailField);
        generateButton= findViewById(R.id.generate);
        contact = findViewById(R.id.contact);
        logout = findViewById(R.id.logout);

        switchName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if(isChecked){
                    Toast.makeText(getApplicationContext(), "ON", Toast.LENGTH_LONG).show();
                    checked[0] = true;
                    selectedMap.put("NameSelect", "1");
                    toDatabase(selectedMap);
                }else{
                    Toast.makeText(getApplicationContext(), "OFF", Toast.LENGTH_LONG).show();
                    checked[0] = false;
                    selectedMap.put("NameSelect", "0");
                    toDatabase(selectedMap);
                }
            }
        });
        switchEmail.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if(isChecked){
                    Toast.makeText(getApplicationContext(), "ON", Toast.LENGTH_LONG).show();
                    checked[1] = true;
                    selectedMap.put("EmailSelect", "1");
                    toDatabase(selectedMap);
                }else{
                    Toast.makeText(getApplicationContext(), "OFF", Toast.LENGTH_LONG).show();
                    checked[1] = false;
                    selectedMap.put("EmailSelect", "0");
                    toDatabase(selectedMap);
                }
            }
        });
        switchPhone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub`
                if(isChecked){
                    Toast.makeText(getApplicationContext(), "ON", Toast.LENGTH_LONG).show();
                    checked[2] = true;
                    selectedMap.put("PhoneSelect", "1");
                    toDatabase(selectedMap);
                }else{
                    Toast.makeText(getApplicationContext(), "OFF", Toast.LENGTH_LONG).show();
                    checked[2] = false;
                    selectedMap.put("PhoneSelect", "0");
                    toDatabase(selectedMap);
                }
            }
        });
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // call the save change function
                if(!nameField.getText().toString().equals("")){
                    selectedMap.put("Name", nameField.getText());
                }
                if(!phoneField.getText().toString().equals("")){
                    selectedMap.put("Phone", phoneField.getText());
                }
                if(!emailField.getText().toString().equals("")){
                    selectedMap.put("Email", emailField.getText());
                }
                toDatabase(selectedMap);

                homeActivity();
            }
        });

        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contactActivity();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        readDatabase();
    }

    public void contactActivity(){
        // go to contact activity
        Intent intent = new Intent(this, RetrieveContact.class);
        intent.putExtra("finish", false);
        startActivity(intent);
    }

    public void homeActivity(){
        // go to contact activity
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }

    public void logout(){
        // go to contact activity
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


    //reads from the database and stores it in the global hashMap selectedMap and boolean array and in buttons
    public void readDatabase() {

        DocumentReference docRef = db.collection("users").document(userUID);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        Map<String, Object> tempMap = document.getData();
                        selectedMap = new HashMap<String, Object>(tempMap);

                        //read switch values from database
                        if (selectedMap.containsKey("NameSelect")) {
                            if (selectedMap.get("NameSelect").equals("1")) {
                                switchName.setChecked(true);
                                checked[0] = true;
                            } else {
                                switchName.setChecked(false);
                                checked[0] = false;
                            }
                        }
                        if (selectedMap.containsKey("EmailSelect")) {
                            if (selectedMap.get("EmailSelect").equals("1")) {
                                switchEmail.setChecked(true);
                                checked[1] = true;
                            } else {
                                switchEmail.setChecked(false);
                                checked[1] = false;
                            }
                        }
                        if (selectedMap.containsKey("PhoneSelect")) {
                            if (selectedMap.get("PhoneSelect").equals("1")) {
                                switchPhone.setChecked(true);
                                checked[2] = true;
                            } else {
                                switchPhone.setChecked(false);
                                checked[2] = false;
                            }
                        }
                        //send field values to database
                        if (selectedMap.containsKey("Name")) {
                            nameField.setText(selectedMap.get("Name").toString());
                        }
                        if (selectedMap.containsKey("Phone")) {
                            phoneField.setText(selectedMap.get("Phone").toString());
                        }
                        if (selectedMap.containsKey("Email")) {
                            emailField.setText(selectedMap.get("Email").toString());
                        }

                    } else {
                        Log.d("readDatabase()", "No such document");
                    }
                }else {
                    Log.d("readDatabase()", "get failed with ", task.getException());
                }
            }
        });
    }
    public void toDatabase(HashMap<String, Object> selectedMap){
        readFields();

        DocumentReference docRef = db.collection("users").document(userUID);
        docRef.set(selectedMap,SetOptions.merge());
    }
    public void readFields(){
        if(!nameField.getText().toString().equals("")){
            selectedMap.put("Name", nameField.getText().toString());
        }
        if(!phoneField.getText().toString().equals("")){
            selectedMap.put("Phone", phoneField.getText().toString());
        }
        if(!emailField.getText().toString().equals("")){
            selectedMap.put("Email", emailField.getText().toString());
        }
    }
}
