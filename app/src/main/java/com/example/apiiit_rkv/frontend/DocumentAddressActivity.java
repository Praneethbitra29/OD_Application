package com.example.apiiit_rkv.frontend;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class DocumentAddressActivity extends AppCompatActivity implements View.OnClickListener{


    private EditText houseno,streetname,locality,city,state,pincode;
    private Button proceed;
    private TextView error;
    FirebaseFirestore firebaseFirestore;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_address);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing");
        houseno = (EditText)findViewById(R.id.edit_houseno);
        streetname = (EditText)findViewById(R.id.edit_streetname);
        locality = (EditText)findViewById(R.id.edit_locality);
        city = (EditText)findViewById(R.id.edit_city);
        state = (EditText)findViewById(R.id.edit_state);
        pincode = (EditText)findViewById(R.id.edit_pincode);
        error = (TextView)findViewById(R.id.address_error);
        proceed = (Button)findViewById(R.id.addresssubmit);
        firebaseFirestore = FirebaseFirestore.getInstance();
        proceed.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        progressDialog.show();
        /*if(houseno.getText().toString().equals("") || streetname.getText().toString().equals("") ||
                locality.getText().toString().equals("") || city.getText().toString().equals("") ||
                state.getText().toString().equals("") || pincode.getText().toString().equals("")){

            progressDialog.dismiss();
            Toast.makeText(this,"Please Fill All The Fields...",Toast.LENGTH_LONG).show();
            error.setText("Please Fill All The Fields...");

        }*/
        if(houseno.getText().toString().equals("")){
            progressDialog.dismiss();
            houseno.setError("Invalid Field");
            houseno.requestFocus();
            return;
        }
        else if(streetname.getText().toString().equals("")){
            progressDialog.dismiss();
            streetname.setError("Invalid Field");
            streetname.requestFocus();
            return;
        }
        else if(locality.getText().toString().equals("")){
            progressDialog.dismiss();
            locality.setError("Invalid Field");
            locality.requestFocus();
            return;
        }
        else if(city.getText().toString().equals("")){
            progressDialog.dismiss();
            city.setError("Invalid Field");
            city.requestFocus();
            return;
        }
        else if(state.getText().toString().equals("")){
            progressDialog.dismiss();
            state.setError("Invalid Field");
            state.requestFocus();
            return;
        }
        else if(pincode.getText().toString().equals("")){
            progressDialog.dismiss();
            pincode.setError("Invalid Field");
            pincode.requestFocus();
            return;
        }
        else if(!(pincode.getText().toString().length() == 6)){
            progressDialog.dismiss();
            pincode.setError("Invalid Field");
            pincode.requestFocus();
            return;
        }
        else {

            SharedPreferences sharedPreferences = getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("houseno",houseno.getText().toString().trim());
            editor.putString("streetname",streetname.getText().toString().trim());
            editor.putString("locality",locality.getText().toString().trim());
            editor.putString("city",city.getText().toString().trim());
            editor.putString("state",state.getText().toString().trim());
            editor.putString("pincode",pincode.getText().toString().trim());
            editor.commit();
            progressDialog.dismiss();
            Intent intent = new Intent(getApplication(),DocumentAmountActivity.class);
            startActivity(intent);
        }

    }

    @Override
    public void onBackPressed() {

    }
}

