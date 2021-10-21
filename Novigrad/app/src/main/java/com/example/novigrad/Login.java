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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;

public class Login extends AppCompatActivity {
    
    //elements of the design like buttons, textview,progressbar etc...

    EditText mEmail,mPassword;
    Button mLoginBtn;
    TextView mCreateBtn;
    ProgressBar progressBar;
    final String[] type = {" "};
    
    //the firebase instance used to identify the user 
    FirebaseAuth fAuth;
    FirebaseFirestore fStore ;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //retrieving the elements of the design, connecting buttons, textview,etc

        mEmail = findViewById(R.id.email2);
        mPassword =findViewById(R.id.password2);
        progressBar = findViewById(R.id.progressBar2);
        fAuth = FirebaseAuth.getInstance();
        mLoginBtn = findViewById(R.id.register2);
        mCreateBtn = findViewById(R.id.needAccount);
        fStore = FirebaseFirestore.getInstance();

        final CollectionReference userRef = fStore.collection("users");

                //when the user doesn't have an account we send them to the register page
        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Register.class));
                finish();
            }
        });



        //after entering email and password, we log them in
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //if the password or email has not been entered we display an error 
                if (mEmail==null){
                    mEmail.setError("Email is required.");
                    return;

                }
               
                if(mPassword==null){
                    mPassword.setError("Password is required.");
                    return;
                }

                //we retrieve the string of the email and password entered by the user
                final String email = mEmail.getText().toString().trim();
                final String password = mPassword.getText().toString().trim();


                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required.");
                    return;

                }

                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is required.");
                    return;
                }

                //if the password is too short
                if(password.length()<6){
                    mPassword.setError("Password must be more than 6 characters");
                    return;
                }

                //we set the visibility of the progressbar to visible so the user does'nt think that the app froze
                progressBar.setVisibility(View.VISIBLE);

		System.out.println("");

                //authenticate the user

                fAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //if the email and password are correct, we send the user to the welcome page
                        if(task.isSuccessful()){

                            String id = fAuth.getUid();
                            System.out.println("THIS IS THE ID "+id);

                            userRef.get()
                                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                        @Override
                                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                            String type ="";
                                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                                                type  = documentSnapshot.getString("type");

                                                if (email.equals(documentSnapshot.getString("email"))){
                                                    if (type.equals("administrator")){
                                                        System.out.println("HERE 1");
                                                        Toast.makeText(Login.this,"Connection Successful",Toast.LENGTH_SHORT);
                                                        startActivity(new Intent(getApplicationContext(), AdminWelcome.class));
                                                    }
                                                    else if (type.equals("User")){
                                                        System.out.println("HERE 2");
                                                        Toast.makeText(Login.this,"Connection Successful",Toast.LENGTH_SHORT);
                                                        startActivity(new Intent(getApplicationContext(), CustomerWelcome.class));
                                                    }
                                                    else{
                                                        System.out.println("HERE 3");
                                                        Toast.makeText(Login.this,"Connection Successful",Toast.LENGTH_SHORT);
                                                        startActivity(new Intent(getApplicationContext(), EmployeeWelcome.class));
                                                    }
                                                }
                                            }
                                        }
                                    });
                        }
                        else{
                            //if the email and password are not correct, we "reset" the page
                            Toast.makeText(Login.this,"Error"+task.getException().getMessage(),Toast.LENGTH_SHORT);
                            progressBar.setVisibility(View.GONE);
                            mPassword.setError("Incorrect password or username.");

                        }
                    }
                });
            }
        });
    }
}
