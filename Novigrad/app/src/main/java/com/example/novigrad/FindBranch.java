package com.example.novigrad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FindBranch extends AppCompatActivity {

    Button findByHours,findByAddress,findByServices,back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_branch);

        findByAddress = findViewById(R.id.findByAddress);
        findByHours = findViewById(R.id.findByHours);
        findByServices = findViewById(R.id.findByServices);
        back = findViewById(R.id.backBtn);


        findByServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),FindByServices.class));
            }
        });
        findByHours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),FindByHours.class));
            }
        });
        findByAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),FindByAddress.class));
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CustomerWelcome.class));
            }
        });
    }
}