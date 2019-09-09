package com.gil_shiran_or.keepon.trainee.main;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.gil_shiran_or.keepon.R;
import com.gil_shiran_or.keepon.login_register.TraineeDetailsRegister;
import com.gil_shiran_or.keepon.trainee.nav.TraineeNavActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;

public class AddPostDialog extends AppCompatDialogFragment {

    private static int PReqCode = 1;
    private static int REQUESCODE = 1;

    private AddPostListener mListener;
    private EditText mAddPostTitleEditText;
    private EditText mAddPostBodyEditText;
    private ViewGroup mAddImageButton;
    private ImageView mImageView;
    private Uri mPickedImgUri = null;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_post_dialog, null);

        mAddPostTitleEditText = view.findViewById(R.id.add_post_title);
        mAddPostBodyEditText = view.findViewById(R.id.add_post_body);
        mAddImageButton = view.findViewById(R.id.post_add_img_button);
        mImageView = view.findViewById(R.id.post_img);

        mAddImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 22) {
                    checkAndRequestForPermission();
                } else {
                    openGallery();
                }
            }
        });

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
                        String addPostTitle = mAddPostTitleEditText.getText().toString().trim();
                        String addPostBody = mAddPostBodyEditText.getText().toString().trim();

                        if (addPostTitle.isEmpty() || addPostBody.isEmpty()) {
                            Toast.makeText(view.getContext(), "All fields must be filled", Toast.LENGTH_SHORT).show();
                        } else {
                            mListener.applyPost(mAddPostTitleEditText.getText().toString(), mAddPostBodyEditText.getText().toString(), mPickedImgUri);
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
        mListener = (AddPostDialog.AddPostListener) getTargetFragment();
    }

    public interface AddPostListener {
        void applyPost(String postTitle, String postBody, Uri imageUri);
    }

    private void checkAndRequestForPermission() {

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {

                Toast.makeText(getContext(), "Please accept for required permission", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PReqCode);
            }
        } else
            openGallery();
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, REQUESCODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == REQUESCODE && data != null && data.getData() != null) {
            mPickedImgUri = data.getData();
            mImageView.setImageURI(mPickedImgUri);
        }
    }
}
