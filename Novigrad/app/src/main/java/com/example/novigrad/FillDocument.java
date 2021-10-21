package com.example.novigrad;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

public class FillDocument extends AppCompatActivity implements View.OnClickListener {

    private String branchID;
    private String selectServiceSpinner;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();;
    private DocumentReference servicesRef, usersRef;
    private TextView textViewData;
    String data="";
    private Uri filePath;
    private static final int PICK_DOC_REQUEST = 234;
    private Button buttonChoose;
    private Button buttonUpload;
    private Button buttonBack;
    private Button buttonWelcomePage;

    FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference storageReference = storage.getReference();
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;
    String firstName;
    String lastName;
    String customerEmail;
    String branchEmail;
    String requestID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //starts the activity and shows the layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_document);

        Intent intent = getIntent();
        branchID = intent.getStringExtra("branchSelected");
        selectServiceSpinner = intent.getStringExtra("Selected Service");
        servicesRef = db.document("services/"+selectServiceSpinner);
        textViewData = findViewById(R.id.text_view_data);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        final DocumentReference documentReference = fStore.collection("users").document(userID);
        final DocumentReference documentReference2 = fStore.collection("users").document(branchID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                firstName = documentSnapshot.getString("firstName");
                lastName = documentSnapshot.getString("lastname");
                customerEmail = documentSnapshot.getString("email");
            }
        });
        documentReference2.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                branchEmail = documentSnapshot.getString("email");
            }
        });

        servicesRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                data = "\nPlease upload these documents: \n(Only 1 pdf file is required)\n\n";
                if(documentSnapshot.getBoolean("criminalRecord")){
                    data+="Criminal record.\n";
                }
                if(documentSnapshot.getBoolean("driverLicense")){
                    data+="Driver license.\n";
                }
                if(documentSnapshot.getBoolean("passportScan")){
                    data+="Passport.\n";
                }
                if(documentSnapshot.getBoolean("personalInfo")){
                    data+="Personal Info.\n";
                }
                if(documentSnapshot.getBoolean("proofOfResidence")){
                    data+="Proof of residence.\n";
                }
                textViewData.setText(data);
            }
        });

        //getting views from layout
        buttonChoose = (Button) findViewById(R.id.selectFilesBtn);
        buttonUpload = (Button) findViewById(R.id.uploadBtn);
        buttonBack = (Button) findViewById(R.id.backBtn);
        buttonWelcomePage = (Button) findViewById(R.id.backWelcomePage);

        //attaching listener
        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
        buttonBack.setOnClickListener(this);
        buttonWelcomePage.setOnClickListener(this);
    }

    /**
     * Function called by every button on the activity select service layout
     * @param view the item that was clicked
     */
    public void onClick(View view)
    {
        if (view == buttonChoose) {
            showFileChooser();
        }
        //if the clicked button is upload
        else if (view == buttonUpload) {
            uploadFile();
        }else if(view == buttonBack) {
            finish();
        }else{
            startActivity(new Intent(getApplicationContext(), CustomerWelcome.class));
        }
    }


    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Document"), PICK_DOC_REQUEST);
    }
    private void uploadFile() {
        //if there is a file to upload
        Log.i("TAG1","HERE");
        if(filePath !=null){
            Log.i("TAG2","HERE2");
        }
        if (filePath != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();
            StorageReference riversRef = storageReference.child("Requests/"+selectServiceSpinner+" - "+firstName+" "+lastName+".pdf");
            Map<String,Object> request = new HashMap<>();
            String newName = firstName + " " + lastName + "'s Request <"+selectServiceSpinner+">";
            request.put("RequestID", UUID.randomUUID().toString());
            request.put("BranchEmail",branchEmail);
            request.put("CustomerEmail",customerEmail);
            request.put("CustomerID",userID);
            request.put("NameOfRequest",selectServiceSpinner);
            request.put("Status","Submitted");

            fStore.collection("requests").document(newName)
                    .set(request)
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
            riversRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying a success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying error message
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
          //  StorageReference riversRef = storageReference.child("Documents/"+selectServiceSpinner+".pdf");

        }
        //if there is not any file
        else {
            //you can display an error toast
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_DOC_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
           /* try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }*/
        }
    }


}
