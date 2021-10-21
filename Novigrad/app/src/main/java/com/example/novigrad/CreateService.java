package com.example.novigrad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateService extends AppCompatActivity {

    Button mcreateBtn,mpersonalInfo,mdriverLicense,mproofOfResidence,mpassportScan,mcriminalRecord;
    TextView mnewName;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    boolean personalInfo,passportScan,proofOfResidence,criminalRecord,driverLicense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_service);

        mcreateBtn = (Button) findViewById(R.id.updateBtn);
        mpersonalInfo = (CheckBox) findViewById(R.id.checkBox1);
        //System.out.println(mpersonalInfo);
        mdriverLicense = (CheckBox) findViewById(R.id.checkBox2);
        //System.out.println(mdriverLicense);
        mproofOfResidence = (CheckBox) findViewById(R.id.checkBox3);
        //System.out.println(mproofOfResidence);
        mpassportScan= (CheckBox) findViewById(R.id.checkBox4);
        //System.out.println(mpassportScan);
        mcriminalRecord = (CheckBox) findViewById(R.id.checkBox5);
        //System.out.println(mcriminalRecord);
        mnewName = (TextView) findViewById(R.id.newName);
        fAuth = FirebaseAuth.getInstance();


    //GARDER CETTE LOGIQUE

        mpersonalInfo.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                personalInfo = !personalInfo;
            }
        });
        mpassportScan.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                passportScan = !passportScan;
            }
        });
        mdriverLicense.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                driverLicense = !driverLicense;
            }
        });
        mproofOfResidence.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                proofOfResidence = !proofOfResidence;
            }
        });
        mcriminalRecord.setOnClickListener( new View.OnClickListener() {
            public void onClick(View v) {
                criminalRecord = !criminalRecord;
            }
        });

    mcreateBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            //getting all the Strings entered by the user in the form
            final String newName = mnewName.getText().toString().trim();



            //checking if the entries are valid for the new name

            if(TextUtils.isEmpty(newName)){
                mnewName.setError("Name is required");
                return;

            }
            userID = fAuth.getCurrentUser().getUid();
            fStore = FirebaseFirestore.getInstance();

            System.out.println(userID);




            //firebase function taht will create a new user using its name and password (the one entered in the form)


                        Toast.makeText(CreateService.this,"User created",Toast.LENGTH_SHORT);

                        //we add the data of the new user to the database

                        System.out.println(userID);



                        Map<String,Object> service = new HashMap<>();

                        //here we add the new user or employee to the firebase database
                        System.out.println("Creating new service");
                        service.put("name",newName);
                        service.put("personalInfo",personalInfo);
                        service.put("driverLicense",driverLicense);
                        service.put("proofOfResidence",proofOfResidence);
                        service.put("passportScan",passportScan);
                        service.put("criminalRecord",criminalRecord);

          
                        fStore.collection("services").document(newName)
                                .set(service)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("TAG", "DocumentSnapshot successfully written!");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("TAG", "Error writing document", e);
                                    }
                                });






            //the new entity has been added both locally and on the online database


                        //sending the user the the welcome page
                        startActivity(new Intent(getApplicationContext(),AdminWelcome.class));

                    }





        });


    }
}
