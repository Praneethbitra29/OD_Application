package com.example.apiiit_rkv.frontend;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Document;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.CONNECTIVITY_SERVICE;
import static android.support.constraint.Constraints.TAG;
import static android.support.v4.content.ContextCompat.getSystemService;

public class DocumentActivity extends Fragment {

    private Button submit;
    private EditText pcnumber,code;
    private TextView error;
    FirebaseFirestore firebaseFirestore;
    private ProgressDialog progressDialog;
    private ProgressBar mphonebar,mcodebar;
    private String mverification;
    RelativeLayout codelayout;
    private int btnType=0;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mcallbacks;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.activity_document, container, false);
        submit = (Button) root.findViewById(R.id.proceed);
        pcnumber = (EditText) root.findViewById(R.id.edit_homepcnumber);
        code = (EditText)root.findViewById(R.id.edit_verifycode);
        error = (TextView) root.findViewById(R.id.homeerror);
        mphonebar = (ProgressBar)root.findViewById(R.id.homepcnumberprogressbar);
        mcodebar = (ProgressBar)root.findViewById(R.id.verifyprogressbar);
        codelayout = (RelativeLayout)root.findViewById(R.id.codelayout);
        firebaseFirestore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setTimestampsInSnapshotsEnabled(true)
                .build();
        firebaseFirestore.setFirestoreSettings(settings);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Processing");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();
                if (pcnumber.getText().toString().trim().equals("")) {
                    progressDialog.dismiss();
                    error.setText("Please Enter the PC Number!..");
                    pcnumber.setText("");
                } else {
                    DocumentReference checkpcnumber = firebaseFirestore.collection("Applied").document(pcnumber.getText().toString().trim());
                    checkpcnumber.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot doc1 = task.getResult();
                                if (pcnumber.getText().toString().trim().equals(doc1.getString("pcnumber"))) {
                                    progressDialog.dismiss();
                                    error.setText("You already Applied Check Status in Status Tab!...");
                                    pcnumber.setText("");
                                } else {
                                    DocumentReference reference = firebaseFirestore.collection("Details").document(pcnumber.getText().toString().trim());
                                    reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if (task.isSuccessful()) {
                                                DocumentSnapshot doc2 = task.getResult();
                                                if (pcnumber.getText().toString().trim().equals(doc2.getString("pcnumber"))) {
                                                    if(doc2.getString("phonenumber")!=null) {
                                                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE);
                                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                                        editor.putString("user", pcnumber.getText().toString().trim());
                                                        editor.commit();
                                                        progressDialog.dismiss();
                                                        if (btnType == 0) {
                                                            mphonebar.setVisibility(View.VISIBLE);
                                                            pcnumber.setEnabled(false);
                                                            submit.setEnabled(false);
                                                            String phonenumber = "+91" + doc2.getString("phonenumber").trim();
                                                            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                                                    phonenumber,
                                                                    60,
                                                                    TimeUnit.SECONDS,
                                                                    TaskExecutors.MAIN_THREAD,
                                                                    mcallbacks
                                                            );
                                                        } else {

                                                            submit.setEnabled(false);
                                                            mcodebar.setVisibility(View.VISIBLE);
                                                            String verificationcode = code.getText().toString();
                                                            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mverification, verificationcode);
                                                            signInWithPhoneAuthCredential(credential);

                                                        }
                                                    }else {
                                                        progressDialog.dismiss();
                                                        error.setText("Your Details donot have a Phone number.Consult the College..");
                                                    }
                                                } else {
                                                    progressDialog.dismiss();
                                                    error.setText("Please Enter a vaild PC Number!..");
                                                    pcnumber.setText("");
                                                }
                                            }
                                        }
                                    });
                                }
                            }

                        }
                    });

                }

            }
        });

        mcallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                String sms = phoneAuthCredential.getSmsCode();
                if(sms != null){
                    code.setText(sms);
                    mcodebar.setVisibility(View.VISIBLE);
                    verifyCode(sms);
                }



            }

            @Override
            public void onVerificationFailed(FirebaseException e) {

                error.setText("There was some error in Verification.");

            }
            @Override
            public void onCodeSent(String verificationId,PhoneAuthProvider.ForceResendingToken token) {

                super.onCodeSent(verificationId, token);
                mverification = verificationId;

                btnType = 1;

                mphonebar.setVisibility(View.INVISIBLE);
                codelayout.setVisibility(View.VISIBLE);
                submit.setText("Verify Code");
                submit.setEnabled(true);

            }
        };

        return root;
    }



    private void signInWithPhoneAuthCredential (PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Intent intent = new Intent(getActivity(),DocumentApplication.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        }else {
                            Toast.makeText(getActivity(),task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mverification , code);
        signInWithPhoneAuthCredential(credential);
    }




}
