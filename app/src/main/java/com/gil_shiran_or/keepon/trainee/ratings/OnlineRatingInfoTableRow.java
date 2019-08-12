package com.gil_shiran_or.keepon.trainee.ratings;

import android.content.Context;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.gil_shiran_or.keepon.R;

import java.util.Locale;

public class OnlineRatingInfoTableRow {

    private Context context;
    private TableRow infoTableRow;
    private TextView numOfStarsTextView;
    private ProgressBar progressBar;
    private TextView numOfReviewersTextView;

    public OnlineRatingInfoTableRow(Context context, TableLayout ratingInfoTableLayout, int numOfStars, int numOfReviewers, int totalNumOfReviewers) {
        this.context = context;

        createInfoTableRow();
        createNumOfStarsTextView(numOfStars);
        createProgressBar(numOfReviewers, totalNumOfReviewers);
        createNumOfReviewersTextView(numOfReviewers);

        infoTableRow.addView(numOfStarsTextView);
        infoTableRow.addView(progressBar);
        infoTableRow.addView(numOfReviewersTextView);

        ratingInfoTableLayout.addView(infoTableRow);
    }

    private void createInfoTableRow() {
        infoTableRow = new TableRow(context);
        TableRow.LayoutParams tableRowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);

        infoTableRow.setLayoutParams(tableRowParams);
        infoTableRow.setGravity(Gravity.CENTER);
    }

    private void createNumOfStarsTextView(int numOfStars) {
        numOfStarsTextView = new TextView(context);
        TableRow.LayoutParams numOfStarsTextViewParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

        numOfStarsTextViewParams.setMargins(0, 0, 10, 0);
        numOfStarsTextView.setLayoutParams(numOfStarsTextViewParams);
        numOfStarsTextView.setText(String.format(Locale.getDefault(), "%d", numOfStars));
    }

    private void createProgressBar(int numOfReviewers, int totalNumOfReviewers) {
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, R.style.ProgressBar);
        progressBar = new ProgressBar(contextThemeWrapper);
        TableRow.LayoutParams progressBarParams = new TableRow.LayoutParams(625, TableRow.LayoutParams.WRAP_CONTENT);

        progressBar.setLayoutParams(progressBarParams);
        progressBar.setMax(totalNumOfReviewers);
        progressBar.setProgress(numOfReviewers);
    }

    private void createNumOfReviewersTextView(int numOfReviewers) {
        numOfReviewersTextView = new TextView(context);
        TableRow.LayoutParams numOfReviewersTextViewParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);

        numOfReviewersTextViewParams.setMargins(10, 0, 0, 0);
        numOfReviewersTextView.setLayoutParams(numOfReviewersTextViewParams);
        numOfReviewersTextView.setText(String.format(Locale.getDefault(), "(%d)", numOfReviewers));
    }
}
