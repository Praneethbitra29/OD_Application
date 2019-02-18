package com.example.apiiit_rkv.frontend;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import static android.app.PendingIntent.getActivity;

public class DocumentApplication extends AppCompatActivity {

    private Button submit,choosepic;
    private EditText pcnumber,password,studentname,fathername,dateofbirth,college,collegecode,mobile,email;
    private RadioGroup gender;
    private RadioButton genderbutton;
    private TextView applicationerror;
    private ImageView imageView;
    private Uri filepath;
    private final int PICK_IMAGE_REQUEST = 1;
    FirebaseFirestore firebaseFirestore;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_application);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing");
        submit = (Button)findViewById(R.id.submit);
        choosepic = (Button)findViewById(R.id.photo);
        imageView = (ImageView)findViewById(R.id.choose_photo);
        pcnumber = (EditText)findViewById(R.id.edit_pcnumber);
        password = (EditText)findViewById(R.id.edit_password);
        studentname = (EditText)findViewById(R.id.edit_studentname);
        fathername = (EditText)findViewById(R.id.edit_fathername);
        dateofbirth = (EditText)findViewById(R.id.edit_dob);
        college = (EditText)findViewById(R.id.edit_college);
        collegecode = (EditText)findViewById(R.id.edit_collegecode);
        mobile = (EditText)findViewById(R.id.edit_mobile);
        email = (EditText)findViewById(R.id.edit_email);
        gender = (RadioGroup)findViewById(R.id.gender_type);
        applicationerror = (TextView)findViewById(R.id.application_error);
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();
        SharedPreferences sharedPreferences = getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE);
        String user = sharedPreferences.getString("user","");
        pcnumber.setText(user);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();
                int selectgender = gender.getCheckedRadioButtonId();
                genderbutton = (RadioButton)findViewById(selectgender) ;
                SharedPreferences sharedPreferences = getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE);
                String user = sharedPreferences.getString("user","");
                if(pcnumber.getText().toString().equals(user) && !(pcnumber.getText().toString().equals(""))){
                    /*if(password.getText().toString().equals("") || studentname.getText().toString().equals("")
                            || fathername.getText().toString().equals("") || dateofbirth.getText().toString().equals("")
                            || college.getText().toString().equals("") || collegecode.getText().toString().equals("") ||
                            mobile.getText().toString().equals("") || email.getText().toString().equals("") ||
                            genderbutton.getText().toString().equals("")){

                        progressDialog.dismiss();
                        Toast.makeText(DocumentApplication.this,"Please Fill All The Fields..",Toast.LENGTH_LONG).show();
                        applicationerror.setText("Please Fill All The Fields..");
                        applicationerror.requestFocus();


                    }*/
                    if(password.getText().toString().equals("")){
                        progressDialog.dismiss();
                        password.setError("Invaild Field");
                        password.requestFocus();
                        return;
                    }
                    else if(studentname.getText().toString().equals("")){
                        progressDialog.dismiss();
                        studentname.setError("Invalid Field");
                        studentname.requestFocus();
                        return;
                    }
                    else if(fathername.getText().toString().equals("")){
                        progressDialog.dismiss();
                        fathername.setError("Invalid Field");
                        fathername.requestFocus();
                        return;
                    }
                    else if(dateofbirth.getText().toString().equals("")){
                        progressDialog.dismiss();
                        dateofbirth.setError("Invalid Field");
                        dateofbirth.requestFocus();
                        return;
                    }
                    else if(college.getText().toString().equals("")){
                        progressDialog.dismiss();
                        college.setError("Invalid Field");
                        college.requestFocus();
                        return;
                    }
                    else if(collegecode.getText().toString().equals("")){
                        progressDialog.dismiss();
                        collegecode.setError("Invalid Field");
                        collegecode.requestFocus();
                        return;
                    }
                    else if(mobile.getText().toString().equals("")){
                        progressDialog.dismiss();
                        mobile.setError("Invalid Field");
                        mobile.requestFocus();
                        return;
                    }
                    else if(email.getText().toString().equals("")){
                        progressDialog.dismiss();
                        email.setError("Invalid Field");
                        email.requestFocus();
                        return;
                    }
                    else if(genderbutton.getText().toString().equals("")){
                        progressDialog.dismiss();
                        genderbutton.requestFocus();
                        return;
                    }
                    else if(password.getText().toString().length() < 8){
                        progressDialog.dismiss();
                        password.setError("Password Length be minimun 8 characters");
                        password.requestFocus();
                        return;
                    }
                    else if(mobile.getText().toString().length() < 10){
                        progressDialog.dismiss();
                        mobile.setError("Invalid Field");
                        mobile.requestFocus();
                        return;

                    }
                    else if(!(email.getText().toString().contains("@gmail.com"))){
                        progressDialog.dismiss();
                        email.setError("Invalid MailId");
                        email.requestFocus();
                        return;
                    }
                    else{

                        if(filepath != null) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("password", password.getText().toString().trim());
                            editor.putString("studentname",studentname.getText().toString());
                            editor.putString("fathername",fathername.getText().toString());
                            editor.putString("dateofbirth",dateofbirth.getText().toString());
                            editor.putString("college",college.getText().toString());
                            editor.putString("collegecode",collegecode.getText().toString());
                            editor.putString("mobile",mobile.getText().toString());
                            editor.putString("email",email.getText().toString());
                            editor.putString("gender",genderbutton.getText().toString());
                            editor.commit();
                            StorageReference ref= storageReference.child("images/"+user);
                            ref.putFile(filepath)
                                    .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            progressDialog.dismiss();
                                            Intent intent = new Intent(getApplication(),DocumentAddressActivity.class);
                                            startActivity(intent);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    })
                                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                                        }
                                    });
                        }
                        else{
                            progressDialog.dismiss();
                            applicationerror.setText("Please Select a pic of Your's...");
                            applicationerror.requestFocus();
                            return;
                        }

                    }
                }
                else{
                    progressDialog.dismiss();
                    pcnumber.setError("Invalid Field");
                    pcnumber.requestFocus();
                    return;
                    /*progressDialog.dismiss();
                    Toast.makeText(DocumentApplication.this,"Invalid PC Number",Toast.LENGTH_LONG).show();
                    applicationerror.setText("Invalid PC Number");
                    applicationerror.requestFocus();*/
                }

            }
        });
        choosepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseimage();
            }
        });
    }

    private void chooseimage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filepath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filepath);
                imageView.setImageBitmap(bitmap);
            }catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onBackPressed() {

    }
}
