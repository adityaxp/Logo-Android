package com.aditya.marquee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AuthenticationActivity extends AppCompatActivity {

    TextView signUpTextView;
    TextView needHelpTextView;
    EditText emailEditText;
    TextInputLayout passwordTextInputLayout;
    Button logInButton;
    private FirebaseAuth mAuth;
    CustomLoadingDialog customLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        signUpTextView = (TextView) findViewById(R.id.signUpTextView);
        needHelpTextView = (TextView) findViewById(R.id.needHelpTextView);
        signUpTextView = (TextView) findViewById(R.id.signUpTextView);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwordTextInputLayout = (TextInputLayout) findViewById(R.id.passwordEditText);
        logInButton = (Button) findViewById(R.id.loginButton);
        mAuth = FirebaseAuth.getInstance();
        customLoadingDialog = new CustomLoadingDialog(AuthenticationActivity.this);

        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customLoadingDialog.customLoadingDialogShow();
                if(emailEditText.getText().toString().equals("") || passwordTextInputLayout.getEditText().getText().toString().equals("")){
                    customLoadingDialog.customLoadingDialogDismiss();
                    Toast.makeText(AuthenticationActivity.this, "Fields can't be empty!", Toast.LENGTH_SHORT).show();
                }else {
                    mAuth.signInWithEmailAndPassword(emailEditText.getText().toString().trim(), passwordTextInputLayout.getEditText().getText().toString().trim())
                            .addOnCompleteListener(AuthenticationActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        customLoadingDialog.customLoadingDialogDismiss();
                                        Toast.makeText(AuthenticationActivity.this, "Authentication Successful!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(AuthenticationActivity.this, MarqueeHomeActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        customLoadingDialog.customLoadingDialogDismiss();
                                        Toast.makeText(AuthenticationActivity.this, "Authentication Failed!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        needHelpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AuthenticationActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AuthenticationActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

    }

}