package com.example.novigrad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class UpdateServices extends AppCompatActivity {


    Button update,mpersonalInfo,mdriverLicense,mproofOfResidence,mpassportScan,mcriminalRecord;
    TextView mnewName;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    Spinner spinner;
    boolean personalInfo,passportScan,proofOfResidence,criminalRecord,driverLicense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        System.out.println("in the update service class");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_available_services);

        update = (Button) findViewById(R.id.updateBtn);
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

        spinner = (Spinner) findViewById(R.id.spinner2);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();


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


        fStore = FirebaseFirestore.getInstance();
        CollectionReference serviceCollection = fStore.collection("services");

        //here we retrived the data from the collection "services" on firestore and we put it in the drop down list
        serviceCollection.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        String name;
                        String total ="Select service,";
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                            name = documentSnapshot.getId();
                            total += name + ",";

                        }
                        System.out.println("THE ARRAY IS "+total);
                        //we now have a long String containing for examepl "citizenship,G1 permit,G2 Permit"
                        //using the method .split(",") we transform this string in an array of 3 elements

                        final ArrayAdapter aa = new ArrayAdapter(UpdateServices.this,android.R.layout.simple_spinner_item,total.split(","));

                        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        //Setting the ArrayAdapter data on the Spinner
                        // Specify the layout to use when the list of choices appears

                        spinner.setAdapter(aa);

                    }
                } ) ;

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //getting all the Strings entered by the user in the form
                final String name = spinner.getSelectedItem().toString().trim();

                if (name.equals("Select service")){
                    startActivity(new Intent(getApplicationContext(),AdminWelcome.class));
                    return;

                }



                //checking if the entries are valid for the new name

                if(TextUtils.isEmpty(name)){
                    mnewName.setError("Name is required");
                    return;

                }
                userID = fAuth.getCurrentUser().getUid();
                fStore = FirebaseFirestore.getInstance();

                System.out.println(userID);




                //firebase function taht will create a new user using its name and password (the one entered in the form)


                Toast.makeText(UpdateServices.this,"User updated",Toast.LENGTH_SHORT);

                //we add the data of the new user to the database

                System.out.println(userID);



                Map<String,Object> service = new HashMap<>();

                //here we add the new user or employee to the firebase database
                System.out.println("Updating service");
                service.put("name",name);

                service.put("personalInfo",personalInfo);
                service.put("driverLicense",driverLicense);
                service.put("proofOfResidence",proofOfResidence);
                service.put("passportScan",passportScan);
                service.put("criminalRecord",criminalRecord);


                fStore.collection("services").document(name)
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
