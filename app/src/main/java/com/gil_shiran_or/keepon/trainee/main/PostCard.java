package com.gil_shiran_or.keepon.trainee.main;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

public class PostCard {

    private Context context;
    private CardView postCardView;
    private LinearLayout subLayout;
    private LinearLayout repliesLayout;

    public PostCard(Context context, LinearLayout rootLayout) {
        this.context = context;

        createPostCardView();
        createSubLayout();
        createPostCardView();
        createRepliesLayout();

        new PostHeader(this.context, subLayout);
        new PostBody(this.context, subLayout);
        new RepliesExpanderLayout(this.context, subLayout, repliesLayout);

        subLayout.addView(repliesLayout);

        new PostReply(this.context, repliesLayout);
        new PostReply(this.context, repliesLayout);

        postCardView.addView(subLayout);

        rootLayout.addView(postCardView, 0);
    }

    private void createPostCardView() {
        postCardView = new CardView(context);
        CardView.LayoutParams postCardViewParams = new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT);

        postCardViewParams.setMargins(0, 35, 0, 0);
        postCardView.setLayoutParams(postCardViewParams);
        postCardView.setRadius(25);
        postCardView.setContentPadding(50, 50, 50, 50);
    }

    private void createSubLayout() {
        subLayout = new LinearLayout(context);
        LinearLayout.LayoutParams subLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        subLayout.setLayoutParams(subLayoutParams);
        subLayout.setGravity(Gravity.CENTER);
        subLayout.setOrientation(LinearLayout.VERTICAL);
    }

    private void createRepliesLayout() {
        repliesLayout = new LinearLayout(context);
        LinearLayout.LayoutParams repliesLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        repliesLayout.setLayoutParams(repliesLayoutParams);
        repliesLayout.setGravity(Gravity.CENTER);
        repliesLayout.setOrientation(LinearLayout.VERTICAL);
        repliesLayout.setVisibility(View.GONE);
    }
}
