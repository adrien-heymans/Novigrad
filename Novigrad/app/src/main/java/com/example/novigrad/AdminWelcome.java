package com.example.novigrad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.google.firebase.auth.FirebaseAuth;

public class AdminWelcome extends AppCompatActivity {

    Button create, remove, updateAvailableServices, manageAccounts,logOut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_welcome);

        create = (Button) findViewById(R.id.createService);
        remove = (Button) findViewById(R.id.removeServiceBtn);
        updateAvailableServices = (Button) findViewById(R.id.btnPlatformManagementUpdateServices);
        manageAccounts = (Button) findViewById(R.id.manageAccountsBtn);
        logOut = (Button) findViewById(R.id.logOutAdmin);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Creating a new service");

                startActivity(new Intent(getApplicationContext(), CreateService.class));
                finish();
            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Removing service");

                startActivity(new Intent(getApplicationContext(), RemoveService.class));
                finish();
            }
        });
        updateAvailableServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Sending  user to update service");
                startActivity(new Intent(getApplicationContext(), UpdateServices.class));
                finish();
            }
        });
        manageAccounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ManageAccounts.class));
            }
        });
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getApplicationContext(), Login.class));
                finish();
            }
        });
     }
}
