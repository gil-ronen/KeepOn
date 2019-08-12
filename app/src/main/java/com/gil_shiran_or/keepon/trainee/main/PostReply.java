package com.gil_shiran_or.keepon.trainee.main;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.gil_shiran_or.keepon.R;

public class PostReply {

    private Context context;
    private LinearLayout subLayout;
    private View separatorView;

    public PostReply(Context context, LinearLayout rootLayout) {
        this.context = context;

        createSubLayout();
        createSeparatorView();

        new PostHeader(this.context, rootLayout);
        new PostBody(this.context, rootLayout);

        subLayout.addView(separatorView);

        rootLayout.addView(subLayout);
    }

    private void createSubLayout() {
        subLayout = new LinearLayout(context);
        LinearLayout.LayoutParams subLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        subLayout.setLayoutParams(subLayoutParams);
        subLayout.setGravity(Gravity.CENTER);
        subLayout.setOrientation(LinearLayout.VERTICAL);
    }

    private void createSeparatorView() {
        separatorView = new View(context);
        LinearLayout.LayoutParams separatorViewParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 3);

        separatorViewParams.setMargins(0, 35, 0, 35);
        separatorView.setLayoutParams(separatorViewParams);
        separatorView.setBackgroundColor(context.getResources().getColor(R.color.dark_gray));
    }
}
