package com.example.studynotes;

public class Post {

    private String userName;
    private String courseName;
    private String postTitle;
    private String postText;
    private String postID;


    public Post() {
    }

    public Post(String userName,String courseName,String postTitle, String postText,String postID) {
        this.userName = userName;
        this.courseName = courseName;
        this.postTitle = postTitle;
        this.postText = postText;
        this.postID= postID;

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }
}
