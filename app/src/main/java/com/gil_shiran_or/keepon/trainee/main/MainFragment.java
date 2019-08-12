package com.gil_shiran_or.keepon.trainee.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.gil_shiran_or.keepon.R;

public class MainFragment extends Fragment {

    //private int repliesArrowRotationAngle = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trainee_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        LinearLayout postsLinearLayout = getView().findViewById(R.id.posts);
        FloatingActionButton addPostButton = getView().findViewById(R.id.add_post);

        getActivity().setTitle("KeepOn");
        new PostCard(getContext(), postsLinearLayout);
        new PostCard(getContext(), postsLinearLayout);

        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AddPostDialog().show(getFragmentManager(), "add post dialog");
            }
        });

        /*LinearLayout a = getView().findViewById(R.id.replies_expandable_layout);
        final LinearLayout b = getView().findViewById(R.id.replies);
        final ImageView c = getView().findViewById(R.id.arrow);
        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (b.getVisibility() == View.GONE) {
                    expand(b);
                } else {
                    collapse(b);
                }

                toggleArrowAnimation(c);
            }
        });*/
    }


    /*private void expand(final View v) {
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
    }*/
}


