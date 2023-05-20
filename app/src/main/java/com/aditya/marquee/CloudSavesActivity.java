package com.aditya.marquee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CloudSavesActivity extends AppCompatActivity {

    CloudSavesAdapter cloudSavesAdapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<CloudSavesData> cloudSavesDataArrayList;
    CloudSavesData cloudSavesData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_saves);

        Toolbar signUpToolBar =  findViewById(R.id.signUpToolBar);

        setSupportActionBar(signUpToolBar);
        setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        signUpToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        cloudSavesDataArrayList = new ArrayList<>();
        RecyclerView cloudSavesRecyclerView = (RecyclerView) findViewById(R.id.cloudSavesRecyclerView);

        layoutManager = new LinearLayoutManager(this);
        cloudSavesAdapter = new CloudSavesAdapter(this, cloudSavesDataArrayList);
        cloudSavesRecyclerView.setLayoutManager(layoutManager);
        cloudSavesRecyclerView.setAdapter(cloudSavesAdapter);



        DatabaseReference databaseReference = FirebaseDatabase.getInstance("https://marquee-411f8-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("CloudSaves").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        ValueEventListener valueEventListener = databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cloudSavesDataArrayList.clear();
                for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                    cloudSavesData = new CloudSavesData(itemSnapshot.child("prompt").getValue().toString(), itemSnapshot.child("date").getValue().toString(), itemSnapshot.child("imageURL").getValue().toString());
                    cloudSavesDataArrayList.add(cloudSavesData);

                }
                cloudSavesAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(CloudSavesActivity.this, MarqueeHomeActivity.class);
        startActivity(intent);
    }
}