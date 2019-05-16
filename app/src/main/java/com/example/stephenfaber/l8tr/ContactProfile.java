package com.example.stephenfaber.l8tr;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ContactProfile extends AppCompatActivity {
    // textView fields
    EditText nameTV;
    EditText emailTV;
    EditText phoneTV;

    // information of the contact
    String accountUID, contactUID;

    // button
    Button saveChanges;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_profile);
        nameTV = (EditText) findViewById(R.id.nameField);
        emailTV = (EditText) findViewById(R.id.emailField);
        phoneTV = (EditText) findViewById(R.id.phoneField);

        // put the contact information in the text view fields
        nameTV.setText(getIntent().getStringExtra("name"));
        emailTV.setText(getIntent().getStringExtra("email"));
        phoneTV.setText(getIntent().getStringExtra("phone"));

        // this will be used to find the contact so we can update the contact information
        accountUID = getIntent().getStringExtra("accountID");
        contactUID = getIntent().getStringExtra("contactID");

        // Initializing button
        saveChanges = (Button) findViewById(R.id.saveChangesButton);

        // on click listener for the save changes button
        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // call the save change function
                saveChanges();
            }
        });
    }

    // save changes function
    public void saveChanges(){
        Map<String, Object> docData = new HashMap<>();
        docData.put("Id", contactUID);
        if(!(nameTV.getText().toString().trim().isEmpty()))
            docData.put("Name", nameTV.getText().toString());
        if(!(emailTV.getText().toString().trim().isEmpty()))
            docData.put("Email", emailTV.getText().toString());
        if(!(phoneTV.getText().toString().trim().isEmpty()))
            docData.put("Phone", phoneTV.getText().toString());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference contact = db.document("users/" + accountUID +"/contacts/"+contactUID);

        contact.set(docData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(ContactProfile.this, "Contact Updated", Toast.LENGTH_SHORT).show();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ContactProfile.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}