package com.example.novigrad;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

/**
 * Allows the employee of a branch to change the hours of is store
 */
public class SetBranchHours extends AppCompatActivity {

    //private variables for the firebase data
    private FirebaseFirestore fStore;
    private FirebaseAuth fAuth;
    private String userID;
    private CollectionReference userCollection;
    private TimePickerDialog picker;

    //variables representing the editable text box and textview on branch_hours.xm

    private TextView mondayStart,mondayClose,tuesdayStart,tuesdayClose,wednesdayStart,wednesdayClose,thursdayStart,thursdayClose,fridayStart,fridayClose,saturdayStart,saturdayClose,sundayStart,sundayClose;
    private CheckBox mondayOpen,tuesdayOpen,wednesdayOpen,thursdayOpen,fridayOpen,saturdayOpen,sundayOpen;

    /**
     * First method executed when the class is called
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set the layout
        setContentView(R.layout.branch_hours);

        //get the instance of the necessary collection on Firebase
        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        //get the reference for the checkbox

        mondayOpen = findViewById(R.id.mondayOpen);
        tuesdayOpen = findViewById(R.id.tuesdayOpen);
        wednesdayOpen = findViewById(R.id.wednesdayOpen);
        thursdayOpen = findViewById(R.id.thursdayOpen);
        fridayOpen = findViewById(R.id.fridayOpen);
        saturdayOpen = findViewById(R.id.saturdayOpen);
        sundayOpen = findViewById(R.id.sundayOpen);

        //get the references for the editable text boxes

        mondayStart =  findViewById(R.id.mondayStart);
        tuesdayStart =  findViewById(R.id.tuesdayStart);
        wednesdayStart =  findViewById(R.id.wednesdayStart);
        thursdayStart =  findViewById(R.id.thursdayStart);
        fridayStart =  findViewById(R.id.fridayStart);
        saturdayStart =  findViewById(R.id.saturdayStart);
        sundayStart =  findViewById(R.id.sundayStart);

        mondayClose =  findViewById(R.id.mondayClose);
        tuesdayClose =  findViewById(R.id.tuesdayClose);
        wednesdayClose =  findViewById(R.id.wednesdayClose);
        thursdayClose =  findViewById(R.id.thursdayClose);
        fridayClose =  findViewById(R.id.fridayClose);
        saturdayClose =  findViewById(R.id.saturdayClose);
        sundayClose=  findViewById(R.id.sundayClose);

        //now we want to display the hours on the screen
        userCollection = fStore.collection("users");
        getHours();


        //now that we have all the button, checkbox and textview ready we need to create the time picker for each of them (14 times )



        mondayStart.setInputType(InputType.TYPE_NULL);
        mondayStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                picker = new TimePickerDialog(SetBranchHours.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                System.out.println(sHour+" "+sMinute);
                                mondayStart.setText(sHour + ":" + sMinute);
                            }
                        }, hour, minutes, true);
                picker.show();
            }
        });

        tuesdayStart.setInputType(InputType.TYPE_NULL);
        tuesdayStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                picker = new TimePickerDialog(SetBranchHours.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                System.out.println(sHour+" "+sMinute);
                                tuesdayStart.setText(sHour + ":" + sMinute);
                            }
                        }, hour, minutes, true);
                picker.show();
            }
        });

        wednesdayStart.setInputType(InputType.TYPE_NULL);
        wednesdayStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                picker = new TimePickerDialog(SetBranchHours.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                System.out.println(sHour+" "+sMinute);
                                wednesdayStart.setText(sHour + ":" + sMinute);
                            }
                        }, hour, minutes, true);
                picker.show();
            }
        });

        thursdayStart.setInputType(InputType.TYPE_NULL);
        thursdayStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                picker = new TimePickerDialog(SetBranchHours.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                System.out.println(sHour+" "+sMinute);
                                thursdayStart.setText(sHour + ":" + sMinute);
                            }
                        }, hour, minutes, true);
                picker.show();
            }
        });

        fridayStart.setInputType(InputType.TYPE_NULL);
        fridayStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                picker = new TimePickerDialog(SetBranchHours.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                System.out.println(sHour+" "+sMinute);
                                fridayStart.setText(sHour + ":" + sMinute);
                            }
                        }, hour, minutes, true);
                picker.show();
            }
        });

        saturdayStart.setInputType(InputType.TYPE_NULL);
        saturdayStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                picker = new TimePickerDialog(SetBranchHours.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                System.out.println(sHour+" "+sMinute);
                                saturdayStart.setText(sHour + ":" + sMinute);
                            }
                        }, hour, minutes, true);
                picker.show();
            }
        });

        sundayStart.setInputType(InputType.TYPE_NULL);
        sundayStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                picker = new TimePickerDialog(SetBranchHours.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                System.out.println(sHour+" "+sMinute);
                                sundayStart.setText(sHour + ":" + sMinute);
                            }
                        }, hour, minutes, true);
                picker.show();
            }
        });

        mondayClose.setInputType(InputType.TYPE_NULL);
        mondayClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                picker = new TimePickerDialog(SetBranchHours.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                System.out.println(sHour+" "+sMinute);
                                mondayClose.setText(sHour + ":" + sMinute);
                            }
                        }, hour, minutes, true);
                picker.show();
            }
        });

        tuesdayClose.setInputType(InputType.TYPE_NULL);
        tuesdayClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                picker = new TimePickerDialog(SetBranchHours.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                System.out.println(sHour+" "+sMinute);
                                tuesdayClose.setText(sHour + ":" + sMinute);
                            }
                        }, hour, minutes, true);
                picker.show();
            }
        });

        wednesdayClose.setInputType(InputType.TYPE_NULL);
        wednesdayClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                picker = new TimePickerDialog(SetBranchHours.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                System.out.println(sHour+" "+sMinute);
                                wednesdayClose.setText(sHour + ":" + sMinute);
                            }
                        }, hour, minutes, true);
                picker.show();
            }
        });

        thursdayClose.setInputType(InputType.TYPE_NULL);
        thursdayClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                picker = new TimePickerDialog(SetBranchHours.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                System.out.println(sHour+" "+sMinute);
                                thursdayClose.setText(sHour + ":" + sMinute);
                            }
                        }, hour, minutes, true);
                picker.show();
            }
        });

        fridayClose.setInputType(InputType.TYPE_NULL);
        fridayClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                picker = new TimePickerDialog(SetBranchHours.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                System.out.println(sHour+" "+sMinute);
                                fridayClose.setText(sHour + ":" + sMinute);
                            }
                        }, hour, minutes, true);
                picker.show();
            }
        });

        saturdayClose.setInputType(InputType.TYPE_NULL);
        saturdayClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                picker = new TimePickerDialog(SetBranchHours.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                System.out.println(sHour+" "+sMinute);
                                saturdayClose.setText(sHour + ":" + sMinute);
                            }
                        }, hour, minutes, true);
                picker.show();
            }
        });

        sundayClose.setInputType(InputType.TYPE_NULL);
        sundayClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                picker = new TimePickerDialog(SetBranchHours.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                System.out.println(sHour+" "+sMinute);
                                sundayClose.setText(sHour + ":" + sMinute);
                            }
                        }, hour, minutes, true);
                picker.show();
            }
        });








    }

    /**
     * Get the saved hours for the store saved on Firebase
     */
    private void getHours() {

        //get the reference of the current employee
        DocumentReference documentReference = userCollection.document(userID);

        //add an event listener for when the data is received
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {

            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                //if the user doesn't exist we just stopped the method
                if (fAuth==null){
                    return;
                }
                if(fStore==null){
                    return;
                }

                //if the data received was not null, set the content of each editable text box
                //with the data received
                if(documentSnapshot == null){
                    Log.i("Error; ", e.toString());
                }else {

                    List<String> tmp = (List<String>) documentSnapshot.get("schedule");
                    String[] mondaySchedule = tmp.get(0).split(";");
                    String[] tuesdaySchedule = tmp.get(1).split(";");
                    String[] wednesdaySchedule = tmp.get(2).split(";");
                    String[] thursdaySchedule = tmp.get(3).split(";");
                    String[] fridaySchedule = tmp.get(4).split(";");
                    String[] saturdaySchedule = tmp.get(5).split(";");
                    String[] sundaySchedule = tmp.get(6).split(";");

                    //getting if the day is closed or not

                    System.out.println("Adding the check box");

                    mondayOpen.setChecked(Boolean.parseBoolean(mondaySchedule[0]));
                    tuesdayOpen.setChecked(Boolean.parseBoolean(tuesdaySchedule[0]));
                    wednesdayOpen.setChecked(Boolean.parseBoolean(wednesdaySchedule[0]));
                    thursdayOpen.setChecked(Boolean.parseBoolean(thursdaySchedule[0]));
                    fridayOpen.setChecked(Boolean.parseBoolean(fridaySchedule[0]));
                    saturdayOpen.setChecked(Boolean.parseBoolean(saturdaySchedule[0]));
                    sundayOpen.setChecked(Boolean.parseBoolean(sundaySchedule[0]));

                    //getting openning hours

                    System.out.println("Adding the start time");

                    mondayStart.setText(mondaySchedule[1]);
                    tuesdayStart.setText(tuesdaySchedule[1]);
                    wednesdayStart.setText(wednesdaySchedule[1]);
                    thursdayStart.setText(thursdaySchedule[1]);
                    fridayStart.setText(fridaySchedule[1]);
                    saturdayStart.setText(saturdaySchedule[1]);
                    sundayStart.setText(sundaySchedule[1]);

                    //getting closing hours

                    System.out.println("Adding the close time");

                    mondayClose.setText(mondaySchedule[2]);
                    tuesdayClose.setText(tuesdaySchedule[2]);
                    wednesdayClose.setText(wednesdaySchedule[2]);
                    thursdayClose.setText(thursdaySchedule[2]);
                    fridayClose.setText(fridaySchedule[2]);
                    saturdayClose.setText(saturdaySchedule[2]);
                    sundayClose.setText(sundaySchedule[2]);
                }
            }
        });
    }

    /**
     * Determine which button was pressed and call the the associated method
     * @param view The button pressed
     */
    public void btnClick(View view){

        //get the id of the clicked item
        int pressID = view.getId();

        //determine witch item was clicked and perform an action relative to that item
        switch (pressID){
            case  R.id.btnBranchHoursReturn:
                returnToMenu();
                break;
            case R.id.btnBranchHoursSave:
                saveNewSchedule();
                break;
            default:
                Log.d("Error", "Error: Unknown Button pressed!");
                break;
        }
    }

    /**
     * Upload the data present in the editable text boxes to Firebase
     */
    private void saveNewSchedule(){

        //create a new Map to upload the data
        Map<String, Object> scheduleData = new HashMap<>();

        System.out.println("HERE PRINTING THE SCHEDULE");

        System.out.println(Arrays.asList(mondayOpen.isChecked()+";"+mondayStart.getText().toString()+";"+mondayClose.getText().toString(),
                tuesdayOpen.isChecked()+";"+tuesdayStart.getText().toString()+";"+tuesdayClose.getText().toString(),
                wednesdayOpen.isChecked()+";"+wednesdayStart.getText().toString()+";"+wednesdayClose.getText().toString(),
                thursdayOpen.isChecked()+";"+thursdayStart.getText().toString()+";"+thursdayClose.getText().toString(),
                fridayOpen.isChecked()+";"+fridayStart.getText().toString()+";"+fridayClose.getText().toString(),
                saturdayOpen.isChecked()+";"+saturdayStart.getText().toString()+";"+saturdayClose.getText().toString(),
                sundayOpen.isChecked()+";"+sundayStart.getText().toString()+";"+sundayClose.getText().toString()).toString());

        //set the data into the Map
       scheduleData.put("schedule", Arrays.asList(mondayOpen.isChecked()+";"+mondayStart.getText().toString()+";"+mondayClose.getText().toString(),
               tuesdayOpen.isChecked()+";"+tuesdayStart.getText().toString()+";"+tuesdayClose.getText().toString(),
               wednesdayOpen.isChecked()+";"+wednesdayStart.getText().toString()+";"+wednesdayClose.getText().toString(),
               thursdayOpen.isChecked()+";"+thursdayStart.getText().toString()+";"+thursdayClose.getText().toString(),
               fridayOpen.isChecked()+";"+fridayStart.getText().toString()+";"+fridayClose.getText().toString(),
               saturdayOpen.isChecked()+";"+saturdayStart.getText().toString()+";"+saturdayClose.getText().toString(),
               sundayOpen.isChecked()+";"+sundayStart.getText().toString()+";"+sundayClose.getText().toString()));

        //upload and merge the Map
        userCollection.document(userID).set(scheduleData, SetOptions.merge());
    }

    /**
     * End the current page and return the user to the previous layout
     */
    private void returnToMenu(){
        startActivity(new Intent(getApplicationContext(),EmployeeWelcome.class));
        finish();
    }
}
