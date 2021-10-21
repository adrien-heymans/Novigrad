package com.example.novigrad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FindByServices extends AppCompatActivity {

    Spinner services;
    Button find;
    ListView listOfBranch;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    String userID,listOfService,serviceChosen;

    GrowableArray arrayID = new GrowableArray(5);
    CollectionReference usersRef;
    String[] emailOfBranch ={""};
    String[] idOfBranch = {""};
    String[] fNameOfBranch ={""};
    String[] lNameOfBranch ={""};
    ArrayAdapter adapter;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_by_services);

        services = findViewById(R.id.spinner);
        listOfBranch = findViewById(R.id.listOfBranch);
        find = findViewById(R.id.findBtn);

        //we display all the services in the drop down list

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getUid();


        CollectionReference serviceCollection = fStore.collection("services");
        usersRef =  fStore.collection("users");

        adapter = new ArrayAdapter(FindByServices.this ,android.R.layout.simple_list_item_1, emailOfBranch);




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

                        final ArrayAdapter aa = new ArrayAdapter(FindByServices.this,android.R.layout.simple_spinner_item,total.split(","));

                        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        //Setting the ArrayAdapter data on the Spinner
                        // Specify the layout to use when the list of choices appears

                        services.setAdapter(aa);

                    }
                } ) ;

        //we need to get the list of branch that proposes that service



        find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                

                final String[] temp1 = {""};
                final String[] temp2 = {""};
                final String[] temp3 = {""};
                final String[] temp4 = {""};

                serviceChosen = services.getSelectedItem().toString();


                usersRef.get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                    ArrayList<String> services = (ArrayList<String>) documentSnapshot.get("services");

                                    System.out.println("HERE ");

                                    if ( services !=null && services.contains(serviceChosen)){
                                        System.out.println(documentSnapshot.getString("email")+ " : "+documentSnapshot.getId());
                                        temp1[0] = temp1[0] + documentSnapshot.getId()+",";
                                        temp2[0] = temp2[0] + documentSnapshot.getString("firstName")+",";
                                        temp3[0] = temp3[0] + documentSnapshot.getString("lastname")+",";
                                        temp4[0] = temp4[0] + documentSnapshot.getString("email")+",";
                                    }

                                }

                                idOfBranch= temp1[0].split(",");
                                fNameOfBranch= temp2[0].split(",");
                                lNameOfBranch= temp3[0].split(",");
                                emailOfBranch= temp4[0].split(",");

                                //now we have 4, list each of them containing the email,fname,lname, and email of the branch that offer the desired service
                                //let's merge some of the information together to make it look mor intuitive for the user
                                //for example we could display this | Branch : <firstname> Email <email>

                                String[] toDisplay = new String[idOfBranch.length];

                                for (int i=0;i<idOfBranch.length;i++) {
                                    toDisplay[i] = "Branch : "+fNameOfBranch[i]+"  |  Contact : "+emailOfBranch[i];

                                }


                                System.out.println("THE RETRIEVED LIST IS : "+ temp4[0]);

                                adapter = new ArrayAdapter(FindByServices.this ,android.R.layout.simple_list_item_1, toDisplay);
                                listOfBranch.setAdapter(adapter);

                            }
                        } ) ;


            }
        });

        listOfBranch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String idOfSelectedBranch = idOfBranch[position];
                Intent intent = new Intent(getApplicationContext(), SelectService.class);
                intent.putExtra("branchSelected", idOfSelectedBranch);
                startActivity(intent);
            }
        });

    }


}