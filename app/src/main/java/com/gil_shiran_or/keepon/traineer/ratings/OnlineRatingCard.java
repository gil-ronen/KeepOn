package com.gil_shiran_or.keepon.traineer.ratings;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
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
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.gil_shiran_or.keepon.R;

public class OnlineRatingCard {

    private Context context;
    private LinearLayout subLayout;
    private CardView ratingCardView;
    private TextView trainerNameTextView;
    private RatingBar ratingBar;
    private TextView ratingScoreTextView;
    private TableLayout ratingInfoTableLayout;

    public OnlineRatingCard(Context context, LinearLayout rootLayout, String trainerName) {
        this.context = context;

        createRatingCardView();
        createSubLayout();
        createTrainerNameTextView(trainerName);
        createRatingBar(4.35f); //TODO: need to get from DB
        createRatingScore(4.35f, 10);   //TODO: need to get from DB
        createRatingInfoTableLayout();

        new OnlineRatingInfoTableRow(this.context, ratingInfoTableLayout, 1, 0, 10);
        new OnlineRatingInfoTableRow(this.context, ratingInfoTableLayout, 2, 1, 10);
        new OnlineRatingInfoTableRow(this.context, ratingInfoTableLayout, 3, 1, 10);
        new OnlineRatingInfoTableRow(this.context, ratingInfoTableLayout, 4, 3, 10);
        new OnlineRatingInfoTableRow(this.context, ratingInfoTableLayout, 5, 5, 10);

        subLayout.addView(trainerNameTextView);
        subLayout.addView(ratingBar);
        subLayout.addView(ratingScoreTextView);
        subLayout.addView(ratingInfoTableLayout);

        new OnlineRatingReview(this.context, subLayout, 4, "I think this trainer is the best. I improved myself so much because of him!!!");
        new OnlineRatingReview(this.context, subLayout, 3, "He can be much more better if he really wanted to");
        new OnlineRatingReview(this.context, subLayout, 2, "Not my cup of tea");
        new OnlineRatingReview(this.context, subLayout, 5, "Yeah!!!");

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

    private void createRatingBar(float rating) {
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, R.style.RatingBar);
        ratingBar = new RatingBar(contextThemeWrapper);
        ViewGroup.LayoutParams ratingBarParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ratingBar.setLayoutParams(ratingBarParams);
        ratingBar.setIsIndicator(true);
        ratingBar.setStepSize(0.01f);
        ratingBar.setRating(rating);
        ratingBar.setSecondaryProgress(context.getResources().getColor(R.color.transparent));
    }

    private void createRatingScore(float rating, int numOfReviewers) {
        ratingScoreTextView = new TextView(context);
        ViewGroup.LayoutParams ratingStateTextViewParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        String ratingScore = rating + " (" + numOfReviewers + ")";

        ratingScoreTextView.setLayoutParams(ratingStateTextViewParams);
        ratingScoreTextView.setText(ratingScore);
        ratingScoreTextView.setTextSize(15);
        ratingScoreTextView.setTextColor(Color.BLACK);
        ratingScoreTextView.setTypeface(Typeface.DEFAULT_BOLD);
    }

    private void createRatingInfoTableLayout() {
        ratingInfoTableLayout = new TableLayout(context);
        TableLayout.LayoutParams tableLayoutParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);

        ratingInfoTableLayout.setLayoutParams(tableLayoutParams);
    }
}
