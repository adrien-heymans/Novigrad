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
import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ManageAccounts extends AppCompatActivity {


    Spinner list;
    Button delete;

    GrowableArray arrayID = new GrowableArray(5);
    GrowableArray arrayEmails = new GrowableArray(5);
   // private TextView textViewData;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usersRef = db.collection("users");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_accounts);
      //  textViewData = findViewById(R.id.text_view_data);

        delete =  findViewById(R.id.deleteUser);
        list = findViewById(R.id.dropDown1);

        usersRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        String name;
                        String total ="";

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Users user = documentSnapshot.toObject(Users.class);
                            user.setUserId(documentSnapshot.getId());
                            if(user.getType().equals("administrator") == false){
                                arrayID.add(user.getUserId());
                                arrayEmails.add(user.getEmail());
                                name = user.getFirstName() +" "+ user.getLastName() + " <"+user.getEmail()+">";
                                total += name + ",";
                            }
                        }

                        //we now have a long String containing for examepl "citizenship,G1 permit,G2 Permit"
                        //using the method .split(",") we transform this string in an array of 3 elements

                        final ArrayAdapter aa = new ArrayAdapter(ManageAccounts.this,android.R.layout.simple_spinner_item,total.split(","));

                        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        //Setting the ArrayAdapter data on the Spinner
                        // Specify the layout to use when the list of choices appears

                        list.setAdapter(aa);

                    }
                } ) ;

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //getting the item selected on the dropdown list

                final String selected = list.getSelectedItem().toString().trim();
                String[] arr1 = selected.split("<");
                String email = arr1[1].substring(0,arr1[1].length()-1);
                System.out.println("THE EMAIL IS "+ email);

                //if the user did not select anything



                //this method will allow us to select the document within the database with the name selected form the dropdown list and then delete it
                db.collection("users").document(arrayID.elementAt(arrayEmails.search(email)))
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


