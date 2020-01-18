package com.example.kirk.biz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.kirk.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.HashMap;

public class bussiness_regActivity extends AppCompatActivity {
    AutoCompleteTextView region2, biztype2;
    EditText bizname, bizad, bizemail, bizpassword, phone;

    Button register;

    FirebaseUser firebaseUser;

    private StorageTask uploadTask;
    StorageReference storageRef;
    DatabaseReference reference;
    ProgressDialog pd;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bussiness_reg);




        final AutoCompleteTextView region2 =  findViewById(R.id.region);
        final AutoCompleteTextView biztype2 =  findViewById(R.id.biztype);
        bizad =  findViewById(R.id.address);
        phone =  findViewById(R.id.phone);
        bizname =  findViewById(R.id.bizname);
        register =  findViewById(R.id.bizreg);
        bizemail = findViewById(R.id.bizemail);
        bizpassword = findViewById(R.id.bizpassword);

        biztype2.setThreshold(1);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, biztype);
        biztype2.setAdapter(adapter);

        region2.setThreshold(1);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item, region);
        region2.setAdapter(adapter2);

        auth = FirebaseAuth.getInstance();




        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pd = new ProgressDialog(bussiness_regActivity.this);
                pd.setMessage("Please wait...");
                pd.show();

                String str_bizname = bizname.getText().toString();

                String str_phone = phone.getText().toString();

                String str_region = region2.getText().toString();
                String str_biztype = biztype2.getText().toString();
                String str_bizad = bizad.getText().toString();
                String str_bizemail= bizemail.getText().toString();
                String str_bizpassword = bizpassword.getText().toString();

                if (TextUtils.isEmpty(str_bizname) || TextUtils.isEmpty(str_bizpassword) || TextUtils.isEmpty(str_bizemail) || TextUtils.isEmpty(str_phone)
                        || TextUtils.isEmpty(str_region) || TextUtils.isEmpty(str_biztype)|| TextUtils.isEmpty(str_bizad)   ){
                    Toast.makeText(bussiness_regActivity.this, "All fields are required!", Toast.LENGTH_SHORT).show();
                }else if(str_bizpassword.length() < 6){
                    Toast.makeText(bussiness_regActivity.this, "Password must have 6 characters!", Toast.LENGTH_SHORT).show();
                }else {
                    register(str_bizname, str_bizpassword, str_bizemail,str_bizad,str_biztype,str_region,str_phone);
                }
            }
        });

    }
    private static final String[] biztype = new String[]{"Record Label", "Makeup Studio", "Music Studio", "Model Agency","Talent Management","Club","Photo Studio","Event Center","Hotel","Dance Studio","Fitness Center","Music School","Film School"};
    private static final String[] region = new String[] {"Abia",
            "Adamawa",
            "Anambra",
            "Akwa Ibom",
            "Bauchi",
            "Bayelsa",
            "Benue",
            "Borno",
            "Cross River",
            "Delta",
            "Ebonyi",
            "Enugu",
            "Edo",
            "Ekiti",
            "FCT - Abuja",
            "Gombe",
            "Imo",
            "Jigawa",
            "Kaduna",
            "Kano",
            "Katsina",
            "Kebbi",
            "Kogi",
            "Kwara",
            "Lagos",
            "Nasarawa",
            "Niger",
            "Ogun",
            "Ondo",
            "Osun",
            "Oyo",
            "Plateau",
            "Rivers",
            "Sokoto",
            "Taraba",
            "Yobe",
            "Zamfara"};
    public void register(final String bizname, String bizpassword, String bizemail, final String bizad, final String biztype, final String region, final String phone){

        auth.createUserWithEmailAndPassword(bizemail, bizpassword)
                .addOnCompleteListener(bussiness_regActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String userID = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference().child("Bussiness").child(userID);
                            HashMap<String, Object> map = new HashMap<>();

                            map.put("id", userID);
                            map.put("bussiness name", bizname);
                            map.put("bussinesstype", biztype );
                            map.put("region", region);
                            map.put("imageurl2", "https://firebasestorage.googleapis.com/v0/b/kirk-30f6a.appspot.com/o/bussiness%2Flogo.png?alt=media&token=fd176be7-1e1a-4784-a0b5-b516b0a13757");

                            map.put("address", bizad);

                            map.put("contact", phone);

                            reference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        pd.dismiss();
                                        Intent intent = new Intent(bussiness_regActivity.this, BizMainActivity .class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                    }
                                }
                            });
                        } else {
                            pd.dismiss();
                            Toast.makeText(bussiness_regActivity.this, "You can't register with this email or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }





}


