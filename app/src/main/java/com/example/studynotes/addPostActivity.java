package com.example.studynotes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class addPostActivity extends AppCompatActivity {

    EditText fname, title, text;
    String userName,courseName,postTitle,postText;


    Button btnup, submitButton;
    ImageView img_logo;
    Uri filePath;
    private final int PICK_IMAGE_REQUEST = 22;

    // instance for firebase storage and StorageReference
    FirebaseStorage storage;
    StorageReference storageReference;


    ConnectionClass connectionClass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        fname = findViewById(R.id.user_name);//user_name
        title = findViewById(R.id.post_title);//post_title
        text = findViewById(R.id.post_text);//post_text

        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        img_logo = (ImageView) findViewById(R.id.Imageprev);

        btnup = (Button) findViewById(R.id.upload);
        btnup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                galleryIntent();
                SelectImage();

            }
        });

        submitButton = findViewById(R.id.submit_button);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
if (checkDataEntered()){


                try {
                    connectionClass = new ConnectionClass();
                    Connection con = connectionClass.CONN();


                    if (con == null) {
                        Toast.makeText(addPostActivity.this, "Error...", Toast.LENGTH_LONG).show();

                    } else {
                         userName = fname.getText().toString();
                         courseName = MySharedPrefrence.getString(addPostActivity.this, Constants.Keys.COURSE_NAME, "");
                         postTitle = title.getText().toString();
                         postText = text.getText().toString();


                        Log.e("FF", courseName + "\n" + userName + "\n" + postTitle + "\n" + postText);

                        String query = "insert into Posts values ('" + userName + "' ,'" + courseName + "' ,'" + postTitle + "' ,'" + postText+ "' ,'" + userName+courseName+postTitle + "') ";

                        Statement stmt = con.createStatement();
                        stmt.executeUpdate(query);


                        uploadImage();

                        new CountDownTimer(5000, 1000) {
                            public void onTick(long millisUntilFinished) {
                            }

                            public void onFinish() {
                                Intent intent = new Intent(addPostActivity.this, PostActivity.class);
                                finish();
                                startActivity(intent);
                            }
                        }.start();



                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        }
    }

        );

        text.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (event.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });

    }

    // Select Image method
    private void SelectImage() {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    // Override onActivityResult method
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {

        super.onActivityResult(requestCode,
                resultCode,
                data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                img_logo.setImageBitmap(bitmap);
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    // UploadImage method
    private void uploadImage() {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            final ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            //TODO
            StorageReference ref
                    = storageReference
                    .child(
                            //String userName,courseName,postTitle,postText;

                            "images/"+userName+courseName+postTitle

                            // + UUID.randomUUID().toString()
                    );

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot) {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast
                                            .makeText(addPostActivity.this,
                                                    " Note has been submitted successfully.",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(addPostActivity.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int) progress + "%");
                                }
                            });
        }

    }



    public boolean checkDataEntered() {

        boolean flag = true;
        if (isEmpty(fname)) {

            fname.setError("Enter your name.");
            flag = false;
        }//end if

        if (isEmpty(title)) {

            title.setError("Enter note title.");
            flag = false;
        }//end if
        if (isEmpty(text)) {

            text.setError("Enter note description.");
            flag = false;
        }//end if

        if (filePath == null){
            //TODO if no pic
            Toast
                    .makeText(addPostActivity.this,
                            "Upload image please.",
                            Toast.LENGTH_SHORT)
                    .show();
            flag = false;
        }


        return flag;
    }//end checkDataEntered


    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }//end isEmpty





}