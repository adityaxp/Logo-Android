package com.aditya.marquee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    Toolbar signUpToolBar;
    EditText userNameEditText, emailEdiText;
    TextInputLayout passwordTextInputLayout;
    Button signUpButton;
    TextView logInTextView;
    private FirebaseAuth mAuth;
    CustomLoadingDialog customLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signUpToolBar =  findViewById(R.id.signUpToolBar);

        setSupportActionBar(signUpToolBar);
        setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        signUpToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        userNameEditText = (EditText) findViewById(R.id.usernameEditText);
        emailEdiText = (EditText) findViewById(R.id.emailEditText);
        passwordTextInputLayout = (TextInputLayout) findViewById(R.id.passwordEditText);
        logInTextView = (TextView) findViewById(R.id.logInTextView);
        signUpButton = (Button) findViewById(R.id.signUpButton);
        logInTextView = (TextView) findViewById(R.id.logInTextView);
        mAuth = FirebaseAuth.getInstance();
        customLoadingDialog = new CustomLoadingDialog(SignUpActivity.this);


        logInTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, AuthenticationActivity.class);
                startActivity(intent);
            }
        });



        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customLoadingDialog.customLoadingDialogShow();
                if(userNameEditText.getText().toString().equals("") || emailEdiText.getText().toString().equals("") || passwordTextInputLayout.getEditText().getText().toString().equals("")){
                    customLoadingDialog.customLoadingDialogDismiss();
                    Toast.makeText(SignUpActivity.this, "Fields can't be empty", Toast.LENGTH_SHORT).show();
                }else{
                    mAuth.createUserWithEmailAndPassword(emailEdiText.getText().toString(), passwordTextInputLayout.getEditText().getText().toString())
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        customLoadingDialog.customLoadingDialogDismiss();
                                        SignUpData signUpData = new SignUpData(userNameEditText.getText().toString(), emailEdiText.getText().toString());
                                        FirebaseDatabase.getInstance("https://marquee-411f8-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("User").child(mAuth.getCurrentUser().getUid()).setValue(signUpData).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(SignUpActivity.this, "Sign up successful", Toast.LENGTH_SHORT).show();
                                                FirebaseAuth.getInstance().signOut();
                                                Intent intent = new Intent(SignUpActivity.this, AuthenticationActivity.class);
                                                startActivity(intent);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                customLoadingDialog.customLoadingDialogDismiss();
                                                Toast.makeText(SignUpActivity.this, "Failed!!!", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    } else {
                                        customLoadingDialog.customLoadingDialogDismiss();
                                        Toast.makeText(SignUpActivity.this, "Authentication Failed!", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            });

                }
            }
        });



        }
}