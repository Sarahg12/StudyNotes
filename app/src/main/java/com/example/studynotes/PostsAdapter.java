package com.example.studynotes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.MyViewHolder> {

    private Context mContext;
    private List<Post> albumList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, idofpost;
        public ImageView thumbnail;
        //, overflow;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            idofpost = (TextView) view.findViewById(R.id.idofpost);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
//            overflow = (ImageView) view.findViewById(R.id.overflow);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.getContext().startActivity(new Intent(view.getContext(), PostDetailActivity.class));
                    MySharedPrefrence.putString(view.getContext(),Constants.Keys.POST_ID,idofpost.getText().toString());
//                    MySharedPrefrence.putString(view.getContext(),Constants.Keys.COURSE_NAME,title.getText().toString());
                    //Post(String userName,String courseName,String postTitle, String postText)
//
//
//                    MySharedPrefrence.putString(mContext,Constants.Keys.USERNAME,album.getUserName());
//                    MySharedPrefrence.putString(mContext,Constants.Keys.COURSE_NAME,album.getCourseName());
//                    MySharedPrefrence.putString(mContext,Constants.Keys.POST_TITLE,album.getPostTitle());
//                    MySharedPrefrence.putString(mContext,Constants.Keys.POST_TEXT,album.getPostText());

                }
            });
        }
    }


    public PostsAdapter(Context mContext, List<Post> albumList) {
        this.mContext = mContext;
        this.albumList = albumList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.course_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Post album = albumList.get(position);
        holder.title.setText(album.getPostTitle());
        holder.idofpost.setText(album.getPostID());
//
//        MySharedPrefrence.putString(mContext,Constants.Keys.USERNAME,album.getUserName());
//        MySharedPrefrence.putString(mContext,Constants.Keys.COURSE_NAME,album.getCourseName());
//        MySharedPrefrence.putString(mContext,Constants.Keys.POST_TITLE,album.getPostTitle());
//        MySharedPrefrence.putString(mContext,Constants.Keys.POST_TEXT,album.getPostText());


        //Post(String userName,String courseName,String postTitle, String postText)


        FirebaseStorage storage = FirebaseStorage.getInstance();
        final StorageReference storageRef = storage.getReference();
        //"images/"+userName+courseName+postTitle
//        final StorageReference ImagesRef = storageRef.child("images/"+album.getUserName()+album.getCourseName()+album.getPostTitle());

        final StorageReference ImagesRef = storageRef.child("images/"+album.getPostID());

// Download directly from StorageReference using Glide
// (See MyAppGlideModule for Loader registration)
        Glide.with(mContext)
                .load(ImagesRef)
                .into(holder.thumbnail);

        // loading album cover using Glide library
//        Glide.with(mContext).load(album.getThumbnail()).into(holder.thumbnail);

    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
//    private void showPopupMenu(View view) {
//        // inflate menu
//        PopupMenu popup = new PopupMenu(mContext, view);
//        MenuInflater inflater = popup.getMenuInflater();
//        inflater.inflate(R.menu.menu_course, popup.getMenu());
//        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
//        popup.show();
//    }

    /**
     * Click listener for popup menu items
     */
//    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
//
//        public MyMenuItemClickListener() {
//        }
//
//        @Override
//        public boolean onMenuItemClick(MenuItem menuItem) {
//            switch (menuItem.getItemId()) {
//                case R.id.action_add_favourite:
//                    Toast.makeText(mContext, "Add to favourite", Toast.LENGTH_SHORT).show();
//                    return true;
//                case R.id.action_play_next:
//                    Toast.makeText(mContext, "Play next", Toast.LENGTH_SHORT).show();
//                    return true;
//                default:
//            }
//            return false;
//        }
//    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }
}
