package com.example.novigrad;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import javax.annotation.Nullable;

public class EmployeeWelcome extends AppCompatActivity {


    //elements of the design like buttons, textview,...
    Button logout,services,hours,request;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    TextView welcome,rating;
    String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //retrieving the elements of the design, connecting buttons, texview,etc
        logout = (Button) findViewById(R.id.logOut);
        welcome = (TextView) findViewById(R.id.welcomeMessage);
        services = (Button) findViewById(R.id.servicesBtn);
        request = (Button) findViewById(R.id.requestBtn);
        hours = (Button) findViewById(R.id.hoursBtn);
        rating = findViewById(R.id.rating);





        //we retrieve the user that is currently logged in
        fAuth = FirebaseAuth.getInstance();
        //we check the user's data in the databse
        fStore = FirebaseFirestore.getInstance();
        //getting the ID of the user (this ID is automatically given by Firebase
        userID = fAuth.getCurrentUser().getUid();

        final DocumentReference documentReference = fStore.collection("users").document(userID);

        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                //if the user doesn't exsit we just stopped the method
                if (fAuth==null){
                    return;
                }
                if(fStore==null){
                    return;
                }

                if(documentSnapshot == null){
                    Log.i("Error; ", e.toString());
                }else {
                    //we display the information of the actual user
                    welcome.setText(   "Welcome "+documentSnapshot.getString("firstName")+ " " + documentSnapshot.getString("lastname"));
                    rating.setText("Your rating is : "+ documentSnapshot.getString("rating")+"/5");
                }
            }
        });
        //when the user wants to log out of the app, we print a message on the console for imformation and we sign out the firebase instance
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Signing out the current user.");
                FirebaseAuth.getInstance().signOut();

                //sending the user back to the log in page
                startActivity(new Intent(getApplicationContext(),Login.class));
                finish();
            }
        });
        services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Sending the enployee to the service mangeemnt activity");
                startActivity(new Intent(getApplicationContext(),service_available.class));
                finish();
            }
        });
        //set the onClick for the hours management button
        hours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SetBranchHours.class));
                finish();
            }
        });
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Request.class));
                finish();
            }
        });
    }
}
