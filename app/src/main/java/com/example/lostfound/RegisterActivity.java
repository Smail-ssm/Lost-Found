package com.example.lostfound;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth firebaseAuth;

    private TextView textViewSignin;
    private EditText editTextEmail, editTextPassword, editTextConfirmPassword;
    private Button buttonRegister;

    private ProgressDialog progressDialog;

    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.abc_fade_in,R.anim.abc_fade_out);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        textViewSignin = (TextView) findViewById(R.id.textViewSignin);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        editTextConfirmPassword = (EditText) findViewById(R.id.editTextConfirmPassword);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);

        progressDialog = new ProgressDialog(this);

        textViewSignin.setOnClickListener(this);
        buttonRegister.setOnClickListener(this);
    }

    private void registerUser(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(confirmPassword) || !password.equals(confirmPassword)){
            Toast.makeText(this, "Please confirm password", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>(){
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task){
                        progressDialog.dismiss();
                        if (task.isSuccessful()){
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(context, "Please check email for verification.", Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(context, task.getException().getMessage() , Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            finish();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        }
                        else {
                            Toast.makeText(RegisterActivity.this,"Could not registered ... Please try again",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View view){
        if (view == buttonRegister){
            registerUser();
        }
        else if (view == textViewSignin){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }
}