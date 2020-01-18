package com.example.kirk.biz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.kirk.Model.BIZ;
import com.example.kirk.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class bizprofileFragment extends Fragment {

    ImageView image2, options2;
    TextView bizname, biztype, bizphone, bizaddress,bizemail;
    Button edit_profile2;

    FirebaseUser firebaseUser;
    String profileid;
    DatabaseReference reference;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bizprofile, container, false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        SharedPreferences prefs = getContext().getSharedPreferences("PREFS", Context.MODE_PRIVATE);
        profileid = prefs.getString("profileid", "none");

        image2 = view.findViewById(R.id.image_profile2);
        options2 = view.findViewById(R.id.options2);
        bizname = view.findViewById(R.id.bussinessname);
        biztype = view.findViewById(R.id.Bussinesstype);
        bizphone = view.findViewById(R.id.bizphone);
        bizemail = view.findViewById(R.id.bizemail2);
        bizaddress = view.findViewById(R.id.bizaddress);
        edit_profile2 = view.findViewById(R.id.edit_profile2);


        options2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), bizoptionsActivity.class));
            }
        });

        edit_profile2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), bizeditprofileActivity.class));
            }
        });

        userinfo();




        return view;
    }

    private void userinfo(){
        DatabaseReference reference =FirebaseDatabase.getInstance().getReference("Bussiness").child(profileid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (getContext() == null){
                    return;
                }
                BIZ biz = dataSnapshot.getValue(BIZ.class);

                bizname.setText(biz.getBizname());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }




}
