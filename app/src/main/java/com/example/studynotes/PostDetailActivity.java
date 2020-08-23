package com.example.studynotes;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PostDetailActivity extends AppCompatActivity {
    ImageView postImg;
    TextView post_title, post_author, post_text;

    private String postId;
    ConnectionClass connectionClass1;

    private Post postToRetrieve1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        postImg = findViewById(R.id.post_img);
        post_title=findViewById(R.id.post_title);
        post_author=findViewById(R.id.post_author);
        post_text=findViewById(R.id.post_text);


        postId = MySharedPrefrence.getString(PostDetailActivity.this, Constants.Keys.POST_ID, "");



        try {
            connectionClass1 = new ConnectionClass();
            Connection con = connectionClass1.CONN();


            if (con == null) {
                Toast.makeText(PostDetailActivity.this, "Error...", Toast.LENGTH_LONG).show();

            } else {

                String courseIdSelected = MySharedPrefrence.getString(PostDetailActivity.this, Constants.Keys.POST_ID, "");
                Statement st = con.createStatement();
                String sql = ("SELECT * FROM Posts WHERE ID = '" + courseIdSelected + "'");
                ResultSet rs = st.executeQuery(sql);
                while (rs.next()) {

                    String userName = rs.getString("userName");
                    String courseName = rs.getString("courseName");
                    String postTitle = rs.getString("postTitle");
                    String postText = rs.getString("postText");

                    postToRetrieve1 = new Post( userName, courseName, postTitle,  postText,userName+courseName+postTitle);
//                    albumList.add(a);
                }
//                adapter.notifyDataSetChanged();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

//            TextView post_title, post_author, post_text;

        post_title.setText(postToRetrieve1.getPostTitle());
        post_author.setText(postToRetrieve1.getUserName());
        post_text.setText(postToRetrieve1.getPostText());



        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReference();
        //"images/"+userName+courseName+postTitle
        final StorageReference ImagesRef = storageRef.child("images/"+postId);


// Download directly from StorageReference using Glide
// (See MyAppGlideModule for Loader registration)
        Glide.with(this)
                .load(ImagesRef)
                .into(postImg);


        FloatingActionButton fab = findViewById(R.id.postfab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostDetailActivity.this, CourseActivity.class);
                startActivity(intent);            }
        });
    }

    }
