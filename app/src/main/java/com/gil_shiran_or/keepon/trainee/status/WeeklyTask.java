package com.gil_shiran_or.keepon.trainee.status;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gil_shiran_or.keepon.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class WeeklyTask {

    private Context context;
    private RelativeLayout externalLayout;
    private LinearLayout taskSubLayout;
    private CircleImageView taskCircleImageView;
    private TextView taskDetailsTextView;
    private TextView taskPointsTextView;
    private LinearLayout completedTaskSubLayout;
    private ImageView completedTaskImageView;
    private View separatorView;

    public WeeklyTask(Context context, LinearLayout rootLayout) {
        this.context = context;

        createExternalLayout();
        createTaskSubLayout();
        createTaskCircleImageView();
        createTaskDetailsTextView("go to the gym 3 times this week");
        createTaskPointsTextView(25);
        createCompletedTaskSubLayout();
        createCompletedTaskImageView();
        createSeparatorView();

        taskSubLayout.addView(taskCircleImageView);
        taskSubLayout.addView(taskDetailsTextView);
        taskSubLayout.addView(taskPointsTextView);

        completedTaskSubLayout.addView(completedTaskImageView);

        externalLayout.addView(taskSubLayout);
        externalLayout.addView(completedTaskSubLayout);

        rootLayout.addView(externalLayout);
        rootLayout.addView(separatorView);
    }

    private void createExternalLayout() {
        externalLayout = new RelativeLayout(context);
        RelativeLayout.LayoutParams externalLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

        externalLayout.setLayoutParams(externalLayoutParams);
    }


    private void createTaskSubLayout() {
        taskSubLayout = new LinearLayout(context);
        LinearLayout.LayoutParams taskSubLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        taskSubLayoutParams.setMargins(0, 20, 0, 20);
        taskSubLayout.setLayoutParams(taskSubLayoutParams);
        taskSubLayout.setGravity(Gravity.CENTER | Gravity.START);
        taskSubLayout.setOrientation(LinearLayout.HORIZONTAL);
    }

    private void createTaskCircleImageView() {
        taskCircleImageView = new CircleImageView(context);
        ViewGroup.LayoutParams taskCircleImageViewParams = new ViewGroup.LayoutParams(275, 275);

        taskCircleImageView.setLayoutParams(taskCircleImageViewParams);
        taskCircleImageView.setPadding(0, 0, 25, 0);
        taskCircleImageView.setElevation(1);
        taskCircleImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.default_profile_img));
    }

    private void createTaskDetailsTextView(String taskDetails) {
        taskDetailsTextView = new TextView(context);
        ViewGroup.LayoutParams taskDetailsTextViewParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        taskDetailsTextView.setLayoutParams(taskDetailsTextViewParams);
        taskDetailsTextView.setText(taskDetails);
        taskDetailsTextView.setTextSize(15);
        taskDetailsTextView.setTextColor(Color.BLACK);
    }

    private void createTaskPointsTextView(int taskPoints) {
        taskPointsTextView = new TextView(context);
        ViewGroup.LayoutParams taskPointsTextViewParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        taskPointsTextView.setLayoutParams(taskPointsTextViewParams);
        taskPointsTextView.setText(Integer.toString(taskPoints));
        taskPointsTextView.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        taskPointsTextView.setTextSize(25);
        taskPointsTextView.setTextColor(Color.BLACK);
    }

    private void createCompletedTaskSubLayout() {
        completedTaskSubLayout = new LinearLayout(context);
        LinearLayout.LayoutParams completedTaskSubLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        completedTaskSubLayout.setLayoutParams(completedTaskSubLayoutParams);
        completedTaskSubLayout.setGravity(Gravity.CENTER);
        completedTaskSubLayout.setOrientation(LinearLayout.VERTICAL);
    }

    private void createCompletedTaskImageView() {
        completedTaskImageView = new CircleImageView(context);
        ViewGroup.LayoutParams completedTaskImageViewParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 320);

        completedTaskImageView.setLayoutParams(completedTaskImageViewParams);
        completedTaskImageView.setPadding(0, 0, 25, 0);
        completedTaskImageView.setVisibility(View.GONE);
        completedTaskImageView.setElevation(1);
        completedTaskImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.completed_task_stamp));
    }

    private void createSeparatorView() {
        separatorView = new View(context);
        LinearLayout.LayoutParams separatorViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 3);

        separatorView.setLayoutParams(separatorViewParams);
        separatorView.setBackgroundColor(context.getResources().getColor(R.color.dark_gray));
    }
}
