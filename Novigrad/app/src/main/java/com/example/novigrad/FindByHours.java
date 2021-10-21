package com.example.novigrad;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;

public class FindByHours extends AppCompatActivity {

    Button mondayOpen,tuesdayOpen,wednesdayOpen,thursdayOpen,fridayOpen,saturdayOpen,sundayOpen,back,confirm;
    String[] emailOfBranch ={""};
    String[] idOfBranch = {""};
    String[] fNameOfBranch ={""};
    String[] lNameOfBranch ={""};
    ListView listOfBranch;
    TextView selectTime;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    CollectionReference usersRef;
    ArrayAdapter adapter;
    private TimePickerDialog picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_by_hours);

        mondayOpen = findViewById(R.id.checkMonday);
        tuesdayOpen = findViewById(R.id.checkTuesday);
        wednesdayOpen = findViewById(R.id.checkWednesday);
        thursdayOpen = findViewById(R.id.checkThursday);
        fridayOpen = findViewById(R.id.checkFriday);
        saturdayOpen = findViewById(R.id.checkSaturday);
        sundayOpen = findViewById(R.id.checkSunday);
        back = findViewById(R.id.backBtn);;
        confirm = findViewById(R.id.confirmBtn);
        selectTime = findViewById(R.id.selectTime);
        listOfBranch = findViewById(R.id.listOfBranch);

        //we need to peapre the listView
        adapter = new ArrayAdapter(FindByHours.this ,android.R.layout.simple_list_item_1, emailOfBranch);

        //getting the firebase data

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        usersRef =  fStore.collection("users");


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FindBranch.class));
                finish();
            }
        });


        //showing the time choser when clicking on the textview

        selectTime.setInputType(InputType.TYPE_NULL);
        selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                picker = new TimePickerDialog(FindByHours.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                System.out.println(sHour+" "+sMinute);
                                selectTime.setText(sHour + ":" + sMinute);
                            }
                        }, hour, minutes, true);
                picker.show();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //first we need to get the value of all the checkbox and the textView

                final boolean monday = !mondayOpen.isSelected();
                final boolean tuesday = !tuesdayOpen.isSelected();
                final boolean wednesday = !wednesdayOpen.isSelected();
                final boolean thursday = !thursdayOpen.isSelected();
                final boolean friday = !fridayOpen.isSelected();
                final boolean saturday = !saturdayOpen.isSelected();
                final boolean sunday = !sundayOpen.isSelected();

                //we will need this arrays to retrieve the data from the branch

                final String[] temp1 = {""};
                final String[] temp2 = {""};
                final String[] temp3 = {""};
                final String[] temp4 = {""};


                final String time = selectTime.getText().toString().trim();

                if (time.equals("Click here to select time")){
                    return;
                }

                final String[] timeChosen = time.split(":");

                //now that we have all the data, we need to go throught the database amnd check each openning time of branches, per day and time.

                usersRef.get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                int a = 0;
                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {


                                    if (documentSnapshot.getString("type").equals("Employee")) {
                                        System.out.println("------------------------------");


                                        ArrayList<String> openHours = (ArrayList<String>) documentSnapshot.get("schedule");

                                        boolean alreadyChecked = false;

                                        //we have to make sure that the hours has been slected and that we do not have just "Open" or "Close" instead of the hours


                                        //we need to check the openning for monday

                                        System.out.println("DATA "+monday+" "+Boolean.parseBoolean(openHours.get(0).split(";")[0]));

                                        if (monday && Boolean.parseBoolean(openHours.get(0).split(";")[0]) && !alreadyChecked) {

                                            System.out.println("OPEN ON MONDAYS : "+ documentSnapshot.get("firstName"));

                                            String[] timeStart = openHours.get(0).split(";")[1].split(":");
                                            String[] timeEnd = openHours.get(0).split(";")[2].split(":");

                                            int time1 = Integer.parseInt(timeStart[0])*60 + Integer.parseInt(timeStart[1]);
                                            int time2 = Integer.parseInt(timeEnd[0])*60 + Integer.parseInt(timeEnd[1]);
                                            int time3 = Integer.parseInt(timeChosen[0])*60 + Integer.parseInt(timeChosen[1]);

                                            System.out.println("Time 1 : "+time1+ " Time 2 :m "+time2 + " Time 3 :"+time3);
                                            System.out.println("WITH TIME : "+timeStart[0]+":"+timeStart[1]+" to "+timeEnd[0]+":"+timeEnd[1]);

                                            if (time1<=time3 && time2>=time3){
                                                //if ((Integer.parseInt(timeStart[0]) < (Integer.parseInt(timeChosen[0])) && (Integer.parseInt(timeEnd[0]) > Integer.parseInt(timeChosen[0]))) && (Integer.parseInt(timeStart[1]) < (Integer.parseInt(timeChosen[0])) && (Integer.parseInt(timeEnd[1]) > Integer.parseInt(timeChosen[0])))) {
                                                  alreadyChecked = true;
                                                  System.out.println("MONDAYS CONFIRMED");
                                            }
                                        }
                                        if (tuesday && Boolean.parseBoolean(openHours.get(1).split(";")[0]) && !alreadyChecked) {

                                            System.out.println("OPEN ON TUEDAYS : "+ documentSnapshot.get("firstName"));

                                            String[] timeStart = openHours.get(1).split(";")[1].split(":");
                                            String[] timeEnd = openHours.get(1).split(";")[2].split(":");

                                            int time1 = Integer.parseInt(timeStart[0])*60 + Integer.parseInt(timeStart[1]);
                                            int time2 = Integer.parseInt(timeEnd[0])*60 + Integer.parseInt(timeEnd[1]);
                                            int time3 = Integer.parseInt(timeChosen[0])*60 + Integer.parseInt(timeChosen[1]);

                                            System.out.println("Time 1 : "+time1+ " Time 2 :m "+time2 + " Time 3 :"+time3);
                                            System.out.println("WITH TIME : "+timeStart[0]+":"+timeStart[1]+" to "+timeEnd[0]+":"+timeEnd[1]);

                                            if (time1<=time3 && time2>=time3){
                                                //if ((Integer.parseInt(timeStart[0]) < (Integer.parseInt(timeChosen[0])) && (Integer.parseInt(timeEnd[0]) > Integer.parseInt(timeChosen[0]))) && (Integer.parseInt(timeStart[1]) < (Integer.parseInt(timeChosen[0])) && (Integer.parseInt(timeEnd[1]) > Integer.parseInt(timeChosen[0])))) {
                                                alreadyChecked = true;
                                                System.out.println("TUEDAYS CONFIRMED");

                                            }


                                        }
                                        if (wednesday && Boolean.parseBoolean(openHours.get(2).split(";")[0]) && !alreadyChecked) {

                                            System.out.println("OPEN ON WENEDSDAY : "+ documentSnapshot.get("firstName"));

                                            String[] timeStart = openHours.get(2).split(";")[1].split(":");
                                            String[] timeEnd = openHours.get(2).split(";")[2].split(":");

                                            int time1 = Integer.parseInt(timeStart[0])*60 + Integer.parseInt(timeStart[1]);
                                            int time2 = Integer.parseInt(timeEnd[0])*60 + Integer.parseInt(timeEnd[1]);
                                            int time3 = Integer.parseInt(timeChosen[0])*60 + Integer.parseInt(timeChosen[1]);

                                            System.out.println("Time 1 : "+time1+ " Time 2 :m "+time2 + " Time 3 :"+time3);
                                            System.out.println("WITH TIME : "+timeStart[0]+":"+timeStart[1]+" to "+timeEnd[0]+":"+timeEnd[1]);

                                            if (time1<=time3 && time2>=time3){
                                                //if ((Integer.parseInt(timeStart[0]) < (Integer.parseInt(timeChosen[0])) && (Integer.parseInt(timeEnd[0]) > Integer.parseInt(timeChosen[0]))) && (Integer.parseInt(timeStart[1]) < (Integer.parseInt(timeChosen[0])) && (Integer.parseInt(timeEnd[1]) > Integer.parseInt(timeChosen[0])))) {
                                                alreadyChecked = true;
                                                System.out.println("WEDNESDAYS CONFIRMED");

                                            }


                                        }
                                        if (thursday && Boolean.parseBoolean(openHours.get(3).split(";")[0]) && !alreadyChecked) {

                                            System.out.println("OPEN ON THURSDAYS : "+ documentSnapshot.get("firstName"));

                                            String[] timeStart = openHours.get(3).split(";")[1].split(":");
                                            String[] timeEnd = openHours.get(3).split(";")[2].split(":");

                                            int time1 = Integer.parseInt(timeStart[0])*60 + Integer.parseInt(timeStart[1]);
                                            int time2 = Integer.parseInt(timeEnd[0])*60 + Integer.parseInt(timeEnd[1]);
                                            int time3 = Integer.parseInt(timeChosen[0])*60 + Integer.parseInt(timeChosen[1]);

                                            System.out.println("Time 1 : "+time1+ " Time 2 :m "+time2 + " Time 3 :"+time3);
                                            System.out.println("WITH TIME : "+timeStart[0]+":"+timeStart[1]+" to "+timeEnd[0]+":"+timeEnd[1]);

                                            if (time1<=time3 && time2>=time3){
                                                //if ((Integer.parseInt(timeStart[0]) < (Integer.parseInt(timeChosen[0])) && (Integer.parseInt(timeEnd[0]) > Integer.parseInt(timeChosen[0]))) && (Integer.parseInt(timeStart[1]) < (Integer.parseInt(timeChosen[0])) && (Integer.parseInt(timeEnd[1]) > Integer.parseInt(timeChosen[0])))) {
                                                alreadyChecked = true;
                                                System.out.println("THURSDAYS CONFIRMED");

                                            }


                                        }
                                        if (friday && Boolean.parseBoolean(openHours.get(4).split(";")[0]) && !alreadyChecked) {

                                            System.out.println("OPEN ON FRIDAYS : "+ documentSnapshot.get("firstName"));

                                            String[] timeStart = openHours.get(4).split(";")[1].split(":");
                                            String[] timeEnd = openHours.get(4).split(";")[2].split(":");

                                            int time1 = Integer.parseInt(timeStart[0])*60 + Integer.parseInt(timeStart[1]);
                                            int time2 = Integer.parseInt(timeEnd[0])*60 + Integer.parseInt(timeEnd[1]);
                                            int time3 = Integer.parseInt(timeChosen[0])*60 + Integer.parseInt(timeChosen[1]);

                                            System.out.println("Time 1 : "+time1+ " Time 2 :m "+time2 + " Time 3 :"+time3);
                                            System.out.println("WITH TIME : "+timeStart[0]+":"+timeStart[1]+" to "+timeEnd[0]+":"+timeEnd[1]);

                                            if (time1<=time3 && time2>=time3){
                                                //if ((Integer.parseInt(timeStart[0]) < (Integer.parseInt(timeChosen[0])) && (Integer.parseInt(timeEnd[0]) > Integer.parseInt(timeChosen[0]))) && (Integer.parseInt(timeStart[1]) < (Integer.parseInt(timeChosen[0])) && (Integer.parseInt(timeEnd[1]) > Integer.parseInt(timeChosen[0])))) {
                                                alreadyChecked = true;
                                                System.out.println("FRIDAYS CONFIRMED");

                                            }


                                        }
                                        if (saturday && Boolean.parseBoolean(openHours.get(5).split(";")[0]) && !alreadyChecked) {

                                            System.out.println("OPEN ON SATURDAYS : "+ documentSnapshot.get("firstName"));

                                            String[] timeStart = openHours.get(5).split(";")[1].split(":");
                                            String[] timeEnd = openHours.get(5).split(";")[2].split(":");

                                            int time1 = Integer.parseInt(timeStart[0])*60 + Integer.parseInt(timeStart[1]);
                                            int time2 = Integer.parseInt(timeEnd[0])*60 + Integer.parseInt(timeEnd[1]);
                                            int time3 = Integer.parseInt(timeChosen[0])*60 + Integer.parseInt(timeChosen[1]);

                                            System.out.println("Time 1 : "+time1+ " Time 2 :m "+time2 + " Time 3 :"+time3);
                                            System.out.println("WITH TIME : "+timeStart[0]+":"+timeStart[1]+" to "+timeEnd[0]+":"+timeEnd[1]);

                                            if (time1<=time3 && time2>=time3){
                                                //if ((Integer.parseInt(timeStart[0]) < (Integer.parseInt(timeChosen[0])) && (Integer.parseInt(timeEnd[0]) > Integer.parseInt(timeChosen[0]))) && (Integer.parseInt(timeStart[1]) < (Integer.parseInt(timeChosen[0])) && (Integer.parseInt(timeEnd[1]) > Integer.parseInt(timeChosen[0])))) {
                                                alreadyChecked = true;
                                                System.out.println("SATURDAYS CONFIRMED");

                                            }


                                        }
                                        if (sunday && Boolean.parseBoolean(openHours.get(6).split(";")[0]) && !alreadyChecked) {

                                            System.out.println("OPEN ON MONDAYS : "+ documentSnapshot.get("firstName"));

                                            String[] timeStart = openHours.get(6).split(";")[1].split(":");
                                            String[] timeEnd = openHours.get(6).split(";")[2].split(":");

                                            int time1 = Integer.parseInt(timeStart[0])*60 + Integer.parseInt(timeStart[1]);
                                            int time2 = Integer.parseInt(timeEnd[0])*60 + Integer.parseInt(timeEnd[1]);
                                            int time3 = Integer.parseInt(timeChosen[0])*60 + Integer.parseInt(timeChosen[1]);

                                            System.out.println("Time 1 : "+time1+ " Time 2 :m "+time2 + " Time 3 :"+time3);
                                            System.out.println("WITH TIME : "+timeStart[0]+":"+timeStart[1]+" to "+timeEnd[0]+":"+timeEnd[1]);

                                            if (time1<=time3 && time2>=time3){
                                                //if ((Integer.parseInt(timeStart[0]) < (Integer.parseInt(timeChosen[0])) && (Integer.parseInt(timeEnd[0]) > Integer.parseInt(timeChosen[0]))) && (Integer.parseInt(timeStart[1]) < (Integer.parseInt(timeChosen[0])) && (Integer.parseInt(timeEnd[1]) > Integer.parseInt(timeChosen[0])))) {
                                                alreadyChecked = true;
                                                System.out.println("SUNDAYS CONFIRMED");

                                            }


                                        }
                                        System.out.println("------------------------------");

                                        // if the branch matches the open hour selected

                                        if (alreadyChecked) {
                                            System.out.println(documentSnapshot.getString("email") + " : " + documentSnapshot.getId());
                                            temp1[0] = temp1[0] + documentSnapshot.getId() + ",";
                                            temp2[0] = temp2[0] + documentSnapshot.getString("firstName") + ",";
                                            temp3[0] = temp3[0] + documentSnapshot.getString("lastname") + ",";
                                            temp4[0] = temp4[0] + documentSnapshot.getString("email") + ",";


                                        }
                                    }

                                }
                                idOfBranch= temp1[0].split(",");
                                fNameOfBranch= temp2[0].split(",");
                                lNameOfBranch= temp3[0].split(",");
                                emailOfBranch= temp4[0].split(",");



                                //now we have 4, list each of them containing the email,fname,lname, and email of the branch that are open on the desired day
                                //let's merge some of the information together to make it look mor intuitive for the user
                                //for example we could display this | Branch : <firstname> Email <email>

                                String[] toDisplay = new String[idOfBranch.length];

                                for (int i=0;i<idOfBranch.length;i++) {
                                    toDisplay[i] = "Branch : "+fNameOfBranch[i]+"  |  Contact : "+emailOfBranch[i];

                                }


                                System.out.println("THE RETRIEVED LIST IS : "+ temp4[0]);

                                adapter = new ArrayAdapter(FindByHours.this ,android.R.layout.simple_list_item_1, toDisplay);
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