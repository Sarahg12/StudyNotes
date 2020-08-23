package com.example.studynotes;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PostActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PostsAdapter adapter;
    private List<Post> albumList;
    ConnectionClass connectionClass;
    String whichCourse;
    int courseHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initCollapsingToolbar();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        albumList = new ArrayList<>();
        adapter = new PostsAdapter(this, albumList);

        //TODO 2
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        //TODO 2
        recyclerView.addItemDecoration(new PostActivity.GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareAlbums();

whichCourse = MySharedPrefrence.getString(PostActivity.this,Constants.Keys.COURSE_NAME,"");
        switch(whichCourse) {
            case "Data Structures" :
                courseHeader=R.drawable.dsmod;
                break;
            case "Operating Systems" :
                courseHeader=R.drawable.oshed;
                break;
            case "Java" ://TODO
                courseHeader=R.drawable.jav;
                break;
            case "Software Testing" :
                courseHeader=R.drawable.sthed;
                break;
            case "Linear Algebra" :
                courseHeader=R.drawable.lghed;
                break;
            case "Web Application Development" :
                courseHeader=R.drawable.wdhed;
                break;
            case "Computer Organization" :
                courseHeader=R.drawable.comod;
                break;
            default :
                courseHeader=R.drawable.archhed;
        }

        try {
            //TODO
            Glide.with(this).load(courseHeader).into((ImageView) findViewById(R.id.backdrop));
        } catch (Exception e) {
            e.printStackTrace();
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostActivity.this, addPostActivity.class);
                startActivity(intent);            }
        });
    }

    /**
     * Initializing collapsing toolbar
     * Will show and hide the toolbar title on scroll
     */
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    /**
     * Adding few albums for testing
     */
    private void prepareAlbums() {


        try {
            connectionClass = new ConnectionClass();
            Connection con = connectionClass.CONN();


            if (con == null) {
                Toast.makeText(PostActivity.this, "Error...", Toast.LENGTH_LONG).show();

            } else {

                String courseNameSelected = MySharedPrefrence.getString(PostActivity.this, Constants.Keys.COURSE_NAME, "");
                Statement st = con.createStatement();
                String sql = ("SELECT * FROM Posts WHERE courseName = '" + courseNameSelected + "'");
                ResultSet rs = st.executeQuery(sql);
                while (rs.next()) {

                    String userName = rs.getString("userName");
                    String courseName = rs.getString("courseName");
                    String postTitle = rs.getString("postTitle");
                    String postText = rs.getString("postText");

                    Post a = new Post( userName, courseName, postTitle,  postText,userName+courseName+postTitle);
                    albumList.add(a);
                }
                adapter.notifyDataSetChanged();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column


            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    /**
     * Converting dp to pixel
     */
    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }


    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this,CourseActivity.class);
        startActivity(intent);
    }
}