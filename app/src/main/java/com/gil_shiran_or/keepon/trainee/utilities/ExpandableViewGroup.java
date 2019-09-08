package com.gil_shiran_or.keepon.trainee.utilities;

import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.TextView;

public class ExpandableViewGroup {

    private String labelBefore;
    private String labelAfter;
    private ViewGroup masterLayout;
    private ViewGroup slaveLayout;

    private TextView labelTextView;
    private ImageView arrowImageView;
    private int arrowRotationAngle = 0;

    public ExpandableViewGroup(String labelBefore, String labelAfter, ViewGroup masterLayout, ViewGroup slaveLayout) {
        this.labelBefore = labelBefore;
        this.labelAfter = labelAfter;
        this.masterLayout = masterLayout;
        this.slaveLayout = slaveLayout;

        for (int i = 0; i < masterLayout.getChildCount(); i++) {
            if (masterLayout.getChildAt(i).getClass().equals(AppCompatTextView.class)) {
                labelTextView = (TextView) masterLayout.getChildAt(i);
            }
            else if (masterLayout.getChildAt(i).getClass().equals(AppCompatImageView.class)) {
                arrowImageView = (ImageView) masterLayout.getChildAt(i);
            }
        }

        buildExpanderCollapserLayout();
    }

    public void collapseAndMakeArrowAnimation() {
        collapse(slaveLayout);
        toggleArrowAnimation(arrowImageView);
    }

    private void buildExpanderCollapserLayout() {
        labelTextView.setText(labelBefore);

        masterLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (slaveLayout.getVisibility() == View.GONE) {
                    labelTextView.setText(labelAfter);
                    expand(slaveLayout);
                } else {
                    labelTextView.setText(labelBefore);
                    collapse(slaveLayout);
                }

                toggleArrowAnimation(arrowImageView);
            }
        });
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
        arrowRotationAngle = arrowRotationAngle == 0 ? 180 : 0;  //toggle
        view.animate().rotation(arrowRotationAngle).setDuration(250).start();
    }

    public void setLabelBefore(String labelBefore) {
        this.labelBefore = labelBefore;

        if (slaveLayout.getVisibility() == View.GONE) {
            labelTextView.setText(labelBefore);
        }
    }

    public void changeArrow() {
        arrowRotationAngle = 180;
        arrowImageView.animate().rotation(arrowRotationAngle).setDuration(250).start();
    }
}
