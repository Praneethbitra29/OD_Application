package com.example.apiiit_rkv.frontend;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.service.carrier.CarrierMessagingService;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.Manifest;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static android.os.Build.VERSION_CODES.M;
import static java.lang.Thread.sleep;

public class ContactActivity extends AppCompatActivity {
    private Button sendBtn;
    private EditText txtMessage;
    FirebaseFirestore firebaseFirestore;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing");

        sendBtn = (Button) findViewById(R.id.contactus_submit);
        txtMessage = (EditText) findViewById(R.id.contactus_editphonenumber);
        firebaseFirestore = FirebaseFirestore.getInstance();

        sendBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {

                progressDialog.show();
                Map<Object,String> contact = new HashMap<>();
                contact.put("number",txtMessage.getText().toString());
                firebaseFirestore.collection("Help").document(txtMessage.getText().toString()).set(contact)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                progressDialog.dismiss();
                                Toast.makeText(getApplication(),"Sended!....",Toast.LENGTH_LONG).show();
                                txtMessage.setText("");
                                try {
                                    sleep(1000);
                                }catch (Exception e) {
                                    e.printStackTrace();
                                }
                                Intent intent = new Intent(ContactActivity.this,MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(getApplication(),"Failed Try again!...",Toast.LENGTH_LONG).show();
                                txtMessage.setText("");
                            }
                        });

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ContactActivity.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
