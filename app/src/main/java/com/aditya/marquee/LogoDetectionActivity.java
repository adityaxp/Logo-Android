package com.aditya.marquee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;


import com.google.android.gms.tasks.Task;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;


public class LogoDetectionActivity extends AppCompatActivity {

    Toolbar logoDetectionToolBar;
    ImageView logoImageView;
    Button pickButton, captureButton, debugButton;
    Boolean imageFlag, uriFlag, drawableFlag;
    Uri uri;
    BitmapDrawable drawable;
    Bitmap bitmap, scaledDownBitmap;
    ByteArrayOutputStream byteArrayOutputStream;
    byte[] imageBytes;
    String base64encoded;
    JsonObject request, image;
    JsonArray features;
    TextView debugTextView;
    private final String TAG = "CloudVision";
    static final int REQUEST_CODE_PICK_ACCOUNT = 101;
    static final int REQUEST_ACCOUNT_AUTHORIZATION = 102;
    static final int REQUEST_PERMISSIONS = 13;

    private static String accessToken;
    private Account mAccount;

    ImageLabeler imageLabeler;
    TextRecognizer textRecognizer;
    InputImage inputImage;
    String text,genText, confidence, index;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo_detection);

        logoDetectionToolBar =  findViewById(R.id.logoDetectionToolBar);

        setSupportActionBar(logoDetectionToolBar);
        setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        logoDetectionToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        imageFlag = false;
        uriFlag = false;
        drawableFlag = false;

        logoImageView = (ImageView) findViewById(R.id.logoImageView);
        pickButton = (Button) findViewById(R.id.pickButton);
        captureButton = (Button) findViewById(R.id.captureButton);
        debugButton = (Button) findViewById(R.id.debugButton);
        debugTextView = (TextView) findViewById(R.id.debugTextView);


        imageLabeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS);
        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);



        pickButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageFlag = true;
                uriFlag = true;
                drawableFlag = false;
                selectImage(v);
            }
        });

        captureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageFlag = true;
                uriFlag = false;
                drawableFlag = true;
                captureImage(v);
            }
        });

        debugButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageFlag){
//                    drawable = (BitmapDrawable) logoImageView.getDrawable();
//                    bitmap = drawable.getBitmap();
//                    scaledDownBitmap = scaleBitmapDown(bitmap, 640);
//                    byteArrayOutputStream = new ByteArrayOutputStream();
//                    scaledDownBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
//                    imageBytes = byteArrayOutputStream.toByteArray();
//                    base64encoded = Base64.encodeToString(imageBytes, Base64.NO_WRAP);

                       drawable = (BitmapDrawable) logoImageView.getDrawable();
                       bitmap = drawable.getBitmap();
                       if(uriFlag){
                           try {
                               inputImage = InputImage.fromFilePath(LogoDetectionActivity.this, uri);
                           } catch (IOException e) {
                               e.printStackTrace();
                           }
                       }else if(drawableFlag){
                              inputImage = InputImage.fromBitmap(bitmap, 0);
                       }
                       imageLabeler.process(inputImage).addOnSuccessListener(new OnSuccessListener<List<ImageLabel>>() {
                           @Override
                           public void onSuccess(List<ImageLabel> imageLabels) {
                               for (ImageLabel label : imageLabels) {
                                    text = label.getText();
                                    confidence = String.valueOf(label.getConfidence());
                                    index = String.valueOf(label.getIndex());
                               }
                               Task<Text> result = textRecognizer.process(inputImage)
                                       .addOnSuccessListener(new OnSuccessListener<Text>() {
                                           @Override
                                           public void onSuccess(Text visionText) {
                                               for (Text.TextBlock block : visionText.getTextBlocks()) {
                                                   Rect boundingBox = block.getBoundingBox();
                                                   Point[] cornerPoints = block.getCornerPoints();
                                                   genText = block.getText();

                                                   for (Text.Line line: block.getLines()) {
                                                       for (Text.Element element: line.getElements()) {
                                                           for (Text.Symbol symbol: element.getSymbols()) {
                                                           }
                                                       }
                                                   }
                                               }
                                           }
                                       })
                                       .addOnFailureListener(
                                               new OnFailureListener() {
                                                   @Override
                                                   public void onFailure(@NonNull Exception e) {
                                                       // Task failed with an exception
                                                       // ...
                                                   }});
                               debugTextView.setText("Output: \nSource Type: "+ text + "\nDetected Logo: " + genText +"\nConfidence metrics: " + confidence + "\nAccuracy: " +  index);
                           }
                       }).addOnFailureListener(new OnFailureListener() {
                           @Override
                           public void onFailure(@NonNull Exception e) {

                           }
                       });






//                    // Create json request to cloud vision
//                    request = new JsonObject();
//                    // Add image to request
//                    image = new JsonObject();
//                    image.add("content", new JsonPrimitive(base64encoded));
//                    request.add("image", image);
//                    //Add features to the request
//                    JsonObject feature = new JsonObject();
//                    feature.add("maxResults", new JsonPrimitive(5));
//                    feature.add("type", new JsonPrimitive("LABEL_DETECTION"));
//                    features = new JsonArray();
//                    features.add(feature);
//                    request.add("features", features);
//
//                    annotateImage(request.toString())
//                            .addOnCompleteListener(new OnCompleteListener<JsonElement>() {
//                                @Override
//                                public void onComplete(@NonNull Task<JsonElement> task) {
//                                    if (!task.isSuccessful()) {
//                                        Toast.makeText(LogoDetectionActivity.this, "Logo Detection failed! Please check your network connection", Toast.LENGTH_SHORT).show();
//                                    } else {
//                                        String text = "";
//                                        String entityId = "";
//                                        float score = 0.f;
//                                        for (JsonElement label : task.getResult().getAsJsonArray().get(0).getAsJsonObject().get("labelAnnotations").getAsJsonArray()) {
//                                            JsonObject labelObj = label.getAsJsonObject();
//                                            text = labelObj.get("description").getAsString();
//                                            entityId = labelObj.get("mid").getAsString();
//                                            score = labelObj.get("score").getAsFloat();
//                                        }
//                                        debugTextView.setText("Output: " + text + " " + entityId );
//                                    }
//                                }
//                            });
//
//
//                }else{
//                    Toast.makeText(LogoDetectionActivity.this, "No image found", Toast.LENGTH_SHORT).show();
             }
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Intent intent = new Intent(LogoDetectionActivity.this, MarqueeHomeActivity.class);
        //startActivity(intent);
    }

    public void selectImage(View view){
        Intent selectPhoto = new Intent(Intent.ACTION_PICK);
        selectPhoto.setType("image/*");
        startActivityForResult(selectPhoto, 1);
    }

    public void captureImage(View view){
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, 2);
    }

