package com.gil_shiran_or.keepon;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;


public class MainChatActivity extends AppCompatActivity {

    // Add member variables here:
    private String mDisplayName;
    private ListView mChatListView;
    private EditText mInputText;
    private ImageButton mSendButton;
    private DatabaseReference mDatabaseReference;
    private ChatListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);

        // Set up the display name and get the Firebase reference
        setupDisplayName();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

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

    // Retrieve the display name from the Shared Preferences
    private void setupDisplayName(){
        SharedPreferences prefs = getSharedPreferences(RegisterActivity.CHAT_PREFS, MODE_PRIVATE);

        mDisplayName = prefs.getString(RegisterActivity.DISPLAY_NAME_KEY, null);

        if(mDisplayName == null) mDisplayName = "Anonymous";
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

        // Grab the text the user typed in and push the message to Firebase

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
