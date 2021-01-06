package com.example.goalmeet.fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import static android.app.Activity.RESULT_OK;
import static com.example.goalmeet.Other.Constants.SP_FILE;

public class ProfilFragment extends Fragment {

    private TextView userProfil_TXT_name,userProfil_TXT_editSymbol, userProfil_TXT_editDescription,userProfil_TXT_editCity;
    private ImageView userProfil_IMG_symbol;
    String userId;
    private User user;
    private TextInputLayout userProfil_ETXT_description,userProfil_ETXT_city;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private StorageReference storageReference;
    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    View view;

    private StorageTask uploasTask;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profil, container, false);
        initView();

        getDataFromSharedPreferences();

        checkWhoUser();
        return view;
    }
    private void getDataFromSharedPreferences() {
        SharedPreferences prefs = getActivity().getSharedPreferences(SP_FILE, getActivity().MODE_PRIVATE);
        userId = prefs.getString("userId", null);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
    }
    private void checkWhoUser() {
        if(userId==null)
        UserInHerProfil();
        else{
            userInelseProfil();
        }
    }




    private void UserInHerProfil() {
        //profil Image reference in storage
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                readDataOfUserFromFireBase(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        userProfil_TXT_editSymbol.setPaintFlags(userProfil_TXT_editSymbol.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        clickOnEdit();
    }
    private void userInelseProfil() {
        reference = FirebaseDatabase.getInstance().getReference("users").child(userId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                readDataOfUserFromFireBase(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
        private void readDataOfUserFromFireBase(DataSnapshot dataSnapshot){
            user = dataSnapshot.getValue(User.class);

            userProfil_TXT_name.setText(user.getUserName());
            if (!user.getImageURL().equals("defult")) {
                Glide.with(getContext()).load(user.getImageURL()).into(userProfil_IMG_symbol);
            }
        }

    private void initView() {
        userProfil_IMG_symbol = view.findViewById(R.id.userProfil_IMG_symbol);
        userProfil_TXT_name = view.findViewById(R.id.userProfil_TXT_name);
        userProfil_TXT_editSymbol = view.findViewById(R.id.userProfil_TXT_editSymbol);
        userProfil_TXT_editDescription = view.findViewById(R.id.userProfil_TXT_editDescription);
        userProfil_TXT_editCity = view.findViewById(R.id.userProfil_TXT_editCity);
        userProfil_ETXT_description = view.findViewById(R.id.userProfil_ETXT_description);
        userProfil_ETXT_city = view.findViewById(R.id.userProfil_ETXT_city);
    }

    private void selectImage() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        i.setType("image/*");

        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i, IMAGE_REQUEST);


    }

    private String getFileExtention(Uri uri) {

        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }


    private void uploadMyImage(){

        if(imageUri != null){
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtention(imageUri));

            uploasTask = fileReference.putFile(imageUri);

            uploasTask.continueWithTask(new Continuation <UploadTask.TaskSnapshot,Task<Uri>>() {


                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                    if (!task.isSuccessful()){
                        throw task.getException();

                    }
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful()){
                        Uri downlandUri = (Uri) task.getResult();
                        String mUri = downlandUri.toString();
                        reference = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
                        HashMap<String,Object> map = new HashMap<>();
                        map.put("imageURL",mUri);
                        reference.updateChildren(map);

                    }else{
                        Toast.makeText(getContext(),"Failed",Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(getContext(),"No Image Selected",Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){

            imageUri = data.getData();

            if(uploasTask !=null && uploasTask.isInProgress()){

                Toast.makeText(getContext(),"Upload in progress..",Toast.LENGTH_SHORT).show();
            }else{
                uploadMyImage();
            }
        }
    }
    public void clickOnEdit(){
        userProfil_TXT_editSymbol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
//
//        userProfil_TXT_editCity.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                userProfil_ETXT_city.setEnabled(true);
//                userProfil_IMG_saveCity.setVisibility(View.VISIBLE);
//            }
//        });
//        userProfilIMG_saveCity.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                user.setCity(team_ETXT_city.getEditText().getText().toString());
//                team_ETXT_city.setHint(team.getCity());
//                team_ETXT_city.setEnabled(false);
//                team_IMG_saveCity.setVisibility(View.GONE);
//            }
//        });
    }

}
