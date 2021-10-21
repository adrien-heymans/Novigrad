package com.example.novigrad;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Iterator;

public class SelectService extends AppCompatActivity {

    private String branchID;
    private Spinner selectServiceSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //starts the activity and shows the layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_service);

        //gets the branch selected ID
        Intent intent = getIntent();
        branchID = intent.getStringExtra("branchSelected");

        //get the spinner item
        selectServiceSpinner = (Spinner) findViewById(R.id.selectServiceSpinner);

        getServices(branchID, selectServiceSpinner);
    }

    /**
     * Function called by every button on the activity select service layout
     * @param view the item that was clicked
     */
    public void btnClick(View view)
    {
        //determine which button was selected and proceed with its purpose
        switch (view.getId())
        {
            //get the next function to execute, add the data to transfer, and then starts it
            case R.id.selectServiceBtnContinue:
                Intent intent = new Intent(getApplicationContext(), FillDocument.class);
                intent.putExtra("branchSelected", branchID);
                intent.putExtra("Selected Service", selectServiceSpinner.getSelectedItem().toString());

                //****************************************************************************
                startActivity(intent);
                break;
            //return to the previous layout
            case R.id.selectServiceBtnCancel:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * Get the services from the previously selected branch
     * @param branchID the ID of the selected branch
     * @param selectServiceSpinner the spinner object to which the services must be added
     */
    private void getServices(String branchID, final Spinner selectServiceSpinner)
    {
        //get the branch services from firebase
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference branch = firestore.collection("users");
        DocumentReference branchData = branch.document(branchID);

        //Event for when the services finished downloading
        branchData.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                //convert the data received back into an Arraylist and create an iterator for it
                ArrayList scheduleList = (ArrayList) value.get("services");
                Iterator iterator = scheduleList.iterator();

                //apped each service into a string
                StringBuilder services = new StringBuilder();
                while(iterator.hasNext())
                {
                    services.append(iterator.next());
                    services.append(":");
                }

                //create a Array for the spinner on the activity layout
                ArrayAdapter<String> servicesArray = new ArrayAdapter<>(SelectService.this,
                        android.R.layout.simple_spinner_item, services.toString().split(":"));

                //add the services to the spinner
                selectServiceSpinner.setAdapter(servicesArray);
            }
        });
    }
}
