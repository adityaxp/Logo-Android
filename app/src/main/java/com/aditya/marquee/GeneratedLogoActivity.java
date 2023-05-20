package com.aditya.marquee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GeneratedLogoActivity extends AppCompatActivity {

    String prompt, imageURL;
    ImageView logoImageView;
    Button saveLogoInCloudButton, saveLogoInternalStorageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generated_logo);


        Toolbar signUpToolBar =  findViewById(R.id.signUpToolBar);
        logoImageView = findViewById(R.id.logoImageView);
        saveLogoInternalStorageButton = findViewById(R.id.saveLogoInternalStorageButton);
        saveLogoInCloudButton = findViewById(R.id.saveLogoInCloudButton);
        CustomLoadingDialog customLoadingDialog = new CustomLoadingDialog(this);

        setSupportActionBar(signUpToolBar);
        setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        signUpToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        prompt =  intent.getStringExtra("prompt");

        Toast.makeText(this, prompt, Toast.LENGTH_SHORT).show();

        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(GeneratedLogoActivity.this);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();

        Glide.with(GeneratedLogoActivity.this)
                .load(circularProgressDrawable)
                .into(logoImageView);

        saveLogoInternalStorageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DownloadManager downloadmanager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(imageURL);

                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setTitle(prompt);
                request.setDescription("Downloading");
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, prompt);
                downloadmanager.enqueue(request);

                logoImageView.setDrawingCacheEnabled(true);
                logoImageView.buildDrawingCache();
                Bitmap bitmap = logoImageView.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                File storageLoc = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES); //context.getExternalFilesDir(null);

                File file = new File(storageLoc, "Marquee"+ String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()))+ ".jpg");

                try{
                    FileOutputStream fos = new FileOutputStream(file);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.close();
                    Toast.makeText(GeneratedLogoActivity.this, "Image saved successfully", Toast.LENGTH_SHORT).show();

                    scanFile(GeneratedLogoActivity.this, Uri.fromFile(file));

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        saveLogoInCloudButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(imageURL.equals("")){
                    Toast.makeText(GeneratedLogoActivity.this, "Please wait image is generating", Toast.LENGTH_SHORT).show();
                }else{
                    customLoadingDialog.customLoadingDialogShow();
                    logoImageView.setDrawingCacheEnabled(true);
                    logoImageView.buildDrawingCache();
                    Bitmap bitmap = logoImageView.getDrawingCache();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();

                    StorageReference storageReference = FirebaseStorage.getInstance().getReference();

                    String timeStamp = String.valueOf(TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis()));
                    StorageReference imageReference = storageReference.child(timeStamp+".jpg");
                    StorageReference storageReference1 = storageReference.child("images/"+timeStamp+".jpg");

                    imageReference.getName().equals(storageReference1.getName());
                    imageReference.getPath().equals(storageReference1.getPath());


                    UploadTask uploadTask = imageReference.putBytes(data);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            customLoadingDialog.customLoadingDialogDismiss();

                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isComplete());
                            Uri urlImage = uriTask.getResult();
                            String downloadImageURL = urlImage.toString();
                            FirebaseAuth mAuth = FirebaseAuth.getInstance();
                            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                            ImageData imageData = new ImageData(date, downloadImageURL, prompt);
                            FirebaseDatabase.getInstance("https://marquee-411f8-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("CloudSaves").child(mAuth.getCurrentUser().getUid()).child(timeStamp).setValue(imageData);
                            customLoadingDialog.customLoadingDialogDismiss();
                            Toast.makeText(GeneratedLogoActivity.this, "Upload successful!", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(GeneratedLogoActivity.this, "Server bucket full!", Toast.LENGTH_SHORT).show();
                            customLoadingDialog.customLoadingDialogDismiss();

                        }
                    });

                }
            }
        });


        try{
            ImageGenerationTask task = new ImageGenerationTask("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", "image-alpha-001", prompt, 1);
            task.execute();


       }catch (Exception e){
           e.printStackTrace();
           Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
       }


    }

    public class ImageGenerationTask extends AsyncTask<Void, Void, String> {

        private String apiKey;
        private String model;
        private String prompt;
        private int numImages;

        public ImageGenerationTask(String apiKey, String model, String prompt, int numImages) {
            this.apiKey = apiKey;
            this.model = model;
            this.prompt = prompt;
            this.numImages = numImages;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String response = "";

            try {
                URL url = new URL("https://api.openai.com/v1/images/generations");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Authorization", "Bearer " + apiKey);

                String input = "{\"model\":\"" + model + "\", \"prompt\": \"" + prompt + "\", \"num_images\": " + numImages + "}";

                conn.setDoOutput(true);
                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                os.writeBytes(input);
                os.flush();
                os.close();

                Scanner scanner = new Scanner(conn.getInputStream());
                while (scanner.hasNext()) {
                    response += scanner.nextLine();
                }
                scanner.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            // Handle the API response here
            // This method is called on the main thread
            System.out.println(response);
            CustomLoadingDialog customLoadingDialog = new CustomLoadingDialog(GeneratedLogoActivity.this);



            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray data = jsonObject.getJSONArray("data");
                JSONObject image = data.getJSONObject(0);
                imageURL = image.getString("url");

                CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(GeneratedLogoActivity.this);
                circularProgressDrawable.setStrokeWidth(5f);
                circularProgressDrawable.setCenterRadius(30f);
                circularProgressDrawable.start();

                Glide.with(GeneratedLogoActivity.this)
                        .load(imageURL)
                        .override(300, 250)
                        .placeholder(circularProgressDrawable)
                        .into(logoImageView);
            } catch (Exception e){
                Toast.makeText(GeneratedLogoActivity.this, e.toString(), Toast.LENGTH_LONG).show();
            }


        }
    }
    private static void scanFile(Context context, Uri imageUri){
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(imageUri);
        context.sendBroadcast(scanIntent);

    }
}