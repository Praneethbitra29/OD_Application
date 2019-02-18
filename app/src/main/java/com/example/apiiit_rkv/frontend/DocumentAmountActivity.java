package com.example.apiiit_rkv.frontend;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DocumentAmountActivity extends AppCompatActivity {

    private Button pre,in,post,pay;
    private TextView amountview,due,total,error;
    private String odamount="1000",type="",amount="";
    FirebaseFirestore firebaseFirestore;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_amount);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing");
        pre = (Button)findViewById(R.id.odPreconvaction);
        in = (Button)findViewById(R.id.odinconvaction);
        post = (Button)findViewById(R.id.odpostconvaction);
        pay = (Button)findViewById(R.id.odpay);
       amountview = (TextView)findViewById(R.id.viewamount);
       due = (TextView)findViewById(R.id.viewdue);
       total = (TextView)findViewById(R.id.viewtotalamount);
        error = (TextView)findViewById(R.id.amounterror);
       firebaseFirestore = FirebaseFirestore.getInstance();
       pay.setEnabled(false);

        pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amountview.setText(odamount);
                due.setText("0");
                type="Pre Convacation";
                amount="1000";
                total.setText(amount);
                pay.setEnabled(true);
            }
        });
        in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amountview.setText(odamount);
                due.setText("100");
                type="In Convacation";
                amount="1100";
                total.setText(amount);
                pay.setEnabled(true);

            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                amountview.setText(odamount);
                due.setText("200");
                type="Post Convacation";
                amount="1200";
                total.setText(amount);
                pay.setEnabled(true);

            }
        });
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();

                if(type.equals("") || amount.equals(""))
                {

                    progressDialog.dismiss();
                    Toast.makeText(getApplication(),"Please Select a Type...",Toast.LENGTH_LONG).show();
                    error.setText("Please Select a Type...");
                }
                else {

                    SharedPreferences sharedPreferences = getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("type", type);
                    editor.putString("amount", amount);
                    editor.commit();
                    progressDialog.dismiss();
                    Intent intent = new Intent(DocumentAmountActivity.this, PaymentActivity.class);
                    startActivity(intent);
                }

            }
        });


    }

    @Override
    public void onBackPressed() {

    }
}
