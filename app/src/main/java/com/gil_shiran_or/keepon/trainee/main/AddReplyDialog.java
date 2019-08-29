package com.gil_shiran_or.keepon.trainee.main;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.gil_shiran_or.keepon.R;

public class AddReplyDialog extends AppCompatDialogFragment {

    private AddReplyListener listener;
    private String postId;
    private EditText addReplyBodyEditText;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.add_reply_dialog, null);

        addReplyBodyEditText = view.findViewById(R.id.add_reply_body);

        builder.setView(view)
                .setTitle("Reply")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("create", null);

        final AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.purple));
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.purple));

                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String addReplyBody = addReplyBodyEditText.getText().toString().trim();

                        if (addReplyBody.isEmpty()) {
                            Toast.makeText(view.getContext(), "All fields must be filled", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            listener.applyReply(addReplyBodyEditText.getText().toString(), postId);
                            dialog.dismiss();
                        }
                    }
                });
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
        void applyReply(String replyBody, String postId);
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}
