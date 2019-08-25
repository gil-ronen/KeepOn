package com.gil_shiran_or.keepon.trainee.ui.main;

public class ReplyItem {

    private String replyWriterName;
    private String replyDate;
    private String replyToTitle;
    private String replyBody;
    private int replyLikesNum;
    private int replyDislikesNum;

    public ReplyItem(String replyWriterName, String replyDate, String replyToTitle, String replyBody, int replyLikesNum, int replyDislikesNum) {
        this.replyWriterName = replyWriterName;
        this.replyDate = replyDate;
        this.replyToTitle = replyToTitle;
        this.replyBody = replyBody;
        this.replyLikesNum = replyLikesNum;
        this.replyDislikesNum = replyDislikesNum;
    }

    public String getReplyWriterName() {
        return replyWriterName;
    }

    public String getReplyDate() {
        return replyDate;
    }

    public String getReplyToTitle() {
        return replyToTitle;
    }

    public String getReplyBody() {
        return replyBody;
    }

    public int getReplyLikesNum() {
        return replyLikesNum;
    }

    public int getReplyDislikesNum() {
        return replyDislikesNum;
    }
}
