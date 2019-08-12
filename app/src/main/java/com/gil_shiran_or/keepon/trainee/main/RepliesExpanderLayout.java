package com.gil_shiran_or.keepon.trainee.main;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gil_shiran_or.keepon.R;

public class RepliesExpanderLayout {

    private Context context;
    private LinearLayout subLayout;
    private View leftSeparatorView;
    private View rightSeparatorView;
    private TextView titleTextView;
    private ImageView arrowImageView;

    private int repliesArrowRotationAngle = 0;

    public RepliesExpanderLayout(Context context, LinearLayout rootLayout, LinearLayout targetLayout) {
        this.context = context;

        createSubLayout(targetLayout, 2);
        leftSeparatorView = createSeparatorView();
        createTitleTextView(2);
        rightSeparatorView = createSeparatorView();
        createArrowImageView();

        subLayout.addView(leftSeparatorView);
        subLayout.addView(titleTextView);
        subLayout.addView(rightSeparatorView);
        subLayout.addView(arrowImageView);

        rootLayout.addView(subLayout);
    }

    private void createSubLayout(final LinearLayout targetLayout, final int numOfReplies) {
        subLayout = new LinearLayout(context);
        LinearLayout.LayoutParams subLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        subLayout.setLayoutParams(subLayoutParams);
        subLayout.setClickable(true);
        subLayout.setFocusable(true);
        subLayout.setGravity(Gravity.CENTER);
        subLayout.setOrientation(LinearLayout.HORIZONTAL);

        subLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (targetLayout.getVisibility() == View.GONE) {
                    titleTextView.setText(context.getResources().getString(R.string.post_replies_expandable_layout_title));
                    expand(targetLayout);
                } else {
                    String title = "View " + numOfReplies + " replies";

                    titleTextView.setText(title);
                    collapse(targetLayout);
                }

                toggleArrowAnimation(arrowImageView);
            }
        });
    }

    private View createSeparatorView() {
        View separatorView = new View(context);
        LinearLayout.LayoutParams separatorViewParams = new LinearLayout.LayoutParams(300, 5);

        separatorViewParams.setMargins(0, 35, 0, 35);
        separatorView.setLayoutParams(separatorViewParams);
        separatorView.setBackgroundColor(context.getResources().getColor(R.color.dark_gray));

        return separatorView;
    }

    private void createTitleTextView(int numOfReplies) {
        titleTextView = new TextView(context);
        ViewGroup.LayoutParams titleTextViewParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        String title = "View " + numOfReplies + " replies";

        titleTextView.setLayoutParams(titleTextViewParams);
        titleTextView.setText(title);
        titleTextView.setPadding(25, 0, 25, 0);
        titleTextView.setTextSize(15);
        titleTextView.setTextColor(Color.BLACK);
    }

    private void createArrowImageView() {
        arrowImageView = new ImageView(context);
        ViewGroup.LayoutParams arrowImageViewParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        arrowImageView.setLayoutParams(arrowImageViewParams);
        arrowImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_down_arrow));
    }

    private void expand(final View v) {
        int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) v.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
        int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int)(targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Expansion speed of 1dp/ms
        a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    private void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation()
        {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(interpolatedTime == 1){
                    v.setVisibility(View.GONE);
                }else{
                    v.getLayoutParams().height = initialHeight - (int)(initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Collapse speed of 1dp/ms
        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    private void toggleArrowAnimation(View view) {
        repliesArrowRotationAngle = repliesArrowRotationAngle == 0 ? 180 : 0;  //toggle
        view.animate().rotation(repliesArrowRotationAngle).setDuration(250).start();
    }
}
