package com.example.goalmeet.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.goalmeet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;


public class LoginActivity extends AppCompatActivity {
    private TextInputLayout login_EDT_password, login_EDT_email;
    private ImageView login_IMG_title,login_IMG_symbol;
    private Button login_BTN_buttonLogin,login_BTN_buttonRegister;
    private FirebaseUser fireBaseUser;
    private DatabaseReference myRef;
    private FirebaseAuth auth;

//    @Override
//    protected void onStart() {
//        super.onStart();
//        fireBaseUser = FirebaseAuth.getInstance().getCurrentUser();
////
//        //checking for users existance
//        if(fireBaseUser !=null){
//            Intent i = new Intent(LoginActivity.this, MainActivity.class);
//            startActivity(i);
//            finish();
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login_IMG_title = findViewById(R.id.login_IMG_title);
        Glide.with(this).load(R.drawable.new_title2).into(login_IMG_title);
        login_IMG_symbol = findViewById(R.id.login_IMG_symbol);
        Glide.with(this).load(R.drawable.team).into(login_IMG_symbol);
        login_EDT_email = findViewById(R.id.login_EDT_email);
        login_EDT_password = findViewById(R.id.login_EDT_password);
        login_BTN_buttonLogin = findViewById(R.id.login_BTN_buttonLogin);
        login_BTN_buttonRegister = findViewById(R.id.login_BTN_buttonRegister);

       // FireBase Auth:
        auth = FirebaseAuth.getInstance();


        //Register Button
        login_BTN_buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, RegistryActivity.class);
                startActivity(i);

            }
        });


        // Login Button:
        login_BTN_buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = login_EDT_email.getEditText().getText().toString();
                String password = login_EDT_password.getEditText().getText().toString();

                //checking if it is empty
                if(TextUtils.isEmpty(email) ||  TextUtils.isEmpty(password)) {

                    Toast.makeText(LoginActivity.this, "Please Fill All Fields", Toast.LENGTH_SHORT);
                }
                else{
                    auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                Intent i  = new Intent(LoginActivity.this,MainActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                finish();
                            }else {
                                Toast.makeText(LoginActivity.this,"Login Invalid",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}
