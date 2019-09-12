package com.gil_shiran_or.keepon.chat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.gil_shiran_or.keepon.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MainChatActivity extends AppCompatActivity {

    // Add member variables here:
    private String mUserId1;
    private String mUserId2;
    private String mCurrentUserName = "";
    private String mUserType;
    private ListView mChatListView;
    private EditText mInputText;
    private ImageButton mSendButton;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseChatsReference;
    private DatabaseReference mDatabaseCurrentChatReference;
    private DatabaseReference mDatabaseCurrentUserReference;

    private ChatListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);


        Toolbar toolbar = findViewById(R.id.chat_toolbar);
        toolbar.setTitle("Chat");
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        mDatabaseChatsReference = FirebaseDatabase.getInstance().getReference().child("Chats");
        mAuth = FirebaseAuth.getInstance(); // Get hold of an instance of FirebaseAuth
        mUserId1 = mAuth.getCurrentUser().getUid(); // Current user ID
        mUserId2 = getIntent().getExtras().getString("otherUserId"); // The other side user ID
        mUserType = getIntent().getExtras().getString("currentUserType");
        mDatabaseCurrentUserReference = FirebaseDatabase.getInstance().getReference().child("Users/" + mUserType + "/" + mUserId1 + "/Profile");


        getChatRoomDatabaseReference();


        // Link the Views in the layout to the Java code
        mInputText = (EditText) findViewById(R.id.messageInput);
        mSendButton = (ImageButton) findViewById(R.id.sendButton);
        mChatListView = (ListView) findViewById(R.id.chat_list_view);

        // end the message when the "enter" button is pressed
        mInputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                sendMessage();
                return true;
            }
        });

        // Add an OnClickListener to the sendButton to send a message
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

    }


    private void getChatRoomDatabaseReference() {
        mDatabaseCurrentUserReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mCurrentUserName = dataSnapshot.child("name").getValue(String.class);
                mDatabaseCurrentUserReference.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabaseChatsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null)
                {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        if (dataSnapshot1.child("userId1").getValue() != null && dataSnapshot1.child("userId2").getValue() != null)
                        {
                            if ((mUserId1.equals(dataSnapshot1.child("userId1").getValue()) && mUserId2.equals(dataSnapshot1.child("userId2").getValue())) || (mUserId1.equals(dataSnapshot1.child("userId2").getValue()) && mUserId2.equals(dataSnapshot1.child("userId1").getValue()))) {
                                mDatabaseCurrentChatReference = dataSnapshot1.getRef();

                                mAdapter = new ChatListAdapter(MainChatActivity.this, dataSnapshot1.getRef(), mCurrentUserName);
                                mChatListView.setAdapter(mAdapter);
                                break;
                            }
                        }
                    }
                }

                if (mDatabaseCurrentChatReference == null) {
                    String key = mDatabaseChatsReference.push().getKey();

                    mDatabaseChatsReference.child(key).child("userId1").setValue(mUserId1);
                    mDatabaseChatsReference.child(key).child("userId2").setValue(mUserId2);

                    mDatabaseCurrentChatReference = mDatabaseChatsReference.child(key);

                    mAdapter = new ChatListAdapter(MainChatActivity.this, mDatabaseChatsReference.child(key), mCurrentUserName);
                    mChatListView.setAdapter(mAdapter);

                }

                mDatabaseChatsReference.removeEventListener(this);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }

        });
    }


    private void sendMessage() {

        String input = mInputText.getText().toString();
        Log.d("KeepOn", "I sent: " + input);

        if (!input.equals("")) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd'/'MM'/'y hh:mm");
            String time = simpleDateFormat.format(new Date());

            Message message = new Message(input, mCurrentUserName, time);
            mDatabaseCurrentChatReference.child("Messages").push().setValue(message);
            mInputText.setText("");
        }
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
    public void onDestroy() {
        super.onDestroy();
        mAdapter.clenup();
    }

}
