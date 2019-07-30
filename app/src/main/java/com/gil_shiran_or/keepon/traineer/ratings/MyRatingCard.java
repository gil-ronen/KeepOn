package com.gil_shiran_or.keepon.traineer.ratings;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gil_shiran_or.keepon.R;

public class MyRatingCard {

    private Context context;
    private LinearLayout subLayout;
    private CardView ratingCardView;
    private TextView trainerNameTextView;
    private RatingBar ratingBar;
    private TextView ratingStateTextView;
    private EditText reviewEditText;
    private Button sendButton;

    public MyRatingCard(Context context, LinearLayout rootLayout, String trainerName) {
        this.context = context;

        createRatingCardView();
        createSubLayout();
        createTrainerNameTextView(trainerName);
        createRatingBar();
        createRatingStateTextView();
        createReviewEditText();
        createSendButton();

        subLayout.addView(trainerNameTextView);
        subLayout.addView(ratingBar);
        subLayout.addView(ratingStateTextView);
        subLayout.addView(reviewEditText);
        subLayout.addView(sendButton);

        ratingCardView.addView(subLayout);
        rootLayout.addView(ratingCardView);
    }

    private void createRatingCardView() {
        ratingCardView = new CardView(context);
        CardView.LayoutParams ratingCardViewParams = new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT);

        ratingCardViewParams.setMargins(0, 35, 0, 0);
        ratingCardView.setLayoutParams(ratingCardViewParams);
        ratingCardView.setRadius(25);
        ratingCardView.setContentPadding(50, 50, 50, 50);
    }

    private void createSubLayout() {
        subLayout = new LinearLayout(context);
        LinearLayout.LayoutParams subLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        subLayout.setLayoutParams(subLayoutParams);
        subLayout.setGravity(Gravity.CENTER);
        subLayout.setOrientation(LinearLayout.VERTICAL);
    }

    private void createTrainerNameTextView(String trainerName) {
        trainerNameTextView = new TextView(context);
        ViewGroup.LayoutParams trainerNameTextViewParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        trainerNameTextView.setLayoutParams(trainerNameTextViewParams);
        trainerNameTextView.setText(trainerName);
        trainerNameTextView.setTextSize(25);
        trainerNameTextView.setTextColor(Color.BLACK);
        trainerNameTextView.setTypeface(Typeface.DEFAULT_BOLD);
    }

    private void createRatingBar() {
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, R.style.RatingBar);
        ratingBar = new RatingBar(contextThemeWrapper);
        ViewGroup.LayoutParams ratingBarParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ratingBar.setLayoutParams(ratingBarParams);
        ratingBar.setStepSize(1);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                float rating = ratingBar.getRating();

                sendButton.setEnabled(true);

                if (rating <= 1) {
                    ratingStateTextView.setText(context.getResources().getString(R.string.rating_state_very_bad));
                }
                else if (rating <= 2) {
                    ratingStateTextView.setText(context.getResources().getString(R.string.rating_state_need_improvement));
                }
                else if (rating <= 3) {
                    ratingStateTextView.setText(context.getResources().getString(R.string.rating_state_good));
                }
                else if (rating <= 4) {
                    ratingStateTextView.setText(context.getResources().getString(R.string.rating_state_great));
                }
                else {
                    ratingStateTextView.setText(context.getResources().getString(R.string.rating_state_awesome));
                }
            }
        });
    }

    private void createRatingStateTextView() {
        ratingStateTextView = new TextView(context);
        ViewGroup.LayoutParams ratingStateTextViewParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ratingStateTextView.setLayoutParams(ratingStateTextViewParams);
        ratingStateTextView.setTextSize(15);
        ratingStateTextView.setTextColor(Color.BLACK);
        ratingStateTextView.setTypeface(Typeface.DEFAULT_BOLD);
    }

    private void createReviewEditText() {
        reviewEditText = new EditText(context);
        ViewGroup.LayoutParams reviewEditTextParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        reviewEditText.setLayoutParams(reviewEditTextParams);
        reviewEditText.setInputType(InputType.TYPE_CLASS_TEXT);
        reviewEditText.getBackground().setColorFilter(context.getResources().getColor(R.color.purple), PorterDuff.Mode.SRC_ATOP);
        reviewEditText.setCursorVisible(false);
        reviewEditText.setHint(context.getResources().getString(R.string.rating_review_hint));
    }

    private void createSendButton() {
        sendButton = new Button(context);
        ViewGroup.LayoutParams sendButtonParams = new ViewGroup.LayoutParams(300, ViewGroup.LayoutParams.WRAP_CONTENT);

        sendButton.setLayoutParams(sendButtonParams);
        sendButton.setText(context.getResources().getString(R.string.rating_button_name));
        sendButton.setTextColor(context.getResources().getColor(R.color.white));
        sendButton.setEnabled(false);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Save Rating into DB

                ratingBar.setIsIndicator(true);
                subLayout.removeView(reviewEditText);
                subLayout.removeView(sendButton);

                Toast.makeText(context, "Rating sent successfully", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
