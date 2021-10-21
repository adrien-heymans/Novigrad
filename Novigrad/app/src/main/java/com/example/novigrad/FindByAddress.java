package com.example.novigrad;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class FindByAddress extends AppCompatActivity {

    public static String[] country = new String[]{"Afghanistan", "Albania", "Algeria", "American Samoa", "Andorra", "Angola", "Anguilla",
            "Antarctica", "Antigua and Barbuda", "Argentina", "Armenia", "Aruba", "Australia", "Austria",
            "Azerbaijan", "Bahamas", "Bahrain", "Bangladesh", "Barbados", "Belarus", "Belgium",
            "Belize", "Benin", "Bermuda", "Bhutan", "Bolivia", "Bosnia and Herzegovina", "Botswana",
            "Brazil", "British Indian Ocean Territory", "British Virgin Islands", "Brunei", "Bulgaria",
            "Burkina Faso", "Burma (Myanmar)", "Burundi", "Cambodia", "Cameroon", "Canada", "Cape Verde",
            "Cayman Islands", "Central African Republic", "Chad", "Chile", "China", "Christmas Island",
            "Cocos (Keeling) Islands", "Colombia", "Comoros", "Cook Islands", "Costa Rica",
            "Croatia", "Cuba", "Cyprus", "Czech Republic", "Democratic Republic of the Congo",
            "Denmark", "Djibouti", "Dominica", "Dominican Republic",
            "Ecuador", "Egypt", "El Salvador", "Equatorial Guinea", "Eritrea", "Estonia",
            "Ethiopia", "Falkland Islands", "Faroe Islands", "Fiji", "Finland", "France", "French Polynesia",
            "Gabon", "Gambia", "Gaza Strip", "Georgia", "Germany", "Ghana", "Gibraltar", "Greece",
            "Greenland", "Grenada", "Guam", "Guatemala", "Guinea", "Guinea-Bissau", "Guyana",
            "Haiti", "Holy See (Vatican City)", "Honduras", "Hong Kong", "Hungary", "Iceland", "India",
            "Indonesia", "Iran", "Iraq", "Ireland", "Isle of Man", "Israel", "Italy", "Ivory Coast", "Jamaica",
            "Japan", "Jersey", "Jordan", "Kazakhstan", "Kenya", "Kiribati", "Kosovo", "Kuwait",
            "Kyrgyzstan", "Laos", "Latvia", "Lebanon", "Lesotho", "Liberia", "Libya", "Liechtenstein",
            "Lithuania", "Luxembourg", "Macau", "Macedonia", "Madagascar", "Malawi", "Malaysia",
            "Maldives", "Mali", "Malta", "Marshall Islands", "Mauritania", "Mauritius", "Mayotte", "Mexico",
            "Micronesia", "Moldova", "Monaco", "Mongolia", "Montenegro", "Montserrat", "Morocco",
            "Mozambique", "Namibia", "Nauru", "Nepal", "Netherlands", "Netherlands Antilles", "New Caledonia",
            "New Zealand", "Nicaragua", "Niger", "Nigeria", "Niue", "Norfolk Island", "North Korea",
            "Northern Mariana Islands", "Norway", "Oman", "Pakistan", "Palau", "Panama",
            "Papua New Guinea", "Paraguay", "Peru", "Philippines", "Pitcairn Islands", "Poland",
            "Portugal", "Puerto Rico", "Qatar", "Republic of the Congo", "Romania", "Russia", "Rwanda",
            "Saint Barthelemy", "Saint Helena", "Saint Kitts and Nevis", "Saint Lucia", "Saint Martin",
            "Saint Pierre and Miquelon", "Saint Vincent and the Grenadines", "Samoa", "San Marino",
            "Sao Tome and Principe", "Saudi Arabia", "Senegal", "Serbia", "Seychelles", "Sierra Leone",
            "Singapore", "Slovakia", "Slovenia", "Solomon Islands", "Somalia", "South Africa", "South Korea",
            "Spain", "Sri Lanka", "Sudan", "Suriname", "Swaziland", "Sweden", "Switzerland",
            "Syria", "Taiwan", "Tajikistan", "Tanzania", "Thailand", "Timor-Leste", "Togo", "Tokelau",
            "Tonga", "Trinidad and Tobago", "Tunisia", "Turkey", "Turkmenistan", "Turks and Caicos Islands",
            "Tuvalu", "Uganda", "Ukraine", "United Arab Emirates", "United Kingdom", "United States", "Uruguay", "US Virgin Islands", "Uzbekistan", "Vanuatu", "Venezuela", "Vietnam",
            "Wallis and Futuna", "West Bank", "Yemen", "Zambia", "Zimbabwe"};

    Button back,confirm;
    EditText city;
    Spinner listOfCountry;
    String[] emailOfBranch ={""};
    String[] idOfBranch = {""};
    String[] fNameOfBranch ={""};
    String[] lNameOfBranch ={""};
    ListView listOfBranch;
    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    CollectionReference usersRef;
    ArrayAdapter adapter,adapter1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_by_address);

        back = findViewById(R.id.backBtn);
        confirm = findViewById(R.id.confirmBtn);
        listOfCountry = findViewById(R.id.listOfCountry);
        listOfBranch = findViewById(R.id.listOfBranch);
        city = findViewById(R.id.city);

        //we need to peapre the dropDown list
        adapter = new ArrayAdapter(FindByAddress.this ,android.R.layout.simple_list_item_1, country);
        adapter1 = new ArrayAdapter(FindByAddress.this ,android.R.layout.simple_list_item_1, emailOfBranch);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listOfCountry.setAdapter(adapter);

        //getting the firebase data

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        usersRef =  fStore.collection("users");

        //setting the back button

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FindBranch.class));
                finish();
            }
        });

        //setting the confirm button


        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final String[] temp1 = {""};
                final String[] temp2 = {""};
                final String[] temp3 = {""};
                final String[] temp4 = {""};

                final String countryChosen = listOfCountry.getSelectedItem().toString();
                final String cityChosen = city.getText().toString().trim();


                usersRef.get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                                    String countryOfDocument = documentSnapshot.getString("country");
                                    String cityOfDocument = documentSnapshot.getString("city");


                                    boolean isBranch = documentSnapshot.getString("type").equals("Employee");

                                    System.out.println("HERE ");

                                    if ( isBranch && countryChosen.equals(countryOfDocument) && cityOfDocument.equals(cityChosen)){

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

                                adapter1 = new ArrayAdapter(FindByAddress.this ,android.R.layout.simple_list_item_1, toDisplay);
                                listOfBranch.setAdapter(adapter1);

                            }
                        } ) ;
            }
        }) ;

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