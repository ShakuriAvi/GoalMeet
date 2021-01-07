package com.example.goalmeet.Controller;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.provider.MediaStore;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.goalmeet.Class.User;
import com.example.goalmeet.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.view.View;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

public class ControllerUserInHisProfile {
    private Context context;
    private TextView userProfil_TXT_name, userProfil_TXT_editSymbol, userProfil_TXT_editDescription, userProfil_TXT_editCity;
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
        mDialog = new Dialog(context);
        userProfil_IMG_symbol =((Activity) context).findViewById(R.id.userProfil_IMG_symbol);
        userProfil_TXT_name =((Activity) context).findViewById(R.id.userProfil_TXT_name);
        userProfil_TXT_editSymbol =((Activity) context).findViewById(R.id.userProfil_TXT_editSymbol);
        userProfil_TXT_editDescription =((Activity) context).findViewById(R.id.userProfil_TXT_editDescription);
        userProfil_TXT_editCity = ((Activity) context).findViewById(R.id.userProfil_TXT_editCity);
        userProfil_ETXT_description =((Activity) context).findViewById(R.id.userProfil_ETXT_description);
        userProfil_ETXT_city = ((Activity) context).findViewById(R.id.userProfil_ETXT_city);
    }
    public void manageProfile() {
    //profil Image reference in storage
    storageReference =FirebaseStorage.getInstance().
    getReference("uploads");

    firebaseUser =FirebaseAuth.getInstance().

    getCurrentUser();

    reference =FirebaseDatabase.getInstance().

    getReference("users").

    child(firebaseUser.getUid());

        reference.addValueEventListener(new

    ValueEventListener() {
        @Override
        public void onDataChange (@NonNull DataSnapshot dataSnapshot){
            readDataOfUserFromFireBase(dataSnapshot);
        }

        @Override
        public void onCancelled (@NonNull DatabaseError error){

        }
    });

        userProfil_TXT_editSymbol.setPaintFlags(userProfil_TXT_editSymbol.getPaintFlags()|Paint.UNDERLINE_TEXT_FLAG);

    clickOnEdit();



}



    private void readDataOfUserFromFireBase(DataSnapshot dataSnapshot) {
        user = dataSnapshot.getValue(User.class);

        userProfil_TXT_name.setText(user.getUserName());
        if (!user.getImageURL().equals("defult")) {
            Glide.with(context).load(user.getImageURL()).into(userProfil_IMG_symbol);
        }
    }

    private void selectImage() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        i.setType("image/*");

        i.setAction(Intent.ACTION_GET_CONTENT);
        ((Activity)context).startActivityForResult(i, IMAGE_REQUEST);


    }

    private String getFileExtention(Uri uri) {

        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }


    private void uploadMyImage() {

        if (imageUri != null) {
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtention(imageUri));

            uploasTask = fileReference.putFile(imageUri);

            uploasTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {


                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                    if (!task.isSuccessful()) {
                        throw task.getException();

                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()) {
                        Uri downlandUri = (Uri) task.getResult();
                        String mUri = downlandUri.toString();
                        reference = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("imageURL", mUri);
                        reference.updateChildren(map);

                    } else {
                        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(context, "No Image Selected", Toast.LENGTH_SHORT).show();

        }

    }



    public void clickOnEdit() {
        userProfil_TXT_editSymbol.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                selectImage();
            }
        });
    }

}
