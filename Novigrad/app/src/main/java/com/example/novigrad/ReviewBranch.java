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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class ReviewBranch extends AppCompatActivity {

    Button back,confirm;
    Spinner listOfBranch;
    EditText comment;
    RatingBar rating;
    String[] emailOfBranch ={""};
    String[] idOfBranch = {""};
    String[] fNameOfBranch ={""};
    String[] lNameOfBranch ={""};
    String[] reviews ={""};
    String[] numberOfReviews ={""};
    int index =0;

    FirebaseFirestore fStore;
    FirebaseAuth fAuth;
    CollectionReference usersRef;
    ArrayAdapter adapter,adapter1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_branch);

        back  = findViewById(R.id.back);
        confirm = findViewById(R.id.confirm);
        listOfBranch = findViewById(R.id.listOfBranch);
        comment = findViewById(R.id.comment);
        rating = findViewById(R.id.ratingBar);

        //getting the firebase values

        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();
        usersRef =  fStore.collection("users");

        //setting the spinner view

        adapter = new ArrayAdapter(ReviewBranch.this ,android.R.layout.simple_list_item_1, emailOfBranch);




        //here we retrived the data from the collection "services" on firestore and we put it in the drop down list
        usersRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        String fname,lname;
                        float rating;
                        String type;
                        String total ="Select Branch,";

                        final String[] temp1 = {""};
                        final String[] temp2 = {""};
                        final String[] temp3 = {""};
                        final String[] temp4 = {""};
                        final String[] temp5 = {""};
                        final String[] temp6 = {""};


                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                            type = documentSnapshot.getString("type");

                            if (type.equals("Employee")){
                                fname = documentSnapshot.getString("firstName");
                                rating = Float.parseFloat(documentSnapshot.getString("rating"));

                                lname = documentSnapshot.getString("lastname");

                                total += fname +" "+lname+" : "+rating+"/5"+  ",";
                                temp1[0] = temp1[0] + documentSnapshot.getId() + ",";
                                temp2[0] = temp2[0] + documentSnapshot.getString("firstName") + ",";
                                temp3[0] = temp3[0] + documentSnapshot.getString("lastname") + ",";
                                temp4[0] = temp4[0] + documentSnapshot.getString("email") + ",";
                                temp5[0] = temp5[0] + documentSnapshot.getString("rating") + ",";
                                temp6[0] = temp6[0] + documentSnapshot.getString("numberOfReviews") + ",";
                            }

                        }

                        idOfBranch = temp1[0].split(",");
                        fNameOfBranch= temp2[0].split(",");
                        lNameOfBranch= temp3[0].split(",");
                        emailOfBranch= temp4[0].split(",");
                        reviews = temp5[0].split(",");
                        numberOfReviews= temp6[0].split(",");


                        //we now have a long String containing for examepl "citizenship,G1 permit,G2 Permit"
                        //using the method .split(",") we transform this string in an array of 3 elements

                        final ArrayAdapter aa = new ArrayAdapter(ReviewBranch.this,android.R.layout.simple_spinner_item,total.split(","));

                        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        //Setting the ArrayAdapter data on the Spinner
                        // Specify the layout to use when the list of choices appears

                        listOfBranch.setAdapter(aa);

                    }
                } ) ;

        //back button

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CustomerWelcome.class));
                finish();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mcomment = comment.getText().toString().trim();
                float mrating = rating.getRating();


                //if no comment was provided

                if(TextUtils.isEmpty(mcomment)){
                    comment.setError("Please provide a feedback.");
                    return;
                }

                //checking if the entries are valid for the new name


                String userID = fAuth.getCurrentUser().getUid();

                Map<String,Object> review = new HashMap<>();
                System.out.println("HERE");
                System.out.println(fNameOfBranch[0] +" "+lNameOfBranch[0]+" : "+reviews[0]+"/5");
                //here we add the review  to the firebase database
                for (int i =0;i<emailOfBranch.length;i++) {
                    System.out.print(fNameOfBranch[i] +" "+lNameOfBranch[i]+" : "+reviews[i]+"/5");
                    if(listOfBranch.getSelectedItem().toString().trim().equals(fNameOfBranch[i] +" "+lNameOfBranch[i]+" : "+reviews[i]+"/5")){

                        System.out.println("Creating new review");
                        review.put("branchID",emailOfBranch[i]);
                        review.put("clientID",userID);

                        //we need to calculate the new rating

                        review.put("rating",String.valueOf(mrating));
                        review.put("comment",mcomment);

                        index =i;
                    }
                }
                fStore.collection("reviews").document()
                        .set(review)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("TAG", "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("TAG", "Error writing document", e);
                            }
                        });

                //we finally need to update the rating of the branch

                float previousRating = Float.parseFloat(reviews[index]);
                float newReview = mrating;
                float numberOfReviewBefore = Float.parseFloat(numberOfReviews[index]);
                float numberOfReviewAfter = numberOfReviewBefore+1;

                System.out.println("Current rating : "+previousRating+ " New Rating : "+mrating+" Number of Reviews Before : "+numberOfReviewBefore+" Number of Reviews After : "+numberOfReviewAfter );
                System.out.println("The value of the new rating will be : "+ (1/numberOfReviewAfter)*newReview + ((numberOfReviewAfter-1)/numberOfReviewAfter)*previousRating);





                //here we just calculate the previews rating + the new one and then we divide
                float newRate = ((Float.parseFloat(numberOfReviews[index])-1)/(Float.parseFloat(numberOfReviews[index])))*(Float.parseFloat(reviews[index])) + ((1/Float.parseFloat(numberOfReviews[index]))* mrating);


                System.out.print("THE NEW RATING IS :"+ newRate);


                fStore.collection("users").document(idOfBranch[index])
                        .update("rating",Float.toString(newRate))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("TAG", "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("TAG", "Error writing document", e);
                            }
                        });

                fStore.collection("users").document(idOfBranch[index])
                        .update("numberOfReviews",Integer.toString(1 + Integer.parseInt(numberOfReviews[index])))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("TAG", "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("TAG", "Error writing document", e);
                            }
                        });
                comment.setText(" ");
                index=0;
                startActivity(new Intent(getApplicationContext(), CustomerWelcome.class));
                finish();
            }







        });








    }

}