package com.example.novigrad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.w3c.dom.Text;

import javax.annotation.Nullable;

public class CustomerWelcome extends AppCompatActivity {

    Button createRequest, checkStatus,logOut,review;
    TextView welcome;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_welcome);

        createRequest = (Button) findViewById(R.id.button);
        checkStatus = (Button) findViewById(R.id.button2);
        logOut = (Button) findViewById(R.id.button3);
        review = (Button) findViewById(R.id.review);
        welcome = (TextView) findViewById(R.id.textView9);
        fAuth = FirebaseAuth.getInstance();
        fStore =FirebaseFirestore.getInstance();

        //creating the welcome message

        String userID = fAuth.getCurrentUser().getUid();

        final DocumentReference documentReference = fStore.collection("users").document(userID);

        System.out.println("THE ID IS "+userID);

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

                    System.out.println( "Welcome "+documentSnapshot.getString("firstName")+ " " + documentSnapshot.getString("lastname"));
                    welcome.setText(   "Welcome "+documentSnapshot.getString("firstName")+ " " + documentSnapshot.getString("lastname"));
                }
            }
        });





        createRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FindBranch.class));
                finish();
            }
        });


        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fAuth.signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });

        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ReviewBranch.class));
                finish();
            }
        });

        checkStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CheckStatus.class));
                finish();
            }
        });


    }
}