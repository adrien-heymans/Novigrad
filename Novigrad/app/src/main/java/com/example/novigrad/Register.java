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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class Register extends AppCompatActivity {

    //instance variables needed, they are presents on the design

    EditText mFirstName,mLastName,mEmail,mPassword,mPhoneNumber,address1,address2,country,postalCode,city,password2;
    Button register;
    TextView mLoginBtn;
    ProgressBar progressBar;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    //initialising the drop down list where the user chose betweem user and employee
    String[] types = {"User","Employee"};
    Employee emp;
    Customer cust;

    //datastructure to store emplopyees and customers locally
    HashMap<String,Employee> employees = new HashMap<>();
    HashMap<String,Customer> customers = new HashMap<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //retrieving the elements of the design, connecting buttons, texview,etc
        mFirstName = findViewById(R.id.firstName);
        mLastName = findViewById(R.id.lastName);
        mPhoneNumber = findViewById(R.id.phoneNumber);
        mEmail = findViewById(R.id.email);
        mPassword = findViewById(R.id.password1);
        register = findViewById(R.id.register);
        progressBar = findViewById(R.id.progressBar);
        mLoginBtn = findViewById(R.id.mLogInBtn);
        address1 = findViewById(R.id.address1);
        address2 = findViewById(R.id.address2);
        city = findViewById(R.id.city);
        country = findViewById(R.id.country);
        postalCode = findViewById(R.id.postalCode);
        password2 = findViewById(R.id.password2);



        //we need to initalize the dropdown list where the user can decide if they are creating an employee accoutn or a user account
        final Spinner dropdown = (Spinner) findViewById(R.id.dropDown1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);






        //Firebase variable , the first one is used for authentication, the second one to store the data of the user on the Clours Firestore

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        //sending the user to the new activity when they don;t have an account
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
                finish();
            }
        });


        //if the user clicks on register we will create a new user
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //getting all the Strings entered by the user in the form

                final String email = mEmail.getText().toString().trim();
                final String password = mPassword.getText().toString().trim();
                final String fname = mFirstName.getText().toString();
                final String lname = mLastName.getText().toString();
                final String phone = mPhoneNumber.getText().toString();
                final String type = dropdown.getSelectedItem().toString();
                final String pass2 = password2.getText().toString();
                final String ad2 = address2.getText().toString();
                final String ad1 = address1.getText().toString();
                final String zip = postalCode.getText().toString();
                final String mcity = city.getText().toString();
                final String mcountry = country.getText().toString();




                //checking if the entries are valid for emails and password


                if(TextUtils.isEmpty(fname)){
                    mFirstName.setError("First name is Required.");
                    return;

                }
                if(TextUtils.isEmpty(lname)){
                    mLastName.setError("Last name is Required.");
                    return;

                }
                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required.");
                    return;

                }
                if(TextUtils.isEmpty(phone)){
                    mPhoneNumber.setError("Phone number is Required.");
                    return;
                }
                if(TextUtils.isEmpty(zip)){
                    postalCode.setError("Postal code is Required.");
                    return;
                }
                if(TextUtils.isEmpty(mcountry)){
                    country.setError("Country is Required.");
                    return;
                }
                if(TextUtils.isEmpty(mcity)){
                    city.setError("City is Required.");
                    return;
                }

                if(TextUtils.isEmpty(ad1)){
                    address1.setError("Adress is Required.");
                    return;
                }
                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is required.");
                    return;
                }
                if(TextUtils.isEmpty(pass2)){
                    password2.setError("Please confirm is password.");
                    return;
                }
                //the length of tha password needs to be at least 6
                if(password.length()<6){
                    mPassword.setError("Password must be more than 6 characters");
                    return;
                }
                if (!pass2.equals(password)){
                    password2.setError("Password don't match");
                    return;

                }

                //a simple progress bar that is turned on between the time the user clicks on register and the moment he is sent to the welcome page
                progressBar.setVisibility(View.VISIBLE);




                //firebase function taht will create a new user using its name and password (the one entered in the form)

                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if(task.isSuccessful()){
                            Toast.makeText(Register.this,"User created",Toast.LENGTH_SHORT);

                            //we add the data of the new user to the database
                            userID = fAuth.getCurrentUser().getUid();

                            DocumentReference documentReference = fStore.collection("users").document(userID);
                            //DocumentReference documentReference2 = fStore.collection("branch").document(email);

                            System.out.println("here 4");
                            Map<String,Object> user = new HashMap<>();
                           // Map<String,Object> branch = new HashMap<>();

                            //here we add the new user or employee to the firebase database
                            System.out.println("Creating new user");
                            user.put("firstName",fname);
                            user.put("lastname",lname);
                            user.put("email",email);
                            user.put("phone",phone);
                            user.put("password",password);
                            user.put("type",type);
                            user.put("city",mcity);
                            user.put("country",mcountry);
                            user.put("address1",ad1);
                            user.put("address2",ad2);
                            user.put("postalCode",zip);

                            //we also need to add the employee or customer in their respective
                            // hashmap, it will be useful in the future

                            if (type=="Employee"){
                                employees.put(phone,new Employee(fname,lname,phone,email,password));

                                user.put("services",Arrays.asList());

                                //Links an array to the employee for the store schedule
                                user.put("schedule", Arrays.asList("true;9:00;17:00" , "true;9:00;17:00", "true;9:00;17:00",
                                        "true;9:00;17:00", "true;9:00;17:00", "true;9:00;17:00", "true;9:00;17:00"));
                                user.put("rating","0");
                                user.put("numberOfReviews","0");

                            }
                            else {
                                customers.put(phone,new Customer(fname,lname,phone,email,password));
                            }

                            //the new entity has been added both locally and on the online database



                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override

                                //we are printing the succes in the consol
                                public void onSuccess(Void aVoid) {
                                    Log.d("TAG","onSuccess : user Profile is created for "+ userID);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                //we are printing the failure if applicable in the console
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("TAG","onFailure : "+e.toString());
                                }
                            });

                            if (type.equals("User")){
                                startActivity(new Intent(getApplicationContext(),CustomerWelcome.class));

                            }
                            else{
                                startActivity(new Intent(getApplicationContext(),EmployeeWelcome.class));
                            }

                            //sending the user the the welcome page


                        }else{

                            //if the task is not successful we print on the console that an error was found
                            System.out.println("error when  creating user");
                            Log.d("Error: ", task.getException().getLocalizedMessage());
                            Toast.makeText(Register.this,"Error !" + task.getException().getLocalizedMessage(),Toast.LENGTH_SHORT);

                            //we hide the progress bar
                            progressBar.setVisibility(View.GONE);


                        }




                    }
                });


            }
        });


    }

    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {
        Toast.makeText(getApplicationContext(), "Selected User: "+types[position] ,Toast.LENGTH_SHORT).show();
    }
}