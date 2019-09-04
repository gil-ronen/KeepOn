package com.gil_shiran_or.keepon.trainee.main;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gil_shiran_or.keepon.R;
import com.gil_shiran_or.keepon.trainee.utilities.ExpandableViewGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostsListAdapter extends RecyclerView.Adapter<PostsListAdapter.PostsViewHolder> {

    private List<Post> mPostsList = new ArrayList<>();
    private List<PostRepliesConnector> mPostRepliesConnectorsList = new ArrayList<>();
    private DatabaseReference mDatabasePostsReference = FirebaseDatabase.getInstance().getReference().child("Posts");
    private DatabaseReference mDatabaseTraineesReference = FirebaseDatabase.getInstance().getReference().child("Users/Trainees");
    private ChildEventListener mChildEventListener;
    private String mCurrentUserId;
    private Fragment mMainFragment;

    public static class PostsViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView authorCircleImageView;
        public TextView authorTextView;
        public TextView dateTextView;
        public TextView titleTextView;
        public TextView bodyTextView;
        public ViewGroup expanderViewGroup;
        public ImageView likeImageView;
        public TextView likesTextView;
        public ImageView dislikeImageView;
        public TextView dislikesTextView;
        public TextView replyButtonTextView;
        public RecyclerView repliesRecyclerView;

        public PostsViewHolder(View itemView) {
            super(itemView);
            authorCircleImageView = itemView.findViewById(R.id.post_author_img);
            authorTextView = itemView.findViewById(R.id.post_author);
            dateTextView = itemView.findViewById(R.id.post_date);
            titleTextView = itemView.findViewById(R.id.post_title);
            bodyTextView = itemView.findViewById(R.id.post_body);
            expanderViewGroup = itemView.findViewById(R.id.replies_expander);
            likeImageView = itemView.findViewById(R.id.post_like_img);
            likesTextView = itemView.findViewById(R.id.post_likes);
            dislikeImageView = itemView.findViewById(R.id.post_dislike_img);
            dislikesTextView = itemView.findViewById(R.id.post_dislikes);
            replyButtonTextView = itemView.findViewById(R.id.post_reply_button);
            repliesRecyclerView = itemView.findViewById(R.id.post_replies_list);
        }
    }

    public PostsListAdapter(Fragment mainFragment) {
        mMainFragment = mainFragment;

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        mCurrentUserId = firebaseAuth.getCurrentUser().getUid();

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Post post = dataSnapshot.getValue(Post.class);

                for (DataSnapshot usersLikedData : dataSnapshot.child("usersLiked").getChildren()) {
                    post.addUserToUsersLiked(usersLikedData.child("userId").getValue(String.class));
                }

                for (DataSnapshot usersDislikedData : dataSnapshot.child("usersDisliked").getChildren()) {
                    post.addUserToUsersDisliked(usersDislikedData.child("userId").getValue(String.class));
                }

                post.setPostId(dataSnapshot.getKey());
                mPostsList.add(0, post);
                notifyDataSetChanged();

                final RecyclerView postsRecyclerView = mMainFragment.getView().findViewById(R.id.posts_list);
                postsRecyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        postsRecyclerView.smoothScrollToPosition(0);
                    }
                });

                if (!mPostRepliesConnectorsList.isEmpty()) {
                    for (PostRepliesConnector postRepliesConnector : mPostRepliesConnectorsList) {
                        if (postRepliesConnector.getPostId().equals(post.getPostId())) {
                            postRepliesConnector.getRepliesListAdapter().notifyDataSetChanged();
                            break;
                        }
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for (int i = 0; i < mPostsList.size(); i++) {
                    if (mPostsList.get(i).getPostId().equals(dataSnapshot.getKey())) {
                        Post post = dataSnapshot.getValue(Post.class);

                        for (DataSnapshot data : dataSnapshot.child("usersLiked").getChildren()) {
                            post.addUserToUsersLiked(data.child("userId").getValue(String.class));
                        }

                        for (DataSnapshot data : dataSnapshot.child("usersDisliked").getChildren()) {
                            post.addUserToUsersDisliked(data.child("userId").getValue(String.class));
                        }

                        post.setPostId(dataSnapshot.getKey());
                        mPostsList.set(i, post);
                        break;
                    }
                }

                notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mDatabasePostsReference.addChildEventListener(mChildEventListener);
    }

    @Override
    public PostsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);

        return new PostsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PostsViewHolder holder, int position) {
        final Post currentPost = mPostsList.get(position);

        setPostHeader(holder, currentPost);
        setPostLikes(holder, currentPost);
        setPostDislikes(holder, currentPost);

        holder.replyButtonTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddReplyDialog addReplyDialog = new AddReplyDialog();

                addReplyDialog.setPostId(currentPost.getPostId());
                addReplyDialog.setTargetFragment(mMainFragment, 0);
                addReplyDialog.show(mMainFragment.getFragmentManager(), "add reply dialog");
            }
        });

        boolean isPostHasConnector = false;

        if (!mPostRepliesConnectorsList.isEmpty()) {
            for (PostRepliesConnector postRepliesConnector : mPostRepliesConnectorsList) {
                if (postRepliesConnector.getPostId().equals(currentPost.getPostId())) {
                    isPostHasConnector = true;
                    break;
                }
            }
        }

        if (!isPostHasConnector) {
            RepliesListAdapter repliesListAdapter = buildRepliesRecyclerView(holder, currentPost);
            PostRepliesConnector postRepliesConnector = new PostRepliesConnector(currentPost.getPostId(), repliesListAdapter);

            mPostRepliesConnectorsList.add(postRepliesConnector);

            new ExpandableViewGroup("View replies", "Hide replies", holder.expanderViewGroup, holder.repliesRecyclerView);
        }
    }

    private RepliesListAdapter buildRepliesRecyclerView(PostsViewHolder holder, Post currentPost) {

        holder.repliesRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mMainFragment.getContext());
        RepliesListAdapter repliesListAdapter = new RepliesListAdapter(mMainFragment, currentPost.getPostId());

        holder.repliesRecyclerView.setLayoutManager(layoutManager);
        holder.repliesRecyclerView.setAdapter(repliesListAdapter);

        return repliesListAdapter;
    }

    private void setPostHeader(final PostsViewHolder holder, final Post currentPost) {
        mDatabaseTraineesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String author = dataSnapshot.child(currentPost.getUserId() + "/username").getValue(String.class);
                String authorImageUrl = dataSnapshot.child(currentPost.getUserId() + "/profilePhotoUri").getValue(String.class);

                holder.authorTextView.setText(author);
                Picasso.with(mMainFragment.getContext()).load(authorImageUrl).fit().into(holder.authorCircleImageView);

                mDatabaseTraineesReference.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.dateTextView.setText(currentPost.getDate());
        holder.titleTextView.setText(currentPost.getTitle());
        holder.bodyTextView.setText(currentPost.getBody());
        holder.likeImageView.setImageDrawable(mMainFragment.getResources().getDrawable(R.drawable.ic_like));
        holder.dislikeImageView.setImageDrawable(mMainFragment.getResources().getDrawable(R.drawable.ic_dislike));
    }

    private void setPostLikes(final PostsViewHolder holder, final Post currentPost) {
        holder.likesTextView.setText(Integer.toString(currentPost.getLikes()));

        if (currentPost.isUsersLikedContainsUserId(mCurrentUserId)) {
            holder.likeImageView.setImageDrawable(mMainFragment.getResources().getDrawable(R.drawable.ic_like_pressed));
            holder.likeImageView.setClickable(false);
            holder.likeImageView.setFocusable(false);
            holder.dislikeImageView.setClickable(false);
            holder.dislikeImageView.setFocusable(false);
        } else if (!currentPost.isUsersDislikedContainsUserId(mCurrentUserId)) {
            holder.likeImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.likeImageView.setImageDrawable(mMainFragment.getResources().getDrawable(R.drawable.ic_like_pressed));
                    holder.likeImageView.setClickable(false);
                    holder.likeImageView.setFocusable(false);
                    holder.dislikeImageView.setClickable(false);
                    holder.dislikeImageView.setFocusable(false);

                    changePostLikesInFirebase(currentPost);
                }
            });
        }
    }

    private void setPostDislikes(final PostsViewHolder holder, final Post currentPost) {
        holder.dislikesTextView.setText(Integer.toString(currentPost.getDislikes()));

        if (currentPost.isUsersDislikedContainsUserId(mCurrentUserId)) {
            holder.dislikeImageView.setImageDrawable(mMainFragment.getResources().getDrawable(R.drawable.ic_dislike_pressed));
            holder.likeImageView.setClickable(false);
            holder.likeImageView.setFocusable(false);
            holder.dislikeImageView.setClickable(false);
            holder.dislikeImageView.setFocusable(false);
        } else if (!currentPost.isUsersLikedContainsUserId(mCurrentUserId)) {
            holder.dislikeImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.dislikeImageView.setImageDrawable(mMainFragment.getResources().getDrawable(R.drawable.ic_dislike_pressed));
                    holder.likeImageView.setClickable(false);
                    holder.likeImageView.setFocusable(false);
                    holder.dislikeImageView.setClickable(false);
                    holder.dislikeImageView.setFocusable(false);

                    changePostDislikesInFirebase(currentPost);
                }
            });
        }
    }

    private void changePostLikesInFirebase(Post post) {
        String key = mDatabasePostsReference.child(post.getPostId() + "/usersLiked").push().getKey();
        UserLikedDisliked userLikedDisliked = new UserLikedDisliked(mCurrentUserId);
        Map<String, Object> usersLikedValues = userLikedDisliked.toMap();
        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put(post.getPostId() + "/likes", post.getLikes() + 1);
        childUpdates.put(post.getPostId() + "/usersLiked/" + key, usersLikedValues);

        mDatabasePostsReference.updateChildren(childUpdates);
    }

    private void changePostDislikesInFirebase(Post post) {
        String key = mDatabasePostsReference.child(post.getPostId() + "/usersDisliked").push().getKey();
        UserLikedDisliked usersDislikedValues = new UserLikedDisliked(mCurrentUserId);
        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put(post.getPostId() + "/dislikes", post.getDislikes() + 1);
        childUpdates.put(post.getPostId() + "/usersDisliked/" + key, usersDislikedValues);

        mDatabasePostsReference.updateChildren(childUpdates);
    }

    @Override
    public int getItemCount() {
        return mPostsList.size();
    }

    public void setPostToFirebase(Post post) {
        String key = mDatabasePostsReference.push().getKey();
        Map<String, Object> postValues = post.toMap();
        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put(key, postValues);

        mDatabasePostsReference.updateChildren(childUpdates);
    }

    public void setReplyPostToFirebase(Reply reply, String postId) {
        String key = mDatabasePostsReference.child(postId + "/replies").push().getKey();
        Map<String, Object> replyValues = reply.toMap();
        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put(postId + "/replies/" + key, replyValues);

        mDatabasePostsReference.updateChildren(childUpdates);
    }

    public void cleanUp() {
        mDatabasePostsReference.removeEventListener(mChildEventListener);

        for (PostRepliesConnector connector : mPostRepliesConnectorsList) {
            connector.getRepliesListAdapter().cleanUp();
        }
    }
}