package com.example.apiiit_rkv.frontend;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class PaymentActivity extends AppCompatActivity {

    private Button done;
    FirebaseFirestore firebaseFirestore;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing");
        done = (Button)findViewById(R.id.paymentdone);
        firebaseFirestore = FirebaseFirestore.getInstance();
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                SharedPreferences sharedPreferences = getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("status", "IN PROCESS....");
                editor.putString("details", "");
                editor.commit();
                progressDialog.dismiss();
                Intent intent = new Intent(PaymentActivity.this,DocumentStatusActivity.class);
                startActivity(intent);


            }
        });
    }
    @Override
    public void onBackPressed() {

    }
}
