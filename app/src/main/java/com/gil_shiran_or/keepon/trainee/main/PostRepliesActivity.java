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
        setPost();
        adjustAddReplyButton();
        buildPostRepliesRecyclerView();
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.my_trainer_toolbar);
        toolbar.setTitle("Post Replies");
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void setPost() {
        ViewGroup postLayout = findViewById(R.id.post_layout);
        View postView = getLayoutInflater().inflate(R.layout.post_item, postLayout, false);

        postLayout.addView(postView, 0);

        CardView postCardView = findViewById(R.id.post_item);
        final CircleImageView authorCircleImageView = findViewById(R.id.post_author_img);
        final TextView authorTextView = findViewById(R.id.post_author);
        final TextView dateTextView = findViewById(R.id.post_date);
        final TextView titleTextView = findViewById(R.id.post_title);
        final TextView bodyTextView = findViewById(R.id.post_body);
        final Activity activity = this;

        postCardView.setClickable(false);
        postCardView.setFocusable(false);

        mValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Post post = dataSnapshot.getValue(Post.class);

                for (DataSnapshot usersLikedData : dataSnapshot.child("usersLiked").getChildren()) {
                    if (usersLikedData.child("userId").getValue(String.class).equals(mCurrentUserId)) {
                        post.setIsLiked(true);
                        break;
                    }
                }

                if (!post.getIsLiked()) {
                    for (DataSnapshot usersDislikedData : dataSnapshot.child("usersDisliked").getChildren()) {
                        if (usersDislikedData.child("userId").getValue(String.class).equals(mCurrentUserId)) {
                            post.setIsDisliked(true);
                            break;
                        }
                    }
                }

                mDatabaseTraineesReference.child(post.getUserId()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String author = dataSnapshot.child("Profile/name").getValue(String.class);
                        String authorImageUrl = dataSnapshot.child("Profile/profilePhotoUrl").getValue(String.class);

                        authorTextView.setText(author);
                        Picasso.with(activity).load(authorImageUrl).fit().into(authorCircleImageView);

                        mDatabaseTraineesReference.removeEventListener(this);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                titleTextView.setText(post.getTitle());
                dateTextView.setText(post.getDate());
                bodyTextView.setText(post.getBody());

                setPostImage(post);
                setPostLikes(post);
                setPostDislikes(post);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mDatabasePostReference.addValueEventListener(mValueEventListener);
    }

    private void setPostImage(Post post) {
        ImageView imageView = findViewById(R.id.post_img);

        if (!post.getImageUrl().isEmpty()) {
            imageView.setVisibility(View.VISIBLE);
            Picasso.with(this).load(post.getImageUrl()).fit().into(imageView);
        }
    }

    private void setPostLikes(final Post post) {
        final ImageView likeImageView = findViewById(R.id.post_like_img);
        final ImageView dislikeImageView = findViewById(R.id.post_dislike_img);
        TextView likesTextView = findViewById(R.id.post_likes);
        final Activity activity = this;

        likesTextView.setText(Integer.toString(post.getLikes()));

        if (post.getIsLiked()) {
            likeImageView.setImageDrawable(this.getResources().getDrawable(R.drawable.ic_like_pressed));
            likeImageView.setClickable(false);
            likeImageView.setFocusable(false);
            dislikeImageView.setClickable(false);
            dislikeImageView.setFocusable(false);
        } else if (!post.getIsDisliked()) {
            likeImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    likeImageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_like_pressed));
                    likeImageView.setClickable(false);
                    likeImageView.setFocusable(false);
                    dislikeImageView.setClickable(false);
                    dislikeImageView.setFocusable(false);

                    changePostLikesInFirebase(post);
                }
            });
        }
    }

    private void setPostDislikes(final Post post) {
        final ImageView likeImageView = findViewById(R.id.post_like_img);
        final ImageView dislikeImageView = findViewById(R.id.post_dislike_img);
        TextView dislikesTextView = findViewById(R.id.post_dislikes);
        final Activity activity = this;

        dislikesTextView.setText(Integer.toString(post.getDislikes()));

        if (post.getIsDisliked()) {
            dislikeImageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_dislike_pressed));
            likeImageView.setClickable(false);
            likeImageView.setFocusable(false);
            dislikeImageView.setClickable(false);
            dislikeImageView.setFocusable(false);
        } else if (!post.getIsLiked()) {
            dislikeImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dislikeImageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_dislike_pressed));
                    likeImageView.setClickable(false);
                    likeImageView.setFocusable(false);
                    dislikeImageView.setClickable(false);
                    dislikeImageView.setFocusable(false);

                    changePostDislikesInFirebase(post);
                }
            });
        }
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

    private void changePostLikesInFirebase(Post post) {
        String key = mDatabasePostReference.child("usersLiked").push().getKey();
        UserLikedDisliked userLikedDisliked = new UserLikedDisliked(mCurrentUserId);
        Map<String, Object> usersLikedValues = userLikedDisliked.toMap();
        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put("likes", post.getLikes() + 1);
        childUpdates.put("usersLiked/" + key, usersLikedValues);

        mDatabasePostReference.updateChildren(childUpdates);
    }

    private void changePostDislikesInFirebase(Post post) {
        String key = mDatabasePostReference.child("usersDisliked").push().getKey();
        UserLikedDisliked usersDislikedValues = new UserLikedDisliked(mCurrentUserId);
        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put("dislikes", post.getDislikes() + 1);
        childUpdates.put("usersDisliked/" + key, usersDislikedValues);

        mDatabasePostReference.updateChildren(childUpdates);
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
        mDatabasePostReference.removeEventListener(mValueEventListener);
        mRepliesListAdapter.cleanUp();
    }
}
