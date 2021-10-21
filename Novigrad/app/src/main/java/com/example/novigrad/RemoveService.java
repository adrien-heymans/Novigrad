package com.example.novigrad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import android.widget.Spinner;



import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;



public class RemoveService extends AppCompatActivity {

    Button remove;
    Spinner dropDown;
    FirebaseFirestore fStore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_service);

        //linking buttons

        remove =  findViewById(R.id.removeBtn);
        dropDown = findViewById(R.id.spinner1);



        //we have to create the list of sevrices that we want to remove to display it in the dropdown list

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
                        //we now have a long String containing for examepl "citizenship,G1 permit,G2 Permit"
                        //using the method .split(",") we transform this string in an array of 3 elements

                        final ArrayAdapter aa = new ArrayAdapter(RemoveService.this,android.R.layout.simple_spinner_item,total.split(","));

                        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        //Setting the ArrayAdapter data on the Spinner
                        // Specify the layout to use when the list of choices appears

                        dropDown.setAdapter(aa);

                    }
                } ) ;

        //when the user click the remove button this method will execute
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //getting the item selected on the dropdown list

                final String name = dropDown.getSelectedItem().toString().trim();

                //if the user did not select anything

                if (name.equals("Select service")){

                    return;
                }

                //this method will allow us to select the document within the database with the name selected form the dropdown list and then delete it
                fStore.collection("services").document(name)
                        .delete()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("TAG", "DocumentSnapshot successfully deleted!");

                                //we send the admin to the admin welcome page when the removal was succesfull
                                startActivity(new Intent(getApplicationContext(),AdminWelcome.class));
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("TAG", "Error deleting document", e);
                                //we send the admin to the admin welcome page when the removal was not successfull
                                startActivity(new Intent(getApplicationContext(),AdminWelcome.class));
                            }
                        });


            }
        });

    }






}
