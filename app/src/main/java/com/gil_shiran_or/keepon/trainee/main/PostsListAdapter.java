package com.gil_shiran_or.keepon.trainee.main;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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

public class PostsListAdapter extends BaseAdapter {

    private DatabaseReference mDatabasePostsReference;
    private DatabaseReference mDatabaseTraineesReference;
    private List<DataSnapshot> mPostsList;
    private Fragment mMainFragment;

    public PostsListAdapter(final Fragment mainFragment) {
        mDatabasePostsReference = FirebaseDatabase.getInstance().getReference().child("Posts");
        mDatabaseTraineesReference = FirebaseDatabase.getInstance().getReference().child("Users/Trainees");
        mPostsList = new ArrayList<>();
        mMainFragment = mainFragment;

        mDatabasePostsReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                mPostsList.add(0, dataSnapshot);
                notifyDataSetChanged();

                final ListView postsListView = mainFragment.getView().findViewById(R.id.posts_list);
                postsListView.post(new Runnable() {
                    @Override
                    public void run() {
                        postsListView.setSelection(0);
                    }
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for (int i = 0; i < mPostsList.size(); i++) {
                    if (mPostsList.get(i).getKey().equals(dataSnapshot.getKey())) {
                        mPostsList.set(i, dataSnapshot);
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
        public TextView titleTextView;
        public TextView bodyTextView;
        public TextView likesNumTextView;
        public TextView dislikesNumTextView;
        public ImageView likeImageView;
        public ImageView dislikeImageView;
        public TextView replyButtonTextView;
        public ListView repliesListView;
    }

    @Override
    public int getCount() {
        return mPostsList.size();
    }

    @Override
    public Post getItem(int position) {
        DataSnapshot dataSnapshot = mPostsList.get(position);
        Post post = dataSnapshot.getValue(Post.class);
        post.setPostId(dataSnapshot.getKey());

        return post;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);

            ViewHolder holder = new ViewHolder();

            holder.authorCircleImageView = convertView.findViewById(R.id.post_author_img);
            holder.authorTextView = convertView.findViewById(R.id.post_author);
            holder.dateTextView = convertView.findViewById(R.id.post_date);
            holder.titleTextView = convertView.findViewById(R.id.post_title);
            holder.bodyTextView = convertView.findViewById(R.id.post_body);
            holder.likesNumTextView = convertView.findViewById(R.id.post_likes);
            holder.dislikesNumTextView = convertView.findViewById(R.id.post_dislikes);
            holder.likeImageView = convertView.findViewById(R.id.post_like_img);
            holder.dislikeImageView = convertView.findViewById(R.id.post_dislike_img);
            holder.replyButtonTextView = convertView.findViewById(R.id.post_reply_button);
            holder.repliesListView = convertView.findViewById(R.id.post_replies_list);

            convertView.setTag(holder);
        }

        final Post post = getItem(position);
        final ViewHolder holder = (ViewHolder) convertView.getTag();

        RepliesListAdapter repliesListAdapter = new RepliesListAdapter(mMainFragment, post.getPostId());
        holder.repliesListView.setAdapter(repliesListAdapter);

        mDatabaseTraineesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String author = dataSnapshot.child(post.getUserId() + "/username").getValue(String.class);
                String authorImageUrl = dataSnapshot.child(post.getUserId() + "/profilePhotoUri").getValue(String.class);

                holder.authorTextView.setText(author);
                Picasso.with(mMainFragment.getContext()).load(authorImageUrl).fit().into(holder.authorCircleImageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        holder.dateTextView.setText(post.getDate());
        holder.titleTextView.setText(post.getTitle());
        holder.bodyTextView.setText(post.getBody());
        holder.likesNumTextView.setText(Integer.toString(post.getLikes()));
        holder.dislikesNumTextView.setText(Integer.toString(post.getDislikes()));

        holder.likeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.likeImageView.setImageDrawable(mMainFragment.getResources().getDrawable(R.drawable.ic_like_pressed));
                holder.likeImageView.setClickable(false);
                holder.likeImageView.setFocusable(false);
                holder.dislikeImageView.setClickable(false);
                holder.dislikeImageView.setFocusable(false);

                changePostLikesInFirebase(post);
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

                changePostDislikesInFirebase(post);
            }
        });

        holder.likeImageView.setImageDrawable(mMainFragment.getResources().getDrawable(R.drawable.ic_like));
        holder.dislikeImageView.setImageDrawable(mMainFragment.getResources().getDrawable(R.drawable.ic_dislike));

        if (post.getUsersLiked().containsValue("E0NB5lGKN2dCULl6yzAHTLCut862")) {
            holder.likeImageView.setImageDrawable(mMainFragment.getResources().getDrawable(R.drawable.ic_like_pressed));
            holder.likeImageView.setClickable(false);
            holder.likeImageView.setFocusable(false);
            holder.dislikeImageView.setClickable(false);
            holder.dislikeImageView.setFocusable(false);
        }

        if (post.getUsersDisliked().containsValue("E0NB5lGKN2dCULl6yzAHTLCut862")) {
            holder.dislikeImageView.setImageDrawable(mMainFragment.getResources().getDrawable(R.drawable.ic_dislike_pressed));
            holder.likeImageView.setClickable(false);
            holder.likeImageView.setFocusable(false);
            holder.dislikeImageView.setClickable(false);
            holder.dislikeImageView.setFocusable(false);
        }

        holder.replyButtonTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddReplyDialog addReplyDialog = new AddReplyDialog();

                addReplyDialog.setPostId(post.getPostId());
                addReplyDialog.setTargetFragment(mMainFragment, 0);
                addReplyDialog.show(mMainFragment.getFragmentManager(), "add reply dialog");
            }
        });

        return convertView;
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
        Map<String, Object> postValues = reply.toMap();
        Map<String, Object> childUpdates = new HashMap<>();

        childUpdates.put(postId + "/replies/" + key, postValues);

        mDatabasePostsReference.updateChildren(childUpdates);
    }

    private void changePostLikesInFirebase(Post post) {
        Map<String,Object> childUpdates = new HashMap<>();

        childUpdates.put(post.getPostId() + "/likes", post.getLikes() + 1);
        mDatabasePostsReference.updateChildren(childUpdates);
        mDatabasePostsReference.child(post.getPostId() + "/usersLiked/userId").setValue("E0NB5lGKN2dCULl6yzAHTLCut862");
    }

    private void changePostDislikesInFirebase(Post post) {
        Map<String,Object> childUpdates = new HashMap<>();

        childUpdates.put(post.getPostId() + "/dislikes", post.getDislikes() + 1);
        mDatabasePostsReference.updateChildren(childUpdates);
        mDatabasePostsReference.child(post.getPostId() + "/usersDisliked/userId").setValue("E0NB5lGKN2dCULl6yzAHTLCut862");
    }
}
