package com.gil_shiran_or.keepon.trainee.ratings;

import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.gil_shiran_or.keepon.R;

public class OnlineRatingReview {

    private Context context;
    private View separatorView;
    private RatingBar ratingBar;
    private TextView reviewTextView;

    public OnlineRatingReview(Context context, LinearLayout subLayout, int numOfStars, String reviewText) {
        this.context = context;

        createSeparatorView();
        createRatingBar(numOfStars);
        createReviewTextView(reviewText);

        subLayout.addView(separatorView);
        subLayout.addView(ratingBar);
        subLayout.addView(reviewTextView);
    }

    private void createSeparatorView() {
        separatorView = new View(context);
        LinearLayout.LayoutParams separatorViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 3);

        separatorViewParams.setMargins(0, 35, 0, 35);
        separatorView.setLayoutParams(separatorViewParams);
        separatorView.setBackgroundColor(context.getResources().getColor(R.color.dark_gray));
    }

    private void createRatingBar(int numOfStars) {
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, R.style.RatingBarReview);
        ratingBar = new RatingBar(contextThemeWrapper);
        ViewGroup.LayoutParams ratingBarParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ratingBar.setLayoutParams(ratingBarParams);
        ratingBar.setIsIndicator(true);
        ratingBar.setRating(numOfStars);
    }

    private void createReviewTextView(String reviewText) {
        reviewTextView = new TextView(context);
        ViewGroup.LayoutParams reviewTextViewParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        reviewTextView.setLayoutParams(reviewTextViewParams);
        reviewTextView.setText(reviewText);
    }
}
