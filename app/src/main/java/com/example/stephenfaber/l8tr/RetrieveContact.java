package com.example.stephenfaber.l8tr;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class RetrieveContact extends AppCompatActivity
{
    // Need to find a way to get userUID from logging into the app. In this case, hardcoding it.
    private static String accountUID;
//    final private static String accountUID =  "EbhSoyB2DkNHqbjUp0acJ8X7w6X2";


    // Need to access the database
    final private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Creating an ArrayList to get document
    private List <Contact> listofContacts = new ArrayList <>();

    // Create Contact object
    Contact acontact;
    // Create path
    private CollectionReference contacts = db.collection("users/" + accountUID +"/contacts");

    // Interface shit
    ListView contactListView;

    // display contacts with updated fields;
    List<Contact> displayContact;

    List<String> displayString;

    //loading contact finish
    boolean finish;

    Button searchButton;
    EditText searchText;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve_contact);



        displayContact = new ArrayList<>();

        Intent intent = getIntent();
        accountUID =  FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        finish = intent.getBooleanExtra("finish", false);

        contactListView = findViewById(R.id.contactViewer);
        getContactIDs();

        searchButton = (Button) findViewById(R.id.sendInfo);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to contacts activity
                sendInfo();
            }
        });
        searchText = (EditText) findViewById(R.id.searhFilter);
        searchText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                searchContact();
                return false;
            }
        });


        contactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = displayContact.get(position).Name;
                String email = displayContact.get(position).Email;
                String phone = displayContact.get(position).Phone.toString();
                String accountID = accountUID;
                String contactID = displayContact.get(position).Id;

                // new intent to java class
                Intent intent = new Intent(getBaseContext(), ContactProfile.class);
                intent.putExtra("name", name);
                intent.putExtra("email", email);
                intent.putExtra("phone", phone);
                intent.putExtra("accountID", accountID);
                intent.putExtra("contactID", contactID);
                startActivity(intent);


            }
        });


    }

    // Need to Start Tyler's portion of the project
    public void sendInfo(){
        finish();
    }


    @Override
    public void onRestart() {
        super.onRestart();
        //When BACK BUTTON is pressed, the activity on the stack is restarted
        //Do what you want on the refresh procedure here
        listofContacts.clear();
        getContactIDs();
    }

    public void searchContact(){
        displayContact.clear();
        List<String> displayString = new ArrayList<>();
        boolean foundMatch = false;
        if(finish){
            String searchData = searchText.getText().toString().trim();

            for(Contact data : listofContacts){
                if(!(data.Name == null)){
                    if(data.Name.toUpperCase().contains(searchData.toUpperCase())) {
                        foundMatch = true;
                        displayString.add(data.Name);
                        displayContact.add(data);
                        continue;
                    }
                }

                if(!(data.Email == null)){
                    if(data.Email.toUpperCase().contains(searchData.toUpperCase())) {
                        foundMatch = true;
                        if (!(data.Name == null)) {
                            displayString.add(data.Name);
                            displayContact.add(data);
                            continue;
                        } else {
                            displayString.add(data.Email);
                            displayContact.add(data);
                            continue;
                        }
                    }
                }

                if(!(data.Phone == null)) {
                    if (data.Phone.toString().contains(searchData)) {
                        foundMatch = true;
                        if (!(data.Name == null)) {
                            displayString.add(data.Name);
                            displayContact.add(data);
                        } else if (!(data.Email == null)) {
                            displayString.add(data.Email);
                            displayContact.add(data);
                        } else {
                            displayString.add(data.Phone.toString());
                            displayContact.add(data);
                        }
                    }
                }

            }

            if(!foundMatch){
                Toast.makeText(RetrieveContact.this, searchData+" Not Found", Toast.LENGTH_SHORT).show();
            }

        } else{
            Toast.makeText(RetrieveContact.this, "No Contacts Available", Toast.LENGTH_SHORT).show();
        }
        ListAdapter listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, displayString);
        contactListView.setAdapter(listAdapter);
    }
    // Function to get all of the IDS
    public void getContactIDs(){

        // Go into the path and get this information.
        contacts.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                // Loop through the collection and get this information
                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots)
                {
                    acontact = documentSnapshot.toObject(Contact.class);
                    listofContacts.add(acontact);
                }
                createViewList();
            }
        }).addOnFailureListener(new OnFailureListener(){
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RetrieveContact.this, "No Contacts", Toast.LENGTH_SHORT).show();
            }
        });

    }

    // Populate Contact Objects into the ListView
    public void createViewList()
    {

        if(!finish) {
           Intent intent = new Intent(this, RetrieveContact.class);
            intent.putExtra("testUser", accountUID);
            intent.putExtra("finish", true);
            finish();
            startActivity(intent);

        }

        displayContact.clear();
        displayString = new ArrayList<>();
        for(int i=0; i < listofContacts.size(); i++){
            if(!(listofContacts.get(i).Name == null)){
                displayString.add(listofContacts.get(i).Name);
                displayContact.add(listofContacts.get(i));
            }else  if(!(listofContacts.get(i).Email == null)){
                displayString.add(listofContacts.get(i).Email);
                displayContact.add(listofContacts.get(i));
            }else  if(!(listofContacts.get(i).Phone == null)) {
                displayString.add(listofContacts.get(i).Phone.toString());
                displayContact.add(listofContacts.get(i));
            }
        }

        ListAdapter listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, displayString);
        contactListView.setAdapter(listAdapter);
    }

}
