package com.aditya.marquee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class MarqueeHomeActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;

    ImageView expandImageView, profileCircleImageView;
    RelativeLayout optionsRelativeLayout, featuresRelativeLayout, menuLayout;
    Boolean expandFlag;
    ValueEventListener valueEventListener;
    DatabaseReference databaseReference;
    TextView welcomeTextView, yourProfileTextView, settingsTextView, aboutTextView, logOutTextView;
    Intent intent;
    SharedPreferences sharedpreferences;
    Random random;
    int randomImagePick = 0;
    Button logoDetectionButton, logoGenerationButton, editLogoButton, moreButton;
    RecentActivityAdapter recentActivityAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<ActivityData> activityDataArrayList;
    ActivityData activityData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marquee_home);

        verifyPermissions();
        expandFlag = true;
        random = new Random();


        sharedpreferences = getSharedPreferences("ImageSharedPreference", Context.MODE_PRIVATE);
        profileCircleImageView = findViewById(R.id.userProfile);

        if(sharedpreferences.getString("ImageTag", "").toString().equals("")){
            randomImagePick = random.nextInt(6) + 1;
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("ImageTag", String.valueOf(randomImagePick));
            editor.commit();
        }else{
            if(sharedpreferences.getString("ImageTag", "").toString().equals("1")){
                profileCircleImageView.setImageResource(R.drawable.dogpfp);
            }else if(sharedpreferences.getString("ImageTag", "").toString().equals("2")){
                profileCircleImageView.setImageResource(R.drawable.dogaltpfp);
            }else if(sharedpreferences.getString("ImageTag", "").toString().equals("3")){
                profileCircleImageView.setImageResource(R.drawable.dogonepfp);
            }else if(sharedpreferences.getString("ImageTag", "").toString().equals("4")){
                profileCircleImageView.setImageResource(R.drawable.catpfp);
            }else if(sharedpreferences.getString("ImageTag", "").toString().equals("5")){
                profileCircleImageView.setImageResource(R.drawable.cataltpfp);
            }else  if(sharedpreferences.getString("ImageTag", "").toString().equals("6")){
                profileCircleImageView.setImageResource(R.drawable.catonepfp);
            }else{
                profileCircleImageView.setImageResource(R.drawable.defaultuser);
            }
        }



        expandImageView = (ImageView) findViewById(R.id.expandImageView);
        optionsRelativeLayout = (RelativeLayout) findViewById(R.id.optionsRelativeLayout);
        featuresRelativeLayout = (RelativeLayout) findViewById(R.id.featuresRelativeLayout);
        menuLayout = (RelativeLayout) findViewById(R.id.menuLayout);
        welcomeTextView = (TextView) findViewById(R.id.welcomeTextView);
        yourProfileTextView = (TextView) findViewById(R.id.yourProfileTextView);
        aboutTextView = (TextView) findViewById(R.id.aboutTextView);
        logOutTextView = (TextView) findViewById(R.id.logOutTextView);
        logoDetectionButton = (Button) findViewById(R.id.logoDetectionButton);
        logoGenerationButton = (Button) findViewById(R.id.logoGenerationButton);
        editLogoButton = (Button) findViewById(R.id.editLogoButton);
        moreButton = (Button) findViewById(R.id.moreButton);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        activityDataArrayList = new ArrayList<>();
        RecyclerView receiptRecyclerView = (RecyclerView) findViewById(R.id.activityRecyclerView);

        layoutManager = new LinearLayoutManager(this);
        recentActivityAdapter = new RecentActivityAdapter(this, activityDataArrayList);
        receiptRecyclerView.setLayoutManager(layoutManager);
        receiptRecyclerView.setAdapter(recentActivityAdapter);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://marquee-411f8-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("RecentActivity").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        ValueEventListener valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                activityDataArrayList.clear();
                for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                    activityData = new ActivityData(itemSnapshot.child("ActivityName").getValue().toString(), "Marquee v0.1", R.drawable.index);
                    activityDataArrayList.add(activityData);
                }
                recentActivityAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        expandImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(expandFlag){
                    expandImageView.setImageResource(R.drawable.ic_expand_less);
                    optionsRelativeLayout.setVisibility(View.VISIBLE);
                    params.addRule(RelativeLayout.BELOW, R.id.optionsRelativeLayout);
                    featuresRelativeLayout.setLayoutParams(params);
                    expandFlag = false;
                }else{
                    expandImageView.setImageResource(R.drawable.ic_expand);
                    optionsRelativeLayout.setVisibility(View.INVISIBLE);
                    params.addRule(RelativeLayout.BELOW, R.id.menuLayout);
                    featuresRelativeLayout.setLayoutParams(params);
                    expandFlag = true;
                }

            }
        });

        databaseReference = FirebaseDatabase.getInstance("https://marquee-411f8-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("userName");
        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                welcomeTextView.setText("Hello, "+ dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        yourProfileTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MarqueeHomeActivity.this, UserProfileActivity.class);
                startActivity(intent);
            }
        });


        aboutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MarqueeHomeActivity.this)
                        .setTitle("About")
                        .setMessage("Marquee v0.1 (Early Access) is an application which let's you do some stuff")
                        .setCancelable(false)
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setNegativeButton("Give Feedback", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    Intent intent = new Intent(Intent.ACTION_SEND);
                                    intent.setType("*/*");
                                    intent.putExtra(android.content.Intent.EXTRA_EMAIL,new String[] { "adityabalsane10@gmail.com" });
                                    intent.putExtra(Intent.EXTRA_SUBJECT, "Marquee feedback");
                                    if (intent.resolveActivity(getPackageManager()) != null) {
                                        Toast.makeText(MarqueeHomeActivity.this, "Thanks for your feedback!", Toast.LENGTH_SHORT).show();
                                        startActivity(intent);
                                    }
                                } catch(Exception e) {
                                    Toast.makeText(MarqueeHomeActivity.this, "Sorry! ShopLens couldn't find an mail app on your device :(", Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
                            }
                        })
                        .show();
            }
        });

        logOutTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MarqueeHomeActivity.this)
                        .setTitle("Alert!")
                        .setMessage("Do you want to logout?")
                        .setCancelable(false)
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseAuth.getInstance().signOut();
                                intent = new Intent(MarqueeHomeActivity.this, AuthenticationActivity.class);
                                startActivity(intent);
                            }
                        }).show();
            }
        });

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        logoDetectionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));

                FirebaseDatabase.getInstance("https://marquee-411f8-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("RecentActivity").child(mAuth.getCurrentUser().getUid()).child(timeStamp).child("ActivityName").setValue("Logo Detection");
                intent = new Intent(MarqueeHomeActivity.this, LogoDetectionActivity.class);
                startActivity(intent);
            }
        });

        logoGenerationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));

                FirebaseDatabase.getInstance("https://marquee-411f8-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("RecentActivity").child(mAuth.getCurrentUser().getUid()).child(timeStamp).child("ActivityName").setValue("Logo Generation");

                intent = new Intent(MarqueeHomeActivity.this, LogoGenrationActivity.class);
                startActivity(intent);
            }
        });

        editLogoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));

                FirebaseDatabase.getInstance("https://marquee-411f8-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("RecentActivity").child(mAuth.getCurrentUser().getUid()).child(timeStamp).child("ActivityName").setValue("Cloud Saves");
                intent = new Intent(MarqueeHomeActivity.this, CloudSavesActivity.class);
                startActivity(intent);
            }
        });

        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MarqueeHomeActivity.this)
                        .setMessage("This part is under development...")
                        .setCancelable(false)
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();
            }
        });


    }

    private void verifyPermissions(){
        String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
                Manifest.permission.GET_ACCOUNTS,
                Manifest.permission.ACCESS_NOTIFICATION_POLICY};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[0]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[1]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[2]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[3]) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this.getApplicationContext(),
                permissions[4]) == PackageManager.PERMISSION_GRANTED){
        }else{
            ActivityCompat.requestPermissions(MarqueeHomeActivity.this, permissions, REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        verifyPermissions();
    }
}