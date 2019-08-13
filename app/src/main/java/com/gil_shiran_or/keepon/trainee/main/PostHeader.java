package com.gil_shiran_or.keepon.trainee.main;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gil_shiran_or.keepon.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostHeader {

    private Context context;
    private LinearLayout subLayout;
    private CircleImageView traineeCircleImageView;
    private TextView traineeNameTextView;
    private TextView postDateTextView;

    public PostHeader(Context context, LinearLayout rootLayout) {
        this.context = context;

        createSubLayout();
        createTraineeCircleImageView();
        createTraineeNameTextView("Shiran Avidov");
        createPostDateTextView();

        subLayout.addView(traineeCircleImageView);
        subLayout.addView(traineeNameTextView);
        subLayout.addView(postDateTextView);

        rootLayout.addView(subLayout);
    }

    private void createSubLayout() {
        subLayout = new LinearLayout(context);
        LinearLayout.LayoutParams subLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        subLayout.setLayoutParams(subLayoutParams);
        subLayout.setGravity(Gravity.CENTER_VERTICAL);
        subLayout.setOrientation(LinearLayout.HORIZONTAL);
    }

    private void createTraineeCircleImageView() {
        traineeCircleImageView = new CircleImageView(context);
        ViewGroup.LayoutParams traineeCircleImageViewParams = new ViewGroup.LayoutParams(170, 170);

        traineeCircleImageView.setLayoutParams(traineeCircleImageViewParams);
        traineeCircleImageView.setPadding(0, 0, 25, 0);
        traineeCircleImageView.setElevation(1);
        traineeCircleImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.default_profile_img));
    }

    private void createTraineeNameTextView(String traineeName) {
        traineeNameTextView = new TextView(context);
        ViewGroup.LayoutParams traineeNameTextViewParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        traineeNameTextView.setLayoutParams(traineeNameTextViewParams);
        traineeNameTextView.setText(traineeName);
        traineeNameTextView.setTextSize(15);
        traineeNameTextView.setTextColor(Color.BLACK);
    }

    private void createPostDateTextView() {
        postDateTextView = new TextView(context);
        ViewGroup.LayoutParams postDateTextViewParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());

        postDateTextView.setLayoutParams(postDateTextViewParams);
        postDateTextView.setText(dateFormat.format(currentDate));
        postDateTextView.setTextSize(15);
        postDateTextView.setTextColor(Color.BLACK);
        postDateTextView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
    }
}
