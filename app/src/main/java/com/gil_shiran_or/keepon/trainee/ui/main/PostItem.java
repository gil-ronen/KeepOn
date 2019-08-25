package com.gil_shiran_or.keepon.trainee.ui.main;

import java.util.ArrayList;
import java.util.List;

public class PostItem {

    private String postWriterName;
    private String postDate;
    private String postTitle;
    private String postBody;
    private int postLikesNum;
    private int postDislikesNum;
    private List<ReplyItem> postRepliesList;

    public PostItem(String postWriterName, String postDate, String postTitle, String postBody, int postLikesNum, int postDislikesNum, List<ReplyItem> postRepliesList) {
        this.postWriterName = postWriterName;
        this.postDate = postDate;
        this.postTitle = postTitle;
        this.postBody = postBody;
        this.postLikesNum = postLikesNum;
        this.postDislikesNum = postDislikesNum;
        this.postRepliesList = postRepliesList;

        if (this.postRepliesList == null) {
            this.postRepliesList = new ArrayList<>();
        }
    }

    public String getPostWriterName() {
        return postWriterName;
    }

    public String getPostDate() {
        return postDate;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public String getPostBody() {
        return postBody;
    }

    public int getPostLikesNum() {
        return postLikesNum;
    }

    public int getPostDislikesNum() {
        return postDislikesNum;
    }

    public List<ReplyItem> getPostRepliesList() {
        return postRepliesList;
    }

    public void addReplyToRepliesList(ReplyItem replyItem) {
        postRepliesList.add(replyItem);
    }
}
