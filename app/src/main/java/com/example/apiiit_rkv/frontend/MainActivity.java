package com.example.apiiit_rkv.frontend;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.lang.annotation.Annotation;
import java.util.Timer;
import java.util.TimerTask;

public class   MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawer;
    Boolean doubleBackToExitPressedOnce = false;
    private long Backpressed;
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        writeStoragePermission();
        connection();





        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if(savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                    new HomeActivity()).commit();
            navigationView.setCheckedItem(R.id.home);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void writeStoragePermission() {

        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                REQUEST_CODE_ASK_PERMISSIONS);
                                    }
                    return;
                }


                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
            }
            return;
        }else {
            return;
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE_ASK_PERMISSIONS:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    return;
                }
                else {
                    Toast.makeText(this,"Permissions Denied",Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        }

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new HomeActivity()).commit();
                //doubleBackToExitPressedOnce = false;
                break;
            case R.id.document:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new DocumentActivity()).commit();
                //doubleBackToExitPressedOnce = true;
                break;
            case R.id.help:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new HelpActivity()).commit();
                //doubleBackToExitPressedOnce = true;
                break;
            case R.id.aboutus:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new AboutusActivity()).commit();
                //doubleBackToExitPressedOnce = true;
                break;
            case R.id.status:
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new StatusActivity()).commit();
                //doubleBackToExitPressedOnce = true;
                break;
            case R.id.share:
                //doubleBackToExitPressedOnce = true;
                String url = "https://www.google.com";
                Uri Webpage = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, Webpage);
                if(intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                }else {
                    Log.d("ImplicitIntents", "Can't handle this intent");
                }
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

   /* @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (doubleBackToExitPressedOnce) {
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container,
                        new HomeActivity()).commit();
                this.doubleBackToExitPressedOnce = false;

            } else {
                finish();
            }


        }
    }*/



        /*else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else if (!doubleBackToExitPressedOnce) {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this,"Please click BACK again to exit.", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
            super.onBackPressed();
            return;
        }*/

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else {
                finish();
        }
    }

   /* @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else {
            String t="";
            Intent intent = getIntent();
            Bundle bundle = intent.getExtras();
            if(bundle != null){
                if(!((String)bundle.get("tab")==null))
                    t = (String)bundle.get("tab");
            }
            if(t.equals("4")){
                Intent intent_main = new Intent(MainActivity.this,HomeActivity.class);
                intent.putExtra("tab","4");
                intent_main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent_main);
            }else {
                finish();
            }
        }
    }*/

    @Override
    protected void onStart() {
        super.onStart();
    }

    /*@Override
    public void onBackPressed() {

        if(Backpressed + 2000 > System.currentTimeMillis()){

            super.onBackPressed();
            return;
        }else {
            Toast.makeText(getBaseContext(),"Press again to Exit",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getBaseContext(),MainActivity.class);
            startActivity(intent);
        }
        Backpressed = System.currentTimeMillis();

    }*/

    @Override
    public void onDestroy() {
        super.onDestroy();
        FirebaseAuth.getInstance().signOut();
    }


        public void connection() {
            ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

            //For 3G check
            boolean is3g = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                    .isConnectedOrConnecting();
            //For WiFi Check
            boolean isWifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                    .isConnectedOrConnecting();


            if (!is3g && !isWifi) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("No Internet Access\nTurn on the Mobile Data or Connect to a wifi");
                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        finish();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
            return;
        }


}