package com.example.novigrad;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class CheckStatus extends AppCompatActivity {
    Button back;
    ListView listOfRequest;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    CollectionReference requestRef;
    String[] requestArr = {""};
    String[] statusOfRequest ={""};
    String[] iDOfClient = {""};
    String[] nameOfRequest ={""};
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_status);

        //we need to link the button together;

        back = findViewById(R.id.backBtn);
        listOfRequest = findViewById(R.id.listOfRequest);

        //let's instatiate the listView
        adapter = new ArrayAdapter(CheckStatus.this ,android.R.layout.simple_list_item_1, requestArr);
        listOfRequest.setAdapter(adapter);


        //we need all the firestaore instance and collection reference

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        requestRef = fStore.collection("requests");
        final String userID = fAuth.getUid();

        //now that it is done we have to gothrought all the request and get the ones associated with this person

        requestRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        final String[] temp1 = {""};
                        final String[] temp2 = {""};
                        final String[] temp3 = {""};

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                            String customerID = documentSnapshot.getString("CustomerID");
                            String status = documentSnapshot.getString("Status");
                            String nameOfRequest = documentSnapshot.getString("NameOfRequest");

                            System.out.println("HERE ");

                            if ( customerID.equals(userID) ){

                               System.out.print("Adding the new request");
                                //temp1[0] = temp1[0] + documentSnapshot.getString("CustomerID")+",";
                                temp2[0] = temp2[0] + documentSnapshot.getString("Status")+",";
                                temp3[0] = temp3[0] + documentSnapshot.getString("NameOfRequest")+",";
                            }

                        }


                        //iDOfClient= temp1[0].split(",");
                        statusOfRequest= temp2[0].split(",");
                        nameOfRequest = temp3[0].split(",");

                        //now we have 4, list each of them containing the email,fname,lname, and email of the branch that offer the desired service
                        //let's merge some of the information together to make it look mor intuitive for the user
                        //for example we could display this | Branch : <firstname> Email <email>

                        String[] toDisplay = new String[nameOfRequest.length];

                        for (int i=0;i<nameOfRequest.length;i++) {
                            toDisplay[i] = "Request : "+nameOfRequest[i]+"  |  Status : "+statusOfRequest[i];
                        }



                        adapter = new ArrayAdapter(CheckStatus.this ,android.R.layout.simple_list_item_1, toDisplay);
                        listOfRequest.setAdapter(adapter);

                    }
                } ) ;


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CustomerWelcome.class));
            }
        });

    }
}