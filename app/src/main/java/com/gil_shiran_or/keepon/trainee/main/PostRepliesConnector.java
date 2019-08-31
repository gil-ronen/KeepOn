package com.gil_shiran_or.keepon.trainee.main;

public class PostRepliesConnector {

    private String mPostId;
    private RepliesListAdapter mRepliesListAdapter;

    public PostRepliesConnector(String postId, RepliesListAdapter repliesListAdapter) {
        mPostId = postId;
        mRepliesListAdapter = repliesListAdapter;
    }

    public String getPostId() {
        return mPostId;
    }

    public RepliesListAdapter getRepliesListAdapter() {
        return mRepliesListAdapter;
    }
}
