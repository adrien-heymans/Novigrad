package com.example.novigrad;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class service_available extends AppCompatActivity {

    Button add,remove;
    Spinner servicesAvailable;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String userID;
    TextView servicesText;
    final String[] temp = {" "};





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_available);

        add = findViewById(R.id.addButton);
        remove = findViewById(R.id.removeButton);
        servicesAvailable = findViewById(R.id.serviceAvailable);
        servicesText = findViewById(R.id.servicesText);


        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getUid();

        CollectionReference serviceCollection = fStore.collection("services");
        CollectionReference userCollection = fStore.collection("users");


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

                        final ArrayAdapter aa = new ArrayAdapter(service_available.this,android.R.layout.simple_spinner_item,total.split(","));

                        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        //Setting the ArrayAdapter data on the Spinner
                        // Specify the layout to use when the list of choices appears

                        servicesAvailable.setAdapter(aa);

                    }
                } ) ;



        System.out.println ("THE SERVICES ARE : "+getServices());



        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String service = servicesAvailable.getSelectedItem().toString();

                if (service.equals("Select service")){
                    return;
                }

                addService(service);
                startActivity(new Intent(getApplicationContext(),EmployeeWelcome.class));
                finish();


            }
        });



        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String service = servicesAvailable.getSelectedItem().toString();

                if (service.equals("Select service")){
                    return;
                }
                removeService(service);
                startActivity(new Intent(getApplicationContext(),EmployeeWelcome.class));
                finish();


            }
        });




    }

    public String getServices(){

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
                    String test="";
                    List<String> tmp = (List<String>) documentSnapshot.get("services");

                    //we can display those services in a good way on the TextView
                    servicesText.setText("");
                    for (int i =0;i<tmp.size();i++){
                        servicesText.setText(servicesText.getText()+"\n"+tmp.get(i));
                        test = test + tmp.get(i)+",";

                    }
                    temp[0]=test;



                }
            }
        }) ;

        return temp[0];
    }

    public void addService(String newService){


        String[] existingService = getServices().split(",");

        //this loop check if the service is already in the list
        for (int i =0;i<existingService.length;i++){
            if (existingService[i].equals(newService)){
                return;
            }
        }
        ArrayList<String> test = new ArrayList<>();

        //here we add the new service to the list
        String[] newArray =new  String[existingService.length+1];

        for (int i =0;i<existingService.length;i++){
            newArray[i]=existingService[i];
        }
        newArray[newArray.length-1]=newService;

        for (int i =0;i<newArray.length;i++){

            test.add(newArray[i]);
        }

        //now we have the new array with the new service added, we just need to add it to the document on the firestore

        Map<String,Object> service = new HashMap<>();

        //here we add the new user or employee to the firebase database
        System.out.println("adding the new list of service");
        service.put("services",test);


        fStore.collection("users").document(userID)
                .update(service)
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
    }

    public void removeService(String newService){

        String[] existingService = getServices().split(",");
        ArrayList<String> newlist = new ArrayList<>();

        //this loop check if the service is already in the list
        for (int i =0;i<existingService.length;i++){
            if (!(existingService[i].equals(newService))){
                newlist.add(existingService[i]);
            }
        }

        Map<String,Object> service = new HashMap<>();

        //here we add the new user or employee to the firebase database
        service.put("services",newlist);

        fStore.collection("users").document(userID)
                .update(service)
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

    }
}