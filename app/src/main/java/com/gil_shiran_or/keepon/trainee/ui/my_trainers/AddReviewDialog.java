package com.gil_shiran_or.keepon.trainee.ui.my_trainers;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.gil_shiran_or.keepon.R;

public class AddReviewDialog extends AppCompatDialogFragment {

    private AddReviewListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.add_review_dialog, null);

        final RatingBar ratingBar = view.findViewById(R.id.add_review_rating);
        final TextView ratingState = view.findViewById(R.id.add_review_rating_state);
        final EditText reviewEditText = view.findViewById(R.id.add_review_review);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                if (ratingBar.getRating() <= 1) {
                    ratingState.setText(getResources().getString(R.string.rating_state_very_bad));
                }
                else if (ratingBar.getRating() <= 2) {
                    ratingState.setText(getResources().getString(R.string.rating_state_need_improvement));
                }
                else if (ratingBar.getRating() <= 3) {
                    ratingState.setText(getResources().getString(R.string.rating_state_good));
                }
                else if (ratingBar.getRating() <= 4) {
                    ratingState.setText(getResources().getString(R.string.rating_state_great));
                }
                else {
                    ratingState.setText(getResources().getString(R.string.rating_state_awesome));
                }
            }
        });

        builder.setView(view)
                .setTitle("New Review")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.applyReview(new MyTrainerReviewItem(ratingBar.getRating(), reviewEditText.getText().toString()));
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
        listener = (AddReviewListener) getTargetFragment();
    }

    public interface AddReviewListener {
        void applyReview(MyTrainerReviewItem myTrainerReviewItem);
    }
}
