package com.example.apiiit_rkv.frontend;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import static android.os.Build.VERSION_CODES.M;

public class StatusActivity extends Fragment {

    FirebaseFirestore firebaseFirestore;
    private EditText pcnumber,password;
    private Button getstatus;
    private TextView error;
    private ProgressDialog progressDialog;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_status,container,false);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Processing");
        pcnumber = (EditText)root.findViewById(R.id.status_edit_pcnumber);
        password = (EditText)root.findViewById(R.id.status_edit_password);
        getstatus = (Button)root.findViewById(R.id.getstatus);
        error = (TextView)root.findViewById(R.id.statuserror);
        firebaseFirestore = FirebaseFirestore.getInstance();
        getstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                if(pcnumber.getText().toString().trim().equals("") && password.getText().toString().trim().equals("")){
                    progressDialog.dismiss();
                    error.setText("Please fill all the Fields");
                }
                else {
                    DocumentReference result_pcnumber = firebaseFirestore.collection("Application").document(pcnumber.getText().toString().trim());
                    result_pcnumber.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                DocumentSnapshot doc = task.getResult();
                                if(pcnumber.getText().toString().trim().equals(doc.getString("orderid"))){
                                    DocumentReference result_password = firebaseFirestore.collection("Application").document(pcnumber.getText().toString().trim());
                                    result_password.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if(task.isSuccessful()) {
                                                DocumentSnapshot doc = task.getResult();
                                                if(password.getText().toString().equals(doc.getString("password"))) {
                                                    DocumentReference result_status = firebaseFirestore.collection("Application").document(pcnumber.getText().toString().trim());
                                                    result_status.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                            if(task.isSuccessful()) {
                                                                DocumentSnapshot doc = task.getResult();
                                                                StringBuilder status = new StringBuilder("");
                                                                status.append(doc.getString("status")).append("\n").append(doc.getString("details"));
                                                                progressDialog.dismiss();
                                                                error.setText(status);
                                                                password.setText("");
                                                                pcnumber.setText("");
                                                            }

                                                        }
                                                    });
                                                } else {
                                                    progressDialog.dismiss();
                                                    error.setText("Invalid Password");
                                                    password.setText("");
                                                    pcnumber.setText("");
                                                }
                                            }
                                        }
                                    });
                                }else {
                                    progressDialog.dismiss();
                                    error.setText("Invalid Order Number");
                                    password.setText("");
                                    pcnumber.setText("");
                                }
                            }
                        }
                    });

                }

            }
        });

        return root;
    }
}
