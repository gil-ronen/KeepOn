package com.gil_shiran_or.keepon.trainee.main;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gil_shiran_or.keepon.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostRepliesActivity extends AppCompatActivity implements AddReplyDialog.AddReplyListener {

    private DatabaseReference mDatabasePostReference;
    private DatabaseReference mDatabaseTraineesReference = FirebaseDatabase.getInstance().getReference().child("Users/Trainees");
    private RepliesListAdapter mRepliesListAdapter;
    private ValueEventListener mValueEventListener;
    private String mPostId;
    private String mCurrentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_replies);
        getWindow().setBackgroundDrawableResource(R.drawable.background_trainee);

        mPostId = getIntent().getExtras().getString("postId");
        mDatabasePostReference = FirebaseDatabase.getInstance().getReference().child("Posts/" + mPostId);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        mCurrentUserId = firebaseAuth.getCurrentUser().getUid();

        setToolbar();
        adjustAddReplyButton();
        buildPostRepliesRecyclerView();
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.post_replies_toolbar);
        toolbar.setTitle("Post Replies");
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void adjustAddReplyButton() {
        FloatingActionButton addReplyFloatingButton = findViewById(R.id.add_reply_button);
        final PostRepliesActivity activity = this;

        addReplyFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddReplyDialog addReplyDialog = new AddReplyDialog();

                addReplyDialog.show(activity.getSupportFragmentManager(), "add reply dialog");
            }
        });
    }

    private void buildPostRepliesRecyclerView() {
        RecyclerView postRepliesRecyclerView = findViewById(R.id.post_replies_list);
        postRepliesRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRepliesListAdapter = new RepliesListAdapter(this, mPostId);

        postRepliesRecyclerView.setLayoutManager(layoutManager);
        postRepliesRecyclerView.setAdapter(mRepliesListAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void applyReply(String replyBody) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

        mRepliesListAdapter.setReplyPostToFirebase(new Reply(mCurrentUserId, formatter.format(date), replyBody));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //mDatabasePostReference.removeEventListener(mValueEventListener);
        mRepliesListAdapter.cleanUp();
    }
}
