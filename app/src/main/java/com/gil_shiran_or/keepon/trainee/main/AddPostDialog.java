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

public class AddPostDialog extends AppCompatDialogFragment {

    private AddPostListener listener;
    private EditText addPostTitleEditText;
    private EditText addPostBodyEditText;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_post_dialog, null);

        addPostTitleEditText = view.findViewById(R.id.add_post_title);
        addPostBodyEditText = view.findViewById(R.id.add_post_body);

        builder.setView(view)
                .setTitle("New Post")
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
                        String addPostTitle = addPostTitleEditText.getText().toString().trim();
                        String addPostBody = addPostBodyEditText.getText().toString().trim();

                        if (addPostTitle.isEmpty() || addPostBody.isEmpty()) {
                            Toast.makeText(view.getContext(), "All fields must be filled", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            listener.applyPost(addPostTitleEditText.getText().toString(), addPostBodyEditText.getText().toString());
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
        listener = (AddPostDialog.AddPostListener) getTargetFragment();
    }

    public interface AddPostListener {
        void applyPost(String postTitle, String postBody);
    }
}
