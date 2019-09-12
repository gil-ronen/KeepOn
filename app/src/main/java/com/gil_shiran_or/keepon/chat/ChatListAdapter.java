package com.gil_shiran_or.keepon.chat;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gil_shiran_or.keepon.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class ChatListAdapter extends BaseAdapter {

    private Activity mActivity;
    private DatabaseReference mDatabaseReference;
    private String mThisUserName = "";
    private ArrayList<Message> mMessagesList;


    private ChildEventListener mListener = new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            Message message = new Message(dataSnapshot.child("message").getValue().toString(), dataSnapshot.child("author").getValue().toString(), dataSnapshot.child("time").getValue().toString());
            mMessagesList.add(message);
            notifyDataSetChanged();


        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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

    public ChatListAdapter(Activity activity, DatabaseReference ref, String thisUserName) {
        mActivity = activity;
        mThisUserName = thisUserName;
        mDatabaseReference = ref.child("Messages");
        mDatabaseReference.addChildEventListener(mListener);
        mMessagesList = new ArrayList<>();
    }

    static class ViewHolder {
        TextView authorName;
        TextView body;
        TextView time;
        LinearLayout.LayoutParams params;
    }


    @Override
    public int getCount() {

        return mMessagesList.size();
    }

    @Override
    public Message getItem(int position) {

        return mMessagesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.chat_msg_row, parent, false);

            final ViewHolder holder = new ViewHolder();
            holder.authorName = (TextView) convertView.findViewById(R.id.author);
            holder.body = (TextView) convertView.findViewById(R.id.message);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.params = (LinearLayout.LayoutParams) holder.authorName.getLayoutParams();
            convertView.setTag(holder);
        }

        final Message message = getItem(position);
        final ViewHolder holder = (ViewHolder) convertView.getTag();


        boolean isMe = mThisUserName.equals(message.getAuthor());


        setChatRowApperance(isMe, holder);

        String author = message.getAuthor();
        holder.authorName.setText(author);

        String msg = message.getMessage();
        holder.body.setText(msg);

        String time = message.getTime();
        holder.time.setText(time);


        return convertView;
    }

    private void setChatRowApperance(boolean isItMe, ViewHolder holder) {
        if (isItMe) {
            holder.params.gravity = Gravity.END;
            holder.authorName.setTextColor(Color.GREEN);
            holder.body.setBackgroundResource(R.drawable.bubble2);

        } else {
            holder.params.gravity = Gravity.START;
            holder.authorName.setTextColor(Color.BLUE);
            holder.body.setBackgroundResource(R.drawable.bubble1);
        }

        holder.authorName.setLayoutParams(holder.params);
        holder.body.setLayoutParams(holder.params);
        holder.time.setLayoutParams(holder.params);
    }

    public void clenup() {
        mDatabaseReference.removeEventListener(mListener);
    }
}