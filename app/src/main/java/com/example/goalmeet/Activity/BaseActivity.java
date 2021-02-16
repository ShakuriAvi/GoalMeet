package com.example.goalmeet.Activity;

import androidx.appcompat.app.AppCompatActivity;

import com.example.goalmeet.R;
import com.example.goalmeet.fragment.ProfilFragment;

public class BaseActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfilFragment()).commit();


    }
}
