package com.gil_shiran_or.keepon.chat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.gil_shiran_or.keepon.R;
import com.gil_shiran_or.keepon.chat.ChatListAdapter;
import com.gil_shiran_or.keepon.chat.InstantMessage;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MainChatActivity extends AppCompatActivity {

    // Add member variables here:
    private String mDisplayName;
    private ListView mChatListView;
    private EditText mInputText;
    private ImageButton mSendButton;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mDatabaseUsersReference;
    private ChatListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);


        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mDatabaseUsersReference = FirebaseDatabase.getInstance().getReference().child("Users");
        mAuth = FirebaseAuth.getInstance(); // Get hold of an instance of FirebaseAuth

        getDisplayName();

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
        mSendButton.setOnClickListener(new View.OnClickListener()
        {
          @Override
          public void onClick(View v)
          {
              sendMessage();
          }
        });
    }


    // Retrieve the display name
    private void getDisplayName(){

        String user_id = mAuth.getCurrentUser().getUid();
        DatabaseReference current_user_db = mDatabaseUsersReference.child(user_id).child("username");

        current_user_db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mDisplayName = dataSnapshot.getValue().toString();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });

    }


    private void sendMessage() {

        String input = mInputText.getText().toString();
        Log.d("KeepOn", "I sent: " + input);

        if(!input.equals(""))
        {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a");
            String time = simpleDateFormat.format(new Date());

            InstantMessage chat = new InstantMessage(input, mDisplayName, time);
            mDatabaseReference.child("messages").push().setValue(chat);
            mInputText.setText("");
        }
    }

    // Override the onStart() lifecycle method. Setup the adapter here.
    @Override
    public void onStart()
    {
        super.onStart();
        mAdapter = new ChatListAdapter(this, mDatabaseReference, mDisplayName);
        mChatListView.setAdapter(mAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();

        // Remove the Firebase event listener on the adapter.
        mAdapter.clenup();
    }

}
