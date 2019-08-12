package com.gil_shiran_or.keepon.trainee.main;

import android.content.Context;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gil_shiran_or.keepon.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostBody {

    private Context context;
    private TextView titleTextView;
    private TextView bodyTextView;
    private LinearLayout subLayout;
    private ImageView likeImageView;
    private TextView likesNumTextView;
    private ImageView dislikeImageView;
    private TextView dislikesNumTextView;
    private TextView replyTextView;

    public PostBody(Context context, LinearLayout rootLayout) {
        this.context = context;

        createTitleTextView("Title 1");
        createBodyTextView("This is the best exercise to do if you want to improve your legs and arms");
        createSubLayout();
        createLikeImageView();
        createLikesNumTextView(25);
        createDislikeImageView();
        createDislikesNumTextView(5);
        createReplyTextView();

        subLayout.addView(likeImageView);
        subLayout.addView(likesNumTextView);
        subLayout.addView(dislikeImageView);
        subLayout.addView(dislikesNumTextView);
        subLayout.addView(replyTextView);

        rootLayout.addView(titleTextView);
        rootLayout.addView(bodyTextView);
        rootLayout.addView(subLayout);
    }

    private void createTitleTextView(String title) {
        titleTextView = new TextView(context);
        ViewGroup.LayoutParams titleTextViewParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        titleTextView.setLayoutParams(titleTextViewParams);
        titleTextView.setText(title);
        titleTextView.setPadding(0, 0, 0, 25);
        titleTextView.setTextSize(20);
        titleTextView.setTextColor(Color.BLACK);
    }

    private void createBodyTextView(String body) {
        bodyTextView = new TextView(context);
        ViewGroup.LayoutParams bodyTextViewParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        bodyTextView.setLayoutParams(bodyTextViewParams);
        bodyTextView.setText(body);
        bodyTextView.setPadding(0, 0, 0, 25);
        bodyTextView.setTextSize(15);
        bodyTextView.setTextColor(Color.BLACK);
    }

    private void createSubLayout() {
        subLayout = new LinearLayout(context);
        LinearLayout.LayoutParams subLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        subLayout.setLayoutParams(subLayoutParams);
        subLayout.setOrientation(LinearLayout.HORIZONTAL);
    }

    private void createLikeImageView() {
        likeImageView = new CircleImageView(context);
        ViewGroup.LayoutParams likeImageViewParams = new ViewGroup.LayoutParams(80, 80);

        likeImageView.setLayoutParams(likeImageViewParams);
        likeImageView.setClickable(true);
        likeImageView.setFocusable(true);
        likeImageView.setPadding(0, 0, 25, 0);
        likeImageView.setElevation(1);
        likeImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_like));
    }

    private void createLikesNumTextView(int numOfLikes) {
        likesNumTextView = new TextView(context);
        ViewGroup.LayoutParams likesNumTextViewParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        likesNumTextView.setLayoutParams(likesNumTextViewParams);
        likesNumTextView.setText(Integer.toString(numOfLikes));
        likesNumTextView.setPadding(0, 0, 25, 0);
        likesNumTextView.setTextSize(15);
        likesNumTextView.setTextColor(Color.BLACK);
    }

    private void createDislikeImageView() {
        dislikeImageView = new CircleImageView(context);
        ViewGroup.LayoutParams dislikeImageViewParams = new ViewGroup.LayoutParams(80, 80);

        dislikeImageView.setLayoutParams(dislikeImageViewParams);
        likeImageView.setClickable(true);
        likeImageView.setFocusable(true);
        dislikeImageView.setPadding(0, 0, 25, 0);
        dislikeImageView.setElevation(1);
        dislikeImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_dislike));
    }

    private void createDislikesNumTextView(int numOfDislikes) {
        dislikesNumTextView = new TextView(context);
        ViewGroup.LayoutParams dislikesNumTextViewParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dislikesNumTextView.setLayoutParams(dislikesNumTextViewParams);
        dislikesNumTextView.setText(Integer.toString(numOfDislikes));
        dislikesNumTextView.setPadding(0, 0, 25, 0);
        dislikesNumTextView.setTextSize(15);
        dislikesNumTextView.setTextColor(Color.BLACK);
    }

    private void createReplyTextView() {
        replyTextView = new TextView(context);
        ViewGroup.LayoutParams replyTextViewParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        replyTextView.setLayoutParams(replyTextViewParams);
        likeImageView.setClickable(true);
        likeImageView.setFocusable(true);
        replyTextView.setText(context.getResources().getString(R.string.post_comment_reply));
        replyTextView.setTextSize(15);
        replyTextView.setTextColor(Color.BLACK);
    }
}