//    private Task<JsonElement> annotateImage(String requestJson) {
//        return mFunctions
//                .getHttpsCallable("annotateImage")
//                .call(requestJson)
//                .continueWith(new Continuation<HttpsCallableResult, JsonElement>() {
//                    @Override
//                    public JsonElement then(@NonNull Task<HttpsCallableResult> task) {
//                        // This continuation runs on either success or failure, but if the task
//                        // has failed then getResult() will throw an Exception which will be
//                        // propagated down.
//                        return JsonParser.parseString(new Gson().toJson(task.getResult().getData()));
//                    }
//                });
//    }


    private Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {
        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1) {
            if (resultCode == RESULT_OK && data != null) {
                uri = data.getData();
                logoImageView.setImageURI(uri);
            } else {
                Toast.makeText(LogoDetectionActivity.this, "Please select an image", Toast.LENGTH_SHORT).show();
            }

        }else if(requestCode == REQUEST_CODE_PICK_ACCOUNT) {
            String email = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            AccountManager am = AccountManager.get(this);
            Account[] accounts = am.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
            for (Account account : accounts) {
                if (account.name.equals(email)) {
                    mAccount = account;
                    break;
                }
            }
            getAuthToken();
        }else if(requestCode == REQUEST_ACCOUNT_AUTHORIZATION){
            if (resultCode == RESULT_OK) {
                Bundle extra = data.getExtras();
                onTokenReceived(extra.getString("authtoken"));
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Authorization Failed", Toast.LENGTH_SHORT)
                        .show();
            }
        }else if(requestCode == 2){
            if(resultCode == RESULT_OK && data != null){
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                logoImageView.setImageBitmap(photo);
            }else {
                Toast.makeText(LogoDetectionActivity.this, "Please capture an image", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(LogoDetectionActivity.this, "Error! please retry", Toast.LENGTH_SHORT).show();
        }

    }

    public void performCloudVisionRequest(Uri uri, Bitmap bitmapgen) {
        if (uri != null) {
            try {
                Bitmap bitmap = resizeBitmap(
                        MediaStore.Images.Media.getBitmap(getContentResolver(), uri));
                callCloudVision(bitmap);
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
        }
    }

    public Bitmap resizeBitmap(Bitmap bitmap) {

        int maxDimension = 1024;
        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }

    @SuppressLint("StaticFieldLeak")
    private void callCloudVision(final Bitmap bitmap) throws IOException {



    }
//    public Image getBase64EncodedJpeg(Bitmap bitmap) {
//        Image image = new Image();
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
//        byte[] imageBytes = byteArrayOutputStream.toByteArray();
//        image.encodeContent(imageBytes);
//        return image;
//    }

    private void pickUserAccount() {
        String[] accountTypes = new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE};
        Intent intent = AccountPicker.newChooseAccountIntent(null, null,
                accountTypes, false, null, null, null, null);
        startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
    }

    private void getAuthToken() {
//        String SCOPE = "oauth2:https://www.googleapis.com/auth/cloud-platform";
//        if (mAccount == null) {
//            pickUserAccount();
//        } else {
//            new GetOAuthToken(LogoDetectionActivity.this, mAccount, SCOPE, REQUEST_ACCOUNT_AUTHORIZATION)
//                    .execute();
//        }
    }

    public void onTokenReceived(String token){
        accessToken = token;
    }


}

