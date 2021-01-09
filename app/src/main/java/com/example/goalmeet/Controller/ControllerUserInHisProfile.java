package com.example.goalmeet.Controller;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.goalmeet.Class.User;
import com.example.goalmeet.R;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.view.View;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

public class ControllerUserInHisProfile {
    private Context context;
    private TextView userProfil_ETXT_name, userProfil_TXT_editSymbol, userProfil_TXT_editDescription, userProfil_TXT_editCity;
    private ImageView userProfil_IMG_symbol;
    private String userId;
    private User user;
    private TextInputLayout userProfil_ETXT_description, userProfil_ETXT_city;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private StorageReference storageReference;
    private ValueEventListener valueEventListener;
    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    View view;
    private Dialog mDialog;
    private StorageTask uploasTask;

    public ControllerUserInHisProfile(Context context) {
        this.context = context;
        initView();
    }
    private void initView(){
        userProfil_IMG_symbol =((Activity) context).findViewById(R.id.userProfil_IMG_symbol);
        userProfil_ETXT_name =((Activity) context).findViewById(R.id.userProfil_ETXT_name);
        userProfil_TXT_editSymbol =((Activity) context).findViewById(R.id.userProfil_TXT_editSymbol);
        userProfil_TXT_editDescription =((Activity) context).findViewById(R.id.userProfil_TXT_editDescription);
        userProfil_TXT_editCity = ((Activity) context).findViewById(R.id.userProfil_TXT_editCity);
        userProfil_ETXT_description =((Activity) context).findViewById(R.id.userProfil_ETXT_description);
        userProfil_ETXT_city = ((Activity) context).findViewById(R.id.userProfil_ETXT_city);
    }

}
