package com.example.goalmeet.fragment;

import android.app.Dialog;
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
import com.example.goalmeet.Class.RequestJoin;
import com.example.goalmeet.Class.User;
import com.example.goalmeet.Controller.ControllerUserInHisProfile;
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

    private TextView  userProfil_TXT_editSymbol, userProfil_TXT_editDescription,userProfil_TXT_editCity,userProfil_TXT_editName ;
    private ImageView userProfil_IMG_symbol, userProfil_IMG_saveDescription, userProfil_IMG_saveCity, userProfil_IMG_saveName;
    String userId;
    private ControllerUserInHisProfile userInHisProfile;
    private User user;
    private TextInputLayout userProfil_ETXT_description,userProfil_ETXT_name, userProfil_ETXT_city ;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private StorageReference storageReference;
    private ValueEventListener valueEventListener;
    private static final int IMAGE_REQUEST = 1;
    private Uri imageUri;
    View view;
    private Dialog mDialog;
    private StorageTask uploasTask;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profil, container, false);

        initView();
        getDataFromSharedPreferences();
        checkRequest();
        return view;
    }

    private void initView() {
        mDialog = new Dialog(getActivity());
        userProfil_IMG_symbol = view.findViewById(R.id.userProfil_IMG_symbol);
        userProfil_ETXT_name = view.findViewById(R.id.userProfil_ETXT_name);
        userProfil_TXT_editName=view.findViewById(R.id.userProfil_TXT_editName);
        userProfil_TXT_editSymbol = view.findViewById(R.id.userProfil_TXT_editSymbol);
        userProfil_TXT_editDescription = view.findViewById(R.id.userProfil_TXT_editDescription);
        userProfil_TXT_editCity = view.findViewById(R.id.userProfil_TXT_editCity);
        userProfil_ETXT_description = view.findViewById(R.id.userProfil_ETXT_description);
        userProfil_ETXT_city = view.findViewById(R.id.userProfil_ETXT_city);
        userProfil_IMG_saveDescription = view.findViewById(R.id.userProfil_IMG_saveDescription);
        userProfil_IMG_saveCity = view.findViewById(R.id.userProfil_IMG_saveCity);
        userProfil_IMG_saveName = view.findViewById(R.id.userProfil_IMG_saveName);
    }

    private void getDataFromSharedPreferences() {
        SharedPreferences prefs = getActivity().getSharedPreferences(SP_FILE, getActivity().MODE_PRIVATE);
        userId = prefs.getString("userId", null);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.commit();
    }

    private void checkRequest() {//check if there is confirm from manager's clubs
        reference = FirebaseDatabase.getInstance().getReference("requestJoin");
        valueEventListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RequestJoin requestJoin = snapshot.getValue(RequestJoin.class);
                    if (requestJoin.getSender().equals(firebaseUser.getUid())) {
                        if (requestJoin.getSeen() == true) {
                            mDialog.setContentView(R.layout.popup);
                            TextView popup_TXT_answer = mDialog.findViewById(R.id.popup_TXT_answer);
                            if (requestJoin.getConfirmApplication() == true) {
                                popup_TXT_answer.setText("The club manager has approved you, welcome to " + requestJoin.getNameTeam());
                                mDialog.getWindow();
                                mDialog.show();
                                //     user.setNameClub("");
                            } else {
                                popup_TXT_answer.setText("The " + requestJoin.getNameTeam() + " group manager has not confirmed your joining, please contact the group manager via message or try another group"
                                        + requestJoin.getNameTeam());
                            }
                        }
                        break;
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        checkWhoUser();
    }

    private void checkWhoUser() {//check if user he is guest or visit in his profile
        if (userId == null) { //UserInHisProfile();
//            userInHisProfile = new ControllerUserInHisProfile(getContext());
//            userInHisProfile.manageProfile();
            UserInHisProfil();
        } else {
            userInelseProfil();
        }

    }



    private void UserInHisProfil() {
        //profil Image reference in storage
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userId =firebaseUser.getUid();
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

    private void readDataOfUserFromFireBase(DataSnapshot dataSnapshot) {
        user = dataSnapshot.getValue(User.class);
        if (!user.getImageURL().equals("defult")) {
            Glide.with(getContext()).load(user.getImageURL()).into(userProfil_IMG_symbol);
        }
        userProfil_ETXT_name.setHint(user.getUserName());
        userProfil_ETXT_city.setHint(user.getCity());
        userProfil_ETXT_description.setHint(user.getDescription());
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
                        Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(), "No Image Selected", Toast.LENGTH_SHORT).show();

        }

    }

    public void clickOnEdit() {//for user in his profile
        userProfil_TXT_editSymbol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        userProfil_TXT_editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userProfil_ETXT_name.setEnabled(true);
                userProfil_IMG_saveName.setVisibility(View.VISIBLE);
            }
        });
        userProfil_IMG_saveName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setUserName(userProfil_ETXT_name.getEditText().getText().toString());
                userProfil_ETXT_name.setHint(user.getCity());
                userProfil_ETXT_name.setEnabled(false);
                userProfil_IMG_saveName.setVisibility(View.GONE);
                changeAttributeInFireBase("userName",user.getUserName());
            }
        });
        userProfil_TXT_editCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userProfil_ETXT_city.setEnabled(true);
                userProfil_IMG_saveCity.setVisibility(View.VISIBLE);
            }
        });
        userProfil_IMG_saveCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setCity(userProfil_ETXT_city.getEditText().getText().toString());
                userProfil_ETXT_city.setHint(user.getCity());
                userProfil_ETXT_city.setEnabled(false);
                userProfil_IMG_saveCity.setVisibility(View.GONE);
                changeAttributeInFireBase("city",user.getCity());
            }
        });

        userProfil_TXT_editDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userProfil_ETXT_description.setEnabled(true);
                userProfil_IMG_saveDescription.setVisibility(View.VISIBLE);
            }
        });
        userProfil_IMG_saveDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.setDescription(userProfil_ETXT_description.getEditText().getText().toString());
                userProfil_ETXT_description.setHint(user.getCity());
                userProfil_ETXT_description.setEnabled(false);
                userProfil_IMG_saveDescription.setVisibility(View.GONE);
                changeAttributeInFireBase("description",user.getDescription());

            }
        });
    }
private void changeAttributeInFireBase(String key,String value){
    reference = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
    HashMap<String,Object> map = new HashMap<>();
    map.put(key,value);
    reference.updateChildren(map);
}
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            imageUri = data.getData();

            if (uploasTask != null && uploasTask.isInProgress()) {

                Toast.makeText(getContext(), "Upload in progress..", Toast.LENGTH_SHORT).show();
            } else {
                uploadMyImage();
            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        reference.removeEventListener(valueEventListener);
    }
}
