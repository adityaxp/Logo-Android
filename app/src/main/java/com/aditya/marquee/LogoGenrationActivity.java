package com.aditya.marquee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LogoGenrationActivity extends AppCompatActivity {

    TextInputEditText promptEditText;
    Button generateLogoButton;
    int limit = 0;
    int currentValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo_genration);

        Toolbar signUpToolBar =  findViewById(R.id.signUpToolBar);
        promptEditText = findViewById(R.id.promptEditText);

        setSupportActionBar(signUpToolBar);
        setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        signUpToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        generateLogoButton  = findViewById(R.id.generateLogoButton);
        generateLogoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(promptEditText.getText().toString().equals("")){
                    Toast.makeText(LogoGenrationActivity.this, "Prompt is empty!", Toast.LENGTH_SHORT).show();
                }else {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://marquee-411f8-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("ImageGenLimiter");
                    databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            limit = Integer.parseInt(dataSnapshot.child("Limit").getValue().toString());
                            currentValue = Integer.parseInt(dataSnapshot.child("CurrentValue").getValue().toString());
                            if(currentValue != limit){
                                currentValue += 1;
                                FirebaseDatabase.getInstance("https://marquee-411f8-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("ImageGenLimiter").child("CurrentValue").setValue(currentValue).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Intent intent = new Intent(LogoGenrationActivity.this, GeneratedLogoActivity.class);
                                        intent.putExtra("prompt", promptEditText.getText().toString());
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            }else {
                                Toast.makeText(LogoGenrationActivity.this, "Token limit exhausted", Toast.LENGTH_SHORT).show();
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(LogoGenrationActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(LogoGenrationActivity.this, MarqueeHomeActivity.class);
        startActivity(intent);
    }
}