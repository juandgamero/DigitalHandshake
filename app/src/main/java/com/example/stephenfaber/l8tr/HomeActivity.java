package com.example.stephenfaber.l8tr;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class HomeActivity extends AppCompatActivity {

    private Button btnReturn;
    private ImageView imageView;
    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();


        btnReturn = findViewById(R.id.btnReturn);
        imageView = findViewById(R.id.imageView);

        String text = "https://latermoblieapp.firebaseapp.com";

        text += ("/?text=" + userID);

//                Toast.makeText(getApplicationContext(), userID, Toast.LENGTH_SHORT).show();

        if(text != null){
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            try {
                BitMatrix bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, 800, 800);
                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
                imageView.setImageBitmap(bitmap);
            }
            catch (WriterException e){
                e.printStackTrace();
            }

        }

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // go to contacts activity
                openProfile();
            }
        });
    }

    public void openProfile(){
        Intent intent = new Intent(this, profile.class);
        startActivity(intent);
    }

}
