package com.example.apiiit_rkv.frontend;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class DocumentStatusActivity extends AppCompatActivity {

    private Button gotohome;
    FirebaseFirestore firebaseFirestore;
    private ProgressDialog progressDialog;
    private int count;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_status);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing");
        progressDialog.show();
        gotohome = (Button)findViewById(R.id.documentstatushome);
        mAuth = FirebaseAuth.getInstance();
        final SharedPreferences sharedPreferences = getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE);
        final String user = sharedPreferences.getString("user","");
        firebaseFirestore = FirebaseFirestore.getInstance();
        DocumentReference count_getid = firebaseFirestore.collection("count").document("count");
        count_getid.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    count = Integer.parseInt(documentSnapshot.getString("count"));
                    count++;
                    Map<Object,String> count_set = new HashMap<>();
                    count_set.put("count",Integer.toString(count));
                    firebaseFirestore.collection("count").document("count").set(count_set);
                    Map<Object,String> document = new HashMap<>();
                    document.put("pcnumber",user);
                    document.put("password",sharedPreferences.getString("password",""));
                    document.put("studentname",sharedPreferences.getString("studentname",""));
                    document.put("fathername",sharedPreferences.getString("fathername",""));
                    document.put("dateofbirth",sharedPreferences.getString("dateofbirth",""));
                    document.put("college",sharedPreferences.getString("college",""));
                    document.put("collegecode",sharedPreferences.getString("collegecode",""));
                    document.put("mobile",sharedPreferences.getString("mobile",""));
                    document.put("email",sharedPreferences.getString("email",""));
                    document.put("gender",sharedPreferences.getString("gender",""));
                    document.put("houseno",sharedPreferences.getString("houseno",""));
                    document.put("streetname",sharedPreferences.getString("streetname",""));
                    document.put("locality",sharedPreferences.getString("locality",""));
                    document.put("city",sharedPreferences.getString("city",""));
                    document.put("state",sharedPreferences.getString("state",""));
                    document.put("pincode",sharedPreferences.getString("pincode",""));
                    document.put("type",sharedPreferences.getString("type",""));
                    document.put("amount",sharedPreferences.getString("amount",""));
                    document.put("status",sharedPreferences.getString("status",""));
                    document.put("details",sharedPreferences.getString("details",""));
                    document.put("orderid",Integer.toString(count));

                    Map<Object,String> applied = new HashMap<>();
                    applied.put("pcnumber",user);
                    firebaseFirestore.collection("Applied").document(user).set(applied);
                    firebaseFirestore.collection("Application").document(Integer.toString(count)).set(document)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    progressDialog.dismiss();
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DocumentStatusActivity.this);
                                    alertDialogBuilder.setMessage("Your Application is successfully Registered....\nYour Order Id is "+count
                                    +"\nDownload the application now...");
                                    alertDialogBuilder.setPositiveButton("Download", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which)  {

                                            File folder = new File(Environment.getExternalStorageDirectory().toString());
                                            File pdfFile = new File(folder.getAbsolutePath(),user+"_"+Integer.toString(count)+"_Application.pdf");
                                            try {
                                                OutputStream outputStream = new FileOutputStream(pdfFile);
                                                Document application = new Document();
                                                PdfWriter.getInstance(application,outputStream);
                                                application.open();
                                                PdfPTable table = new PdfPTable(2);
                                                String[] names = {"OrderID","PC Number","Student Name","Father Name","Date Of Birth"
                                                ,"Gender","EMail","Mobile Number","College","College Code","Type","Amount"};
                                                String[] values = {Integer.toString(count),user,sharedPreferences.getString("studentname",""),sharedPreferences.getString("fathername",""),
                                                        sharedPreferences.getString("dateofbirth",""),sharedPreferences.getString("gender",""),
                                                        sharedPreferences.getString("email",""),sharedPreferences.getString("mobile",""),
                                                        sharedPreferences.getString("college",""),sharedPreferences.getString("collegecode",""),
                                                        sharedPreferences.getString("type",""),sharedPreferences.getString("amount","")};
                                                Font fonttext = new Font(Font.FontFamily.TIMES_ROMAN,15,Font.NORMAL);
                                                for(int i=0;i<12;i++){

                                                    PdfPCell cell1 = new PdfPCell(new Phrase(names[i],fonttext));
                                                    cell1.setPadding(5);
                                                    cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                                                    cell1.setBorderColor(BaseColor.WHITE);
                                                    table.addCell(cell1);
                                                    PdfPCell cell2 = new PdfPCell(new Phrase(values[i],fonttext));
                                                    cell2.setPadding(5);
                                                    cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                                                    cell2.setBorderColor(BaseColor.WHITE);
                                                    table.addCell(cell2 );
                                                }
                                                PdfPTable titleTable = new PdfPTable(1);
                                                Font font = new Font(Font.FontFamily.TIMES_ROMAN,30,Font.NORMAL);
                                                PdfPCell cell = new PdfPCell(new Phrase("RGUKT OD Application",font));
                                                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                                                cell.setPadding(30);
                                                cell.setPaddingBottom(50);
                                                cell.setBorderColor(BaseColor.WHITE);
                                                cell.setBorder(Rectangle.BOTTOM);
                                                titleTable.addCell(cell);
                                                PdfPTable delaration = new PdfPTable(1);
                                                Font fontDeclare = new Font(Font.FontFamily.TIMES_ROMAN,20,Font.NORMAL);


                                                PdfPCell cellDelaractiontitle = new PdfPCell(new Phrase("Declaration",fontDeclare));
                                                cellDelaractiontitle.setPaddingTop(30);
                                                cellDelaractiontitle.setHorizontalAlignment(Element.ALIGN_CENTER);
                                                cellDelaractiontitle.setBorderColor(BaseColor.WHITE);


                                                PdfPCell cellDelaraction = new PdfPCell(new Phrase("I here by declare the details mentioned above are true.",fonttext));
                                                cellDelaraction.setBorderColor(BaseColor.WHITE);
                                                cellDelaraction.setHorizontalAlignment(Element.ALIGN_CENTER);
                                                cellDelaraction.setPaddingTop(20);


                                                delaration.addCell(cellDelaractiontitle);
                                                delaration.addCell(cellDelaraction);
                                                application.add(titleTable);
                                                application.add(table);
                                                application.add(delaration);
                                                application.close();
                                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(DocumentStatusActivity.this);
                                                alertDialog.setMessage("Application Saved to Your Internal Storage..");
                                                alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                    }
                                                });
                                                AlertDialog alert = alertDialog.create();
                                                alert.show();


                                            }catch (Exception e) {
                                                e.printStackTrace();
                                            }




                                        }
                                    });
                                    AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                }
                            });
                }
            }
        });
        gotohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth.signOut();
                sentToAuth();
            }
        });
    }

    private void sentToAuth() {
        Intent intent = new Intent(DocumentStatusActivity.this,MainActivity.class);
        intent.addFlags(intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

    }

}
