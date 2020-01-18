package com.example.kirk.biz;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kirk.R;

public class bizeditprofileActivity extends AppCompatActivity {

    ImageView image2, options;
    TextView  profession, bizname, biztype, phone, email, bizloc;
    Button edit_profile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bizeditprofile);
    }
}
