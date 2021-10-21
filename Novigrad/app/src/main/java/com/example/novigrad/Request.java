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
import android.widget.Spinner;

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

import javax.annotation.Nullable;

public class Request extends AppCompatActivity {

    Spinner list;
    GrowableArray arrayID = new GrowableArray(5);


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference usersRef = db.collection("requests");

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);

        list = findViewById(R.id.dropDown1);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        final DocumentReference documentReference = fStore.collection("users").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                email = documentSnapshot.getString("email");

            }
        });
        usersRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        String name;
                        String total ="";

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Users user = documentSnapshot.toObject(Users.class);
                            user.setUserId(documentSnapshot.getId());
                            if(user.getBranchEmail().equals(email)){
                                arrayID.add(user.getUserId());
                                name = user.getUserId();
                                total += name + ",";
                            }

                        }

                        final ArrayAdapter aa = new ArrayAdapter(Request.this,android.R.layout.simple_spinner_item,total.split(","));

                        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        //Setting the ArrayAdapter data on the Spinner
                        // Specify the layout to use when the list of choices appears

                        list.setAdapter(aa);

                    }
                } ) ;
    }

    public void btnClick(View view){

        //get the id of the clicked item
        int pressID = view.getId();

        //determine witch item was clicked and perform an action relative to that item
        switch (pressID){
            case  R.id.btnRequestApprove:
                RequestApprove();
                break;
            case R.id.btnRequestDeny:
                RequestDeny();
                break;
            case R.id.btnRequestReturn:
                returnToMenu();
                break;
            default:
                Log.d("Error", "Error: Unknown Button pressed!");
                break;
        }
    }
    private void RequestApprove(){
        final String name = list.getSelectedItem().toString().trim();

        db.document("requests/"+name).update("Status","Approved");

    }
    private void RequestDeny(){
        final String name = list.getSelectedItem().toString().trim();

        db.document("requests/"+name).update("Status","Denied");
    }
    private void returnToMenu(){
        startActivity(new Intent(getApplicationContext(),EmployeeWelcome.class));
        finish();
    }
}
