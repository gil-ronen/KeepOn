package com.gil_shiran_or.keepon.trainee.ui.main;

import android.view.ViewGroup;

import com.gil_shiran_or.keepon.trainee.ui.utilities.ExpandableViewGroup;

public class PostRepliesListAdapterItem {

    private PostItem postItem;
    private RepliesListAdapter repliesListAdapter;
    private ViewGroup expanderViewGroup;
    private ExpandableViewGroup expandableViewGroup;

    public PostRepliesListAdapterItem(PostItem postItem, RepliesListAdapter repliesListAdapter, ViewGroup expanderViewGroup, ExpandableViewGroup expandableViewGroup) {
        this.postItem = postItem;
        this.repliesListAdapter = repliesListAdapter;
        this.expanderViewGroup = expanderViewGroup;
        this.expandableViewGroup = expandableViewGroup;
    }

    public PostItem getPostItem() {
        return postItem;
    }

    public RepliesListAdapter getRepliesListAdapter() {
        return repliesListAdapter;
    }

    public ViewGroup getExpanderViewGroup() {
        return expanderViewGroup;
    }

    public ExpandableViewGroup getExpandableViewGroup() {
        return expandableViewGroup;
    }
}
