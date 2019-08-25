package com.gil_shiran_or.keepon.trainee.ui.main;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.gil_shiran_or.keepon.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddReplyDialog extends AppCompatDialogFragment {

    private AddReplyListener listener;
    private PostItem postItem;

    private PostsListAdapter postsListAdapter;
    private ReplyItem replyItem;
    private String replyWriterName;
    private boolean isReplyToPost;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.add_reply_dialog, null);

        builder.setView(view)
                .setTitle("Reply")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText addReplyBodyEditText = view.findViewById(R.id.add_reply_body);

                        if (isReplyToPost) {
                            listener.applyReply(addReplyBodyEditText.getText().toString(), postItem.getPostWriterName(), postItem);
                        }
                        else {
                            listener.applyReply(addReplyBodyEditText.getText().toString(), replyItem.getReplyWriterName(), postItem);
                        }

                        /*if (isReplyToPost) {
                            postsListAdapter.addReplyToRepliesList(new ReplyItem(replyWriterName, formatter.format(date), "reply @" + post.getPostWriterName(),
                                    addReplyBodyEditText.getText().toString(), 0, 0), post);
                        }
                        else {
                            postsListAdapter.addReplyToRepliesList(new ReplyItem(replyWriterName, formatter.format(date), "reply @" + reply.getReplyWriterName(),
                                    addReplyBodyEditText.getText().toString(), 0, 0), post);
                        }*/
                    }
                });

        final AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.purple));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.purple));
            }
        });

        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (AddReplyDialog.AddReplyListener) getTargetFragment();
    }

    public interface AddReplyListener {
        void applyReply(String replyBody, String replyToWriterName, PostItem postItem);
    }

    public void setPostItem(PostItem postItem) {
        this.postItem = postItem;
    }

    public void setReplyItem(ReplyItem replyItem) {
        this.replyItem = replyItem;
    }

    public void setIsReplyToPost(boolean isReplyToPost) {
        this.isReplyToPost = isReplyToPost;
    }
}
