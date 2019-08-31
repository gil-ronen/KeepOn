package com.gil_shiran_or.keepon.trainee.main;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gil_shiran_or.keepon.R;
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

public class RepliesListAdapter extends RecyclerView.Adapter<RepliesListAdapter.RepliesViewHolder> {

    private List<Reply> mRepliesList = new ArrayList<>();
    private DatabaseReference mDatabasePostRepliesReference;
    private DatabaseReference mDatabaseTraineesReference = FirebaseDatabase.getInstance().getReference().child("Users/Trainees");
    private ChildEventListener mChildEventListener;
    private String mCurrentUserId;
    private Fragment mMainFragment;

    public static class RepliesViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView authorCircleImageView;
        public TextView authorTextView;
        public TextView dateTextView;
        public TextView bodyTextView;
        public ImageView likeImageView;
        public TextView likesTextView;
        public ImageView dislikeImageView;
        public TextView dislikesTextView;

        public RepliesViewHolder(View itemView) {
            super(itemView);
            authorCircleImageView = itemView.findViewById(R.id.reply_author_img);
            authorTextView = itemView.findViewById(R.id.reply_author);
            dateTextView = itemView.findViewById(R.id.reply_date);
            bodyTextView = itemView.findViewById(R.id.reply_body);
            likeImageView = itemView.findViewById(R.id.reply_like_img);
            likesTextView = itemView.findViewById(R.id.reply_likes);
            dislikeImageView = itemView.findViewById(R.id.reply_dislike_img);
            dislikesTextView = itemView.findViewById(R.id.reply_dislikes);
        }
    }

    public RepliesListAdapter(Fragment mainFragment, String postId) {
        mMainFragment = mainFragment;
        mDatabasePostRepliesReference = FirebaseDatabase.getInstance().getReference().child("Posts/" + postId + "/replies");

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        mCurrentUserId = firebaseAuth.getCurrentUser().getUid();

        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Reply reply = dataSnapshot.getValue(Reply.class);

                for (DataSnapshot usersLikedData : dataSnapshot.child("usersLiked").getChildren()) {
                    reply.addUserToUsersLiked(usersLikedData.child("userId").getValue(String.class));
                }

                for (DataSnapshot usersDislikedData : dataSnapshot.child("usersDisliked").getChildren()) {
                    reply.addUserToUsersDisliked(usersDislikedData.child("userId").getValue(String.class));
                }

                reply.setReplyId(dataSnapshot.getKey());
                mRepliesList.add(reply);
                notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for (int i = 0; i < mRepliesList.size(); i++) {
                    if (mRepliesList.get(i).getReplyId().equals(dataSnapshot.getKey())) {
                        Reply reply = dataSnapshot.getValue(Reply.class);

                        for (DataSnapshot data : dataSnapshot.child("usersLiked").getChildren()) {
                            reply.addUserToUsersLiked(data.child("userId").getValue(String.class));
                        }

                        for (DataSnapshot data : dataSnapshot.child("usersDisliked").getChildren()) {
                            reply.addUserToUsersDisliked(data.child("userId").getValue(String.class));
                        }

                        reply.setReplyId(dataSnapshot.getKey());
                        mRepliesList.set(i, reply);
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

        mDatabasePostRepliesReference.addChildEventListener(mChildEventListener);
    }

    @Override
    public RepliesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reply_item, parent, false);

        return new RepliesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RepliesViewHolder holder, int position) {
        final Reply currentReply = mRepliesList.get(position);

        setPostHeader(holder, currentReply);
        setPostLikes(holder, currentReply);
        setPostDislikes(holder, currentReply);
    }

    private void setPostHeader(final RepliesViewHolder holder, final Reply currentReply) {
        mDatabaseTraineesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String author = dataSnapshot.child(currentReply.getUserId() + "/username").getValue(String.class);
                String authorImageUrl = dataSnapshot.child(currentReply.getUserId() + "/profilePhotoUri").getValue(String.class);

                holder.authorTextView.setText(author);
                Picasso.with(mMainFragment.getContext()).load(authorImageUrl).fit().into(holder.authorCircleImageView);

                mDatabaseTraineesReference.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.dateTextView.setText(currentReply.getDate());
        holder.bodyTextView.setText(currentReply.getBody());
        holder.likeImageView.setImageDrawable(mMainFragment.getResources().getDrawable(R.drawable.ic_like));
        holder.dislikeImageView.setImageDrawable(mMainFragment.getResources().getDrawable(R.drawable.ic_dislike));
    }

    private void setPostLikes(final RepliesViewHolder holder, final Reply currentReply) {
        holder.likesTextView.setText(Integer.toString(currentReply.getLikes()));

        if (currentReply.isUsersLikedContainsUserId(mCurrentUserId)) {
            holder.likeImageView.setImageDrawable(mMainFragment.getResources().getDrawable(R.drawable.ic_like_pressed));
            holder.likeImageView.setClickable(false);
            holder.likeImageView.setFocusable(false);
            holder.dislikeImageView.setClickable(false);
            holder.dislikeImageView.setFocusable(false);
        } else if (!currentReply.isUsersDislikedContainsUserId(mCurrentUserId)) {
            holder.likeImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.likeImageView.setImageDrawable(mMainFragment.getResources().getDrawable(R.drawable.ic_like_pressed));
                    holder.likeImageView.setClickable(false);
                    holder.likeImageView.setFocusable(false);
                    holder.dislikeImageView.setClickable(false);
                    holder.dislikeImageView.setFocusable(false);

                    changeReplyLikesInFirebase(currentReply);
                }
            });
        }
    }

    private void setPostDislikes(final RepliesViewHolder holder, final Reply currentReply) {
        holder.dislikesTextView.setText(Integer.toString(currentReply.getDislikes()));

        if (currentReply.isUsersDislikedContainsUserId(mCurrentUserId)) {
            holder.dislikeImageView.setImageDrawable(mMainFragment.getResources().getDrawable(R.drawable.ic_dislike_pressed));
            holder.likeImageView.setClickable(false);
            holder.likeImageView.setFocusable(false);
            holder.dislikeImageView.setClickable(false);
            holder.dislikeImageView.setFocusable(false);
        } else if (!currentReply.isUsersLikedContainsUserId(mCurrentUserId)) {
            holder.dislikeImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.dislikeImageView.setImageDrawable(mMainFragment.getResources().getDrawable(R.drawable.ic_dislike_pressed));
                    holder.likeImageView.setClickable(false);
                    holder.likeImageView.setFocusable(false);
                    holder.dislikeImageView.setClickable(false);
                    holder.dislikeImageView.setFocusable(false);

                    changeReplyDislikesInFirebase(currentReply);
                }
            });
        }
    }

    private void changeReplyLikesInFirebase(Reply reply) {
        String key = mDatabasePostRepliesReference.child(reply.getReplyId() + "/usersLiked").push().getKey();
        UserLikedDisliked userLikedDisliked = new UserLikedDisliked(mCurrentUserId);
        Map<String, Object> usersLikedValues = userLikedDisliked.toMap();
        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put(reply.getReplyId() + "/likes", reply.getLikes() + 1);
        childUpdates.put(reply.getReplyId() + "/usersLiked/" + key, usersLikedValues);

        mDatabasePostRepliesReference.updateChildren(childUpdates);
    }

    private void changeReplyDislikesInFirebase(Reply reply) {
        String key = mDatabasePostRepliesReference.child(reply.getReplyId() + "/usersDisliked").push().getKey();
        UserLikedDisliked usersDislikedValues = new UserLikedDisliked(mCurrentUserId);
        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put(reply.getReplyId() + "/dislikes", reply.getDislikes() + 1);
        childUpdates.put(reply.getReplyId() + "/usersDisliked/" + key, usersDislikedValues);

        mDatabasePostRepliesReference.updateChildren(childUpdates);
    }

    @Override
    public int getItemCount() {
        return mRepliesList.size();
    }

    public void cleanUp() {
        mDatabasePostRepliesReference.removeEventListener(mChildEventListener);
    }
}