package com.example.goalmeet.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.goalmeet.Class.User;
import com.example.goalmeet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistryActivity extends AppCompatActivity {

    private ImageView registry_IMG_background;
    private TextInputLayout registry_EDT_userName , registry_EDT_password,registry_EDT_email;
    private Button registry_BTN_buttonRegistry;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registry);
        registry_EDT_userName = findViewById(R.id.registry_EDT_userName);
        registry_EDT_password = findViewById(R.id.registry_EDT_password);
        registry_BTN_buttonRegistry = findViewById(R.id.registry_BTN_buttonRegistry);
        registry_EDT_email = findViewById(R.id.registry_EDT_email);
        registry_IMG_background= findViewById(R.id.registry_IMG_background);
        //Glide.with(this).load(R.drawable.img_background).centerCrop().into(registry_IMG_background);
        Log.d("aaaa", "11111 ");
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("users");
        auth = FirebaseAuth.getInstance();

        //adding Event Listener to Button Register
        registry_BTN_buttonRegistry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = registry_EDT_userName.getEditText().getText().toString();
                String emailText = registry_EDT_email.getEditText().getText().toString();
                String password = registry_EDT_password.getEditText().getText().toString();
                Log.d("aaaa", " " + emailText + password + userName);
                if(userName ==null || emailText == null || password == null) {

                    Toast.makeText(RegistryActivity.this, "Please Fill All Fields", Toast.LENGTH_SHORT);
                }
                else{
                    register(userName,password,emailText);
            }

            }
        });
    }

    private  void register(String userName,String password,String email){
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    String userId = firebaseUser.getUid();
                    User user = new User(userId,userName,"defult","offline","","","");
                    String key = myRef.push().getKey();
                    myRef.child(userId).setValue(user);

                  //  myRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
                    //HashMaps
//                    HashMap<String,String> hashMap = new HashMap<>();
//                    hashMap.put("id",userId);
//                    hashMap.put("username",userName);
//                    hashMap.put("imageURL","defult");

                    // opening the main activity after success Registration
//                    myRef.addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Void> task) {
//                             if(task.isSuccessful()) {
                                 Intent i  = new Intent(RegistryActivity.this,MainActivity.class);
                                 i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                 startActivity(i);
                                 finish();
//                             }
 //                       }
 //                   });

                }else {
                  //  Log.d("FirebaseAuth", "onComplete" + task.getException().getMessage());
                  //  Log.d("aaaa", " 44444" );
                    Toast.makeText(RegistryActivity.this,"Invalid Email or Password",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
