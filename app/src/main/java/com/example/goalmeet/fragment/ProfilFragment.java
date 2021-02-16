package com.example.goalmeet.fragment;


import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import com.example.goalmeet.Activity.MessageActivity;
import com.example.goalmeet.Class.Request;
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

    private TextView userProfil_TXT_editSymbol, userProfil_TXT_editDescription, userProfil_TXT_editCity, userProfil_TXT_editName;
    private ImageView userProfil_IMG_symbol, userProfil_IMG_saveDescription, userProfil_IMG_saveCity, userProfil_IMG_saveName, userProfil_IMG_message;
    private String userId;
    private Request request;
    private   SharedPreferences prefs;
    private SharedPreferences.Editor editor;
    private User user;
    private TextInputLayout userProfil_ETXT_description, userProfil_ETXT_name, userProfil_ETXT_city, userProfil_ETXT_myTeam;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;
    private StorageReference storageReference;
    private ValueEventListener listenerAnswerRequest, lisitenerGuest, listenerUserHisProfile;
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
        permissionUser();
        Log.d("pppp", "1111 ");
        return view;
    }

    private void initView() {
        mDialog = new Dialog(getActivity());
        userProfil_IMG_symbol = view.findViewById(R.id.userProfil_IMG_symbol);
        userProfil_ETXT_name = view.findViewById(R.id.userProfil_ETXT_name);
        userProfil_TXT_editName = view.findViewById(R.id.userProfil_TXT_editName);
        userProfil_TXT_editSymbol = view.findViewById(R.id.userProfil_TXT_editSymbol);
        userProfil_TXT_editDescription = view.findViewById(R.id.userProfil_TXT_editDescription);
        userProfil_TXT_editCity = view.findViewById(R.id.userProfil_TXT_editCity);
        userProfil_ETXT_description = view.findViewById(R.id.userProfil_ETXT_description);
        userProfil_ETXT_city = view.findViewById(R.id.userProfil_ETXT_city);
        userProfil_IMG_saveDescription = view.findViewById(R.id.userProfil_IMG_saveDescription);
        userProfil_IMG_saveCity = view.findViewById(R.id.userProfil_IMG_saveCity);
        userProfil_IMG_saveName = view.findViewById(R.id.userProfil_IMG_saveName);
        userProfil_IMG_message = view.findViewById(R.id.userProfil_IMG_message);
        userProfil_ETXT_myTeam = view.findViewById(R.id.userProfil_ETXT_myTeam);
    }

    private void getDataFromSharedPreferences() {
       prefs = getContext().getSharedPreferences(SP_FILE, getContext().MODE_PRIVATE);
        userId = prefs.getString("userId", null);
        editor = prefs.edit();
        Log.d("tttt","3 "+ userId);
    }

    private void permissionUser() {
        if (userId==null) {//for user in her profile
            reference = FirebaseDatabase.getInstance().getReference("request"); //check if there is confirm from manager's clubs
            listenerAnswerRequest = reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        request = snapshot.getValue(Request.class);
                        if (request.getSender().equals(firebaseUser.getUid())) { // it's request to join group
                            if (request.getSeen() == true) {
                                if (request.getToTeam().equals(request.getFromTeam()) && request.getToTeam().equals("")) {//request of user want to join the club
                                    getMessageDialog(snapshot, request.getConfirmApplication(), "Congratulations!! " + request.getToTeam() + " have added you to their group. Good luck.",
                                            request.getToTeam() + " did not approve your request to join. Please notify the team manager or look for a new group in the available groups");
                                    snapshot.getRef().removeValue();
                                } else {//it's request of manager team for meet
                                    getMessageDialog(snapshot, request.getConfirmApplication(), "Congratulations!! " + request.getToTeam() + " want to meet your team. please contacat with " + request.getToTeam() + " and match game.",
                                            request.getToTeam() + " did not approve your request to match. Please notify the team manager or look for a new group in the available groups");
                                }
                            }
                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            UserInHisProfil();
        } else {
            userInElseProfil();
                if(userId!=null)
                    editor.remove("userId").apply();
        }
    }

    private void getMessageDialog(DataSnapshot snapshot, Boolean confirmApplication, String confirm, String refused) {

        mDialog.setContentView(R.layout.popup_status_myteam);
        TextView popup_TXT_answer = mDialog.findViewById(R.id.popup_TXT_answer);
        if (confirmApplication == true) {
            popup_TXT_answer.setText(confirm);
            mDialog.getWindow();
            mDialog.show();
        } else {
            popup_TXT_answer.setText(refused);
            mDialog.getWindow();
            mDialog.show();
        }
        snapshot.getRef().removeValue();

    }

    private void UserInHisProfil() {
        //profil Image reference in storage
        storageReference = FirebaseStorage.getInstance().getReference("uploads");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = firebaseUser.getUid();
        reference = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());

        listenerUserHisProfile = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                readDataOfUserFromFireBase(dataSnapshot);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        userProfil_TXT_editSymbol.setPaintFlags(userProfil_TXT_editSymbol.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        clickOnEditHisProfile();

    }

    private void userInElseProfil() {
        reference = FirebaseDatabase.getInstance().getReference("users").child(userId);

        Log.d("pppp", " " + reference);
        lisitenerGuest = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("tttt","2222");
                readDataOfUserFromFireBase(dataSnapshot);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
        clickOnOtherProfile();
    }

    private void readDataOfUserFromFireBase(DataSnapshot dataSnapshot) {
        user = dataSnapshot.getValue(User.class);
        if (!user.getImageURL().equals("defult")) {
            Glide.with(getContext()).load(user.getImageURL()).into(userProfil_IMG_symbol);
        }
        userProfil_ETXT_name.setHint(user.getUserName());
        userProfil_ETXT_city.setHint(user.getCity());
        userProfil_ETXT_description.setHint("About Me: " + user.getDescription());
        if (user.getNameClub().equals(""))
            userProfil_ETXT_myTeam.setHint("Name Team: None");
        else
            userProfil_ETXT_myTeam.setHint("Name Team: " + user.getNameClub());
    }




    public void clickOnEditHisProfile() {//for user in his profile
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
                changeAttributeInFireBase("userName", user.getUserName());
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
                changeAttributeInFireBase("city", user.getCity());
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
                changeAttributeInFireBase("description", user.getDescription());

            }
        });
    }

    private void clickOnOtherProfile() {
        userProfil_IMG_message.setVisibility(View.VISIBLE);

        userProfil_IMG_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MessageActivity.class);
                i.putExtra("userId", user.getId());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                getActivity().startActivity(i);
            }
        });
        userProfil_TXT_editDescription.setVisibility(View.GONE);
        userProfil_TXT_editSymbol.setVisibility(View.GONE);
        userProfil_TXT_editName.setVisibility(View.GONE);
        userProfil_TXT_editCity.setVisibility(View.GONE);
    }
    private void selectImage() {


        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
        getIntent.setType("image/*");

        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

        getActivity().startActivityForResult(chooserIntent, IMAGE_REQUEST);

    }

    private String getFileExtention(Uri uri) {
        Log.d("qqqq","999999");
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));

    }


    private void uploadMyImage() {
        Log.d("qqqq","88888");
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
                        Log.d("qqqq",mUri);
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
    private void changeAttributeInFireBase(String key, String value) {
        Log.d("qqqq","777777");
        reference = FirebaseDatabase.getInstance().getReference("users").child(firebaseUser.getUid());
        HashMap<String, Object> map = new HashMap<>();
        map.put(key, value);
        reference.updateChildren(map);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        reference.removeEventListener(listenerAnswerRequest);
        if (lisitenerGuest != null)
            reference.removeEventListener(lisitenerGuest);
        if (listenerUserHisProfile != null)
            reference.removeEventListener(listenerUserHisProfile);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("qqqq","RESULT_OK "+RESULT_OK );
        if (requestCode == IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            imageUri = data.getData();
            Log.d("qqqq",imageUri.getPath());
            if (uploasTask != null && uploasTask.isInProgress()) {

                Toast.makeText(getContext(), "Upload in progress..", Toast.LENGTH_SHORT).show();
            } else {
                Log.d("qqqq","22222");
                uploadMyImage();
            }
        }
    }
}
