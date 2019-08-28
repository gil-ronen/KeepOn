package com.gil_shiran_or.keepon.trainee.main;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gil_shiran_or.keepon.R;
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

public class RepliesListAdapter extends BaseAdapter {

    private DatabaseReference mDatabaseRepliesReference;
    private DatabaseReference mDatabaseTraineesReference;
    private List<DataSnapshot> mRepliesList;
    private Fragment mMainFragment;

    public RepliesListAdapter(Fragment mainFragment, String postId) {
        mDatabaseRepliesReference = FirebaseDatabase.getInstance().getReference().child("Posts/" + postId + "/replies");
        mDatabaseTraineesReference = FirebaseDatabase.getInstance().getReference().child("Users/Trainees");
        mRepliesList = new ArrayList<>();
        mMainFragment = mainFragment;

        mDatabaseRepliesReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                mRepliesList.add(dataSnapshot);
                notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for (int i = 0; i < mRepliesList.size(); i++) {
                    if (mRepliesList.get(i).getKey().equals(dataSnapshot.getKey())) {
                        mRepliesList.set(i, dataSnapshot);
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
        });
    }

    private static class ViewHolder
    {
        public CircleImageView authorCircleImageView;
        public TextView authorTextView;
        public TextView dateTextView;
        public TextView bodyTextView;
        public TextView likesNumTextView;
        public TextView dislikesNumTextView;
        public ImageView likeImageView;
        public ImageView dislikeImageView;
    }

    @Override
    public int getCount() {
        return mRepliesList.size();
    }

    @Override
    public Reply getItem(int position) {
        DataSnapshot dataSnapshot = mRepliesList.get(position);
        Reply reply = dataSnapshot.getValue(Reply.class);
        reply.setReplyId(dataSnapshot.getKey());

        return reply;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.reply_item, parent, false);

            ViewHolder holder = new ViewHolder();

            holder.authorCircleImageView = convertView.findViewById(R.id.reply_author_img);
            holder.authorTextView = convertView.findViewById(R.id.reply_author);
            holder.dateTextView = convertView.findViewById(R.id.reply_date);
            holder.bodyTextView = convertView.findViewById(R.id.reply_body);
            holder.likesNumTextView = convertView.findViewById(R.id.reply_likes);
            holder.dislikesNumTextView = convertView.findViewById(R.id.reply_dislikes);
            holder.likeImageView = convertView.findViewById(R.id.reply_like_img);
            holder.dislikeImageView = convertView.findViewById(R.id.reply_dislike_img);

            convertView.setTag(holder);
        }

        final Reply reply = getItem(position);
        final ViewHolder holder = (ViewHolder) convertView.getTag();

        mDatabaseTraineesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String author = dataSnapshot.child(reply.getUserId() + "/username").getValue(String.class);
                String authorImageUrl = dataSnapshot.child(reply.getUserId() + "/profile_photo").getValue(String.class);

                holder.authorTextView.setText(author);
                Picasso.with(mMainFragment.getContext()).load(authorImageUrl).fit().into(holder.authorCircleImageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.dateTextView.setText(reply.getDate());
        holder.bodyTextView.setText(reply.getBody());
        holder.likesNumTextView.setText(Integer.toString(reply.getLikes()));
        holder.dislikesNumTextView.setText(Integer.toString(reply.getDislikes()));

        holder.likeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.likeImageView.setImageDrawable(mMainFragment.getResources().getDrawable(R.drawable.ic_like_pressed));
                holder.likeImageView.setClickable(false);
                holder.likeImageView.setFocusable(false);
                holder.dislikeImageView.setClickable(false);
                holder.dislikeImageView.setFocusable(false);

                changePostLikesInFirebase(reply);
            }
        });

        holder.dislikeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.dislikeImageView.setImageDrawable(mMainFragment.getResources().getDrawable(R.drawable.ic_dislike_pressed));
                holder.likeImageView.setClickable(false);
                holder.likeImageView.setFocusable(false);
                holder.dislikeImageView.setClickable(false);
                holder.dislikeImageView.setFocusable(false);

                changePostDislikesInFirebase(reply);
            }
        });

        holder.likeImageView.setImageDrawable(mMainFragment.getResources().getDrawable(R.drawable.ic_like));
        holder.dislikeImageView.setImageDrawable(mMainFragment.getResources().getDrawable(R.drawable.ic_dislike));

        if (reply.getUsersLiked().containsValue("5xpO2eIuy8S3bvxI34B8S973zi12")) {
            holder.likeImageView.setImageDrawable(mMainFragment.getResources().getDrawable(R.drawable.ic_like_pressed));
            holder.likeImageView.setClickable(false);
            holder.likeImageView.setFocusable(false);
            holder.dislikeImageView.setClickable(false);
            holder.dislikeImageView.setFocusable(false);
        }

        if (reply.getUsersDisliked().containsValue("5xpO2eIuy8S3bvxI34B8S973zi12")) {
            holder.dislikeImageView.setImageDrawable(mMainFragment.getResources().getDrawable(R.drawable.ic_dislike_pressed));
            holder.likeImageView.setClickable(false);
            holder.likeImageView.setFocusable(false);
            holder.dislikeImageView.setClickable(false);
            holder.dislikeImageView.setFocusable(false);
        }

        return convertView;
    }

    private void changePostLikesInFirebase(Reply reply) {
        Map<String,Object> childUpdates = new HashMap<>();

        childUpdates.put(reply.getReplyId() + "/likes", reply.getLikes() + 1);
        mDatabaseRepliesReference.updateChildren(childUpdates);
        mDatabaseRepliesReference.child(reply.getReplyId() + "/usersLiked/userId").setValue("5xpO2eIuy8S3bvxI34B8S973zi12");
    }

    private void changePostDislikesInFirebase(Reply reply) {
        Map<String,Object> childUpdates = new HashMap<>();

        childUpdates.put(reply.getReplyId() + "/dislikes", reply.getDislikes() + 1);
        mDatabaseRepliesReference.updateChildren(childUpdates);
        mDatabaseRepliesReference.child(reply.getReplyId() + "/usersDisliked/userId").setValue("5xpO2eIuy8S3bvxI34B8S973zi12");
    }
}
