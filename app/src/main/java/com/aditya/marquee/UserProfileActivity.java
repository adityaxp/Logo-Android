package com.aditya.marquee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {

    Toolbar userProfileToolBar;
    CircleImageView userImageRound, userProfile1, userProfile2, userProfile3, userProfile4, userProfile5, userProfile6;
    SharedPreferences sharedpreferences;
    ImageButton updateImageButton, editEmailButton, editProfileNameButton;
    EditText userNameEditText, emailEditText;
    ValueEventListener valueEventListener;
    DatabaseReference databaseReference;
    Button saveProfileButton;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        userProfileToolBar =  findViewById(R.id.userProfileToolBar);

        setSupportActionBar(userProfileToolBar);
        setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        userProfileToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        userImageRound = (CircleImageView) findViewById(R.id.userImageRound);
        userProfile1 = (CircleImageView) findViewById(R.id.userProfile1);
        userProfile2 = (CircleImageView) findViewById(R.id.userProfile2);
        userProfile3 = (CircleImageView) findViewById(R.id.userProfile3);
        userProfile4 = (CircleImageView) findViewById(R.id.userProfile4);
        userProfile5 = (CircleImageView) findViewById(R.id.userProfile5);
        userProfile6 = (CircleImageView) findViewById(R.id.userProfile6);

        userNameEditText = (EditText) findViewById(R.id.userNameEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);

        editEmailButton = (ImageButton) findViewById(R.id.editEmailButton);
        editProfileNameButton = (ImageButton) findViewById(R.id.editProfileNameButton);
        updateImageButton = (ImageButton) findViewById(R.id.updateImageButton);

        saveProfileButton = (Button) findViewById(R.id.saveProfileButton);

        sharedpreferences = getSharedPreferences("ImageSharedPreference", Context.MODE_PRIVATE);

        setupProfileImage();

        databaseReference = FirebaseDatabase.getInstance("https://marquee-411f8-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userNameEditText.setText(dataSnapshot.child("userName").getValue().toString());
                emailEditText.setText(dataSnapshot.child("userEmail").getValue().toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(UserProfileActivity.this, "Failed to retrieve profile data", Toast.LENGTH_SHORT).show();
            }
        });

        userProfile1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetImageBorders();
                userImageRound.setImageResource(R.drawable.dogpfp);
                userProfile1.setBorderWidth(8);
                userProfile1.setBorderColor(Color.parseColor("#00ff00"));
            }
        });

        userProfile2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetImageBorders();
                userImageRound.setImageResource(R.drawable.dogaltpfp);
                userProfile2.setBorderWidth(8);
                userProfile2.setBorderColor(Color.parseColor("#00ff00"));
            }
        });

        userProfile3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetImageBorders();
                userImageRound.setImageResource(R.drawable.dogonepfp);
                userProfile3.setBorderWidth(8);
                userProfile3.setBorderColor(Color.parseColor("#00ff00"));
            }
        });

        userProfile4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetImageBorders();
                userImageRound.setImageResource(R.drawable.catpfp);
                userProfile4.setBorderWidth(8);
                userProfile4.setBorderColor(Color.parseColor("#00ff00"));
            }
        });

        userProfile5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetImageBorders();
                userImageRound.setImageResource(R.drawable.cataltpfp);
                userProfile5.setBorderWidth(8);
                userProfile5.setBorderColor(Color.parseColor("#00ff00"));
            }
        });

        userProfile6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetImageBorders();
                userImageRound.setImageResource(R.drawable.catonepfp);
                userProfile6.setBorderWidth(8);
                userProfile6.setBorderColor(Color.parseColor("#00ff00"));
            }
        });

        editProfileNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userNameEditText.setEnabled(true);
            }
        });

        editEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailEditText.setEnabled(true);
            }
        });

        saveProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(UserProfileActivity.this)
                        .setMessage("This part is under development...")
                        .setCancelable(false)
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                userNameEditText.setEnabled(false);
                                emailEditText.setEnabled(false);
                            }
                        }).show();            }
        });

        updateImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(v);
            }
        });



    }

    public void setupProfileImage(){

        userProfile1.setImageResource(R.drawable.dogpfp);
        userProfile2.setImageResource(R.drawable.dogaltpfp);
        userProfile3.setImageResource(R.drawable.dogonepfp);
        userProfile4.setImageResource(R.drawable.catpfp);
        userProfile5.setImageResource(R.drawable.cataltpfp);
        userProfile6.setImageResource(R.drawable.catonepfp);

        if(sharedpreferences.getString("ImageTag", "").toString().equals("1")){
            userImageRound.setImageResource(R.drawable.dogpfp);
            userProfile1.setBorderWidth(8);
            userProfile1.setBorderColor(Color.parseColor("#00ff00"));
        }else if(sharedpreferences.getString("ImageTag", "").toString().equals("2")){
            userImageRound.setImageResource(R.drawable.dogaltpfp);
            userProfile2.setBorderWidth(8);
            userProfile2.setBorderColor(Color.parseColor("#00ff00"));
        }else if(sharedpreferences.getString("ImageTag", "").toString().equals("3")){
            userImageRound.setImageResource(R.drawable.dogonepfp);
            userProfile3.setBorderWidth(8);
            userProfile3.setBorderColor(Color.parseColor("#00ff00"));
        }else if(sharedpreferences.getString("ImageTag", "").toString().equals("4")){
            userImageRound.setImageResource(R.drawable.catpfp);
            userProfile4.setBorderWidth(8);
            userProfile4.setBorderColor(Color.parseColor("#00ff00"));
        }else if(sharedpreferences.getString("ImageTag", "").toString().equals("5")){
            userImageRound.setImageResource(R.drawable.cataltpfp);
            userProfile5.setBorderWidth(8);
            userProfile5.setBorderColor(Color.parseColor("#00ff00"));
        }else  if(sharedpreferences.getString("ImageTag", "").toString().equals("6")){
            userImageRound.setImageResource(R.drawable.catonepfp);
            userProfile6.setBorderWidth(8);
            userProfile6.setBorderColor(Color.parseColor("#00ff00"));
        }else{
            userImageRound.setImageResource(R.drawable.defaultuser);
        }

    }

    public void resetImageBorders(){
        userProfile1.setBorderWidth(1);
        userProfile2.setBorderWidth(1);
        userProfile3.setBorderWidth(1);
        userProfile4.setBorderWidth(1);
        userProfile5.setBorderWidth(1);
        userProfile6.setBorderWidth(1);
        userProfile1.setBorderColor(Color.BLACK);
        userProfile2.setBorderColor(Color.BLACK);
        userProfile3.setBorderColor(Color.BLACK);
        userProfile4.setBorderColor(Color.BLACK);
        userProfile5.setBorderColor(Color.BLACK);
        userProfile6.setBorderColor(Color.BLACK);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(UserProfileActivity.this, MarqueeHomeActivity.class);
        startActivity(intent);
    }

    public void selectImage(View view){
        Intent selectPhoto = new Intent(Intent.ACTION_PICK);
        selectPhoto.setType("image/*");
        startActivityForResult(selectPhoto, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && data != null){
            uri = data.getData();
            userImageRound.setImageURI(uri);
        }else {
            Toast.makeText(UserProfileActivity.this, "Please select an image", Toast.LENGTH_SHORT).show();
        }
    }
}