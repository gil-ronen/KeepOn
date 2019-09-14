package com.gil_shiran_or.keepon.trainee.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gil_shiran_or.keepon.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostsListAdapter extends RecyclerView.Adapter<PostsListAdapter.PostsViewHolder> {

    private List<Post> mPostsList = new ArrayList<>();
    private DatabaseReference mDatabasePostsReference = FirebaseDatabase.getInstance().getReference().child("Posts");
    private DatabaseReference mDatabaseTraineesReference = FirebaseDatabase.getInstance().getReference().child("Users/Trainees");
    private ChildEventListener mChildEventListener;
    private String mCurrentUserId;
    private Fragment mMainFragment;

    public static class PostsViewHolder extends RecyclerView.ViewHolder {

        public CardView postCardView;
        public CircleImageView authorCircleImageView;
        public TextView authorTextView;
        public TextView dateTextView;
        public TextView titleTextView;
        public TextView bodyTextView;
        public ImageView imageView;
        public ImageView likeImageView;
        public TextView likesTextView;
        public ImageView dislikeImageView;
        public TextView dislikesTextView;

        public PostsViewHolder(View itemView) {
            super(itemView);
            postCardView = itemView.findViewById(R.id.post_item);
            authorCircleImageView = itemView.findViewById(R.id.post_author_img);
            authorTextView = itemView.findViewById(R.id.post_author);
            dateTextView = itemView.findViewById(R.id.post_date);
            titleTextView = itemView.findViewById(R.id.post_title);
            bodyTextView = itemView.findViewById(R.id.post_body);
            imageView = itemView.findViewById(R.id.post_img);
            likeImageView = itemView.findViewById(R.id.post_like_img);
            likesTextView = itemView.findViewById(R.id.post_likes);
            dislikeImageView = itemView.findViewById(R.id.post_dislike_img);
            dislikesTextView = itemView.findViewById(R.id.post_dislikes);
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

                post.setPostId(dataSnapshot.getKey());
                mPostsList.add(0, post);
                notifyItemInserted(0);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for (int i = 0; i < mPostsList.size(); i++) {
                    if (mPostsList.get(i).getPostId().equals(dataSnapshot.getKey())) {
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

                        post.setPostId(dataSnapshot.getKey());
                        mPostsList.set(i, post);
                        notifyItemChanged(i);
                        break;
                    }
                }
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
        setPostImage(holder, currentPost);
        setPostLikes(holder, currentPost);
        setPostDislikes(holder, currentPost);
        setPostCardClick(holder, currentPost);
    }

    private void setPostHeader(final PostsViewHolder holder, final Post currentPost) {
        mDatabaseTraineesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String author = dataSnapshot.child(currentPost.getUserId() + "/Profile/name").getValue(String.class);
                String authorImageUrl = dataSnapshot.child(currentPost.getUserId() + "/Profile/profilePhotoUrl").getValue(String.class);

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

    private void setPostImage(PostsViewHolder holder, Post currentPost) {
        holder.imageView.setVisibility(View.GONE);

        if (!currentPost.getImageUrl().isEmpty()) {
            holder.imageView.setVisibility(View.VISIBLE);
            Picasso.with(mMainFragment.getContext()).load(currentPost.getImageUrl()).fit().into(holder.imageView);
        }
    }

    private void setPostLikes(final PostsViewHolder holder, final Post currentPost) {
        holder.likesTextView.setText(Integer.toString(currentPost.getLikes()));

        if (currentPost.getIsLiked()) {
            holder.likeImageView.setImageDrawable(mMainFragment.getResources().getDrawable(R.drawable.ic_like_pressed));
            holder.likeImageView.setClickable(false);
            holder.likeImageView.setFocusable(false);
            holder.dislikeImageView.setClickable(false);
            holder.dislikeImageView.setFocusable(false);
        } else if (!currentPost.getIsDisliked()) {
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

        if (currentPost.getIsDisliked()) {
            holder.dislikeImageView.setImageDrawable(mMainFragment.getResources().getDrawable(R.drawable.ic_dislike_pressed));
            holder.likeImageView.setClickable(false);
            holder.likeImageView.setFocusable(false);
            holder.dislikeImageView.setClickable(false);
            holder.dislikeImageView.setFocusable(false);
        } else if (!currentPost.getIsLiked()) {
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

    private void setPostCardClick(final PostsViewHolder holder, final Post currentPost) {
        holder.postCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("postId", currentPost.getPostId());
                Intent intent = new Intent(mMainFragment.getContext(), PostRepliesActivity.class);
                intent.putExtras(bundle);
                mMainFragment.getContext().startActivity(intent);
            }
        });
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

    public void setPostToFirebase(Post post, Uri imageUri) {
        String key = mDatabasePostsReference.push().getKey();

        if (imageUri != null) {
            uploadPostPhoto(post, key, imageUri);
        }
        else {
            Map<String, Object> postValues = post.toMap();
            Map<String, Object> childUpdates = new HashMap<>();

            childUpdates.put(key, postValues);

            mDatabasePostsReference.updateChildren(childUpdates);
        }

        final RecyclerView postsRecyclerView = mMainFragment.getView().findViewById(R.id.posts_list);
        postsRecyclerView.post(new Runnable() {
            @Override
            public void run() {
                postsRecyclerView.smoothScrollToPosition(0);
            }
        });
    }

    private void uploadPostPhoto(final Post post, final String postId, Uri imageUri) {
        StorageReference mStorage = FirebaseStorage.getInstance().getReference().child("posts_photos").child(postId);
        final StorageReference imageFilePath = mStorage.child(imageUri.getLastPathSegment());

        imageFilePath.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        post.setImageUrl(uri.toString());

                        Map<String, Object> postValues = post.toMap();
                        Map<String, Object> childUpdates = new HashMap<>();

                        childUpdates.put(postId, postValues);

                        mDatabasePostsReference.updateChildren(childUpdates);
                    }
                });
            }
        });
    }

    public void cleanUp() {
        mDatabasePostsReference.removeEventListener(mChildEventListener);
    }
}