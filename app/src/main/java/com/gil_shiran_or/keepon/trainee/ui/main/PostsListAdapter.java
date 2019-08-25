package com.gil_shiran_or.keepon.trainee.ui.main;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gil_shiran_or.keepon.R;
import com.gil_shiran_or.keepon.trainee.ui.utilities.ExpandableViewGroup;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostsListAdapter extends RecyclerView.Adapter<PostsListAdapter.PostsViewHolder> {

    private List<PostItem> postsList;
    private List<PostRepliesListAdapterItem> postsRepliesList = new ArrayList<>();
    private RepliesListAdapter repliesListAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private AddReplyDialog addReplyDialog;
    private Context context;
    private MainFragment mainFragment;

    public static class PostsViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView postWriterCircleImageView;
        public TextView postWriterNameTextView;
        public TextView postDateTextView;
        public TextView postTitleTextView;
        public TextView postBodyTextView;
        public ViewGroup expanderViewGroup;
        public ImageView postLikeImageView;
        public TextView postLikesNumTextView;
        public ImageView postDislikeImageView;
        public TextView postDislikesNumTextView;
        public TextView postReplyButtonTextView;
        public RecyclerView postRepliesRecyclerView;

        public PostsViewHolder(View itemView) {
            super(itemView);
            postWriterCircleImageView = itemView.findViewById(R.id.post_writer_img);
            postWriterNameTextView = itemView.findViewById(R.id.post_writer_name);
            postDateTextView = itemView.findViewById(R.id.post_date);
            postTitleTextView = itemView.findViewById(R.id.post_title);
            postBodyTextView = itemView.findViewById(R.id.post_body);
            expanderViewGroup = itemView.findViewById(R.id.replies_expander);
            postLikeImageView = itemView.findViewById(R.id.post_like);
            postLikesNumTextView = itemView.findViewById(R.id.likes_num);
            postDislikeImageView = itemView.findViewById(R.id.post_dislike);
            postDislikesNumTextView = itemView.findViewById(R.id.dislikes_num);
            postReplyButtonTextView = itemView.findViewById(R.id.post_reply_button);
            postRepliesRecyclerView = itemView.findViewById(R.id.replies_list);
        }
    }

    public PostsListAdapter(List<PostItem> postsList, Context context, MainFragment mainFragment) {
        this.postsList = postsList;
        this.context = context;
        this.mainFragment = mainFragment;
    }

    @Override
    public PostsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);

        return new PostsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final PostsViewHolder holder, int position) {
        final PostItem currentPostItem = postsList.get(position);

        setPostHeader(holder, currentPostItem);
        setPostLikes(holder, currentPostItem);
        setPostDislikes(holder, currentPostItem);

        holder.postReplyButtonTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addReplyDialog = new AddReplyDialog();

                addReplyDialog.setPostItem(currentPostItem);
                addReplyDialog.setIsReplyToPost(true);
                addReplyDialog.setTargetFragment(mainFragment, 0);
                addReplyDialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "add reply dialog");
            }
        });

        layoutManager = new LinearLayoutManager(context);
        repliesListAdapter = new RepliesListAdapter(currentPostItem.getPostRepliesList(), currentPostItem, mainFragment, context);
        holder.postRepliesRecyclerView.setLayoutManager(layoutManager);
        holder.postRepliesRecyclerView.setAdapter(repliesListAdapter);

        if (currentPostItem.getPostRepliesList() != null && !currentPostItem.getPostRepliesList().isEmpty()) {
            holder.expanderViewGroup.setVisibility(View.VISIBLE);
            ExpandableViewGroup expandableViewGroup = new ExpandableViewGroup("View " + currentPostItem.getPostRepliesList().size() + " replies",
                    "Hide replies", holder.expanderViewGroup, holder.postRepliesRecyclerView);

            postsRepliesList.add(new PostRepliesListAdapterItem(currentPostItem, repliesListAdapter, holder.expanderViewGroup, expandableViewGroup));
        }
        else {
            ExpandableViewGroup expandableViewGroup = new ExpandableViewGroup("", "Hide replies", holder.expanderViewGroup, holder.postRepliesRecyclerView);
            postsRepliesList.add(new PostRepliesListAdapterItem(currentPostItem, repliesListAdapter, holder.expanderViewGroup, expandableViewGroup));
        }
    }

    private void setPostHeader(PostsViewHolder holder, PostItem currentPostItem) {
        holder.postWriterNameTextView.setText(currentPostItem.getPostWriterName());
        holder.postDateTextView.setText(currentPostItem.getPostDate());
        holder.postTitleTextView.setText(currentPostItem.getPostTitle());
        holder.postBodyTextView.setText(currentPostItem.getPostBody());
    }

    private void setPostLikes(final PostsViewHolder holder, PostItem currentPostItem) {
        holder.postLikesNumTextView.setText(Integer.toString(currentPostItem.getPostLikesNum()));
        holder.postLikeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.postLikesNumTextView.setText(Integer.toString(Integer.parseInt(holder.postLikesNumTextView.getText().toString()) + 1));
                holder.postLikeImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_like_pressed));
                holder.postLikeImageView.setClickable(false);
                holder.postLikeImageView.setFocusable(false);
                holder.postDislikeImageView.setClickable(false);
                holder.postDislikeImageView.setFocusable(false);
            }
        });
    }

    private void setPostDislikes(final PostsViewHolder holder, PostItem currentPostItem) {
        holder.postDislikesNumTextView.setText(Integer.toString(currentPostItem.getPostDislikesNum()));
        holder.postDislikeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.postDislikesNumTextView.setText(Integer.toString(Integer.parseInt(holder.postDislikesNumTextView.getText().toString()) + 1));
                holder.postDislikeImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_dislike_pressed));
                holder.postLikeImageView.setClickable(false);
                holder.postLikeImageView.setFocusable(false);
                holder.postDislikeImageView.setClickable(false);
                holder.postDislikeImageView.setFocusable(false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }

    public void addReplyToRepliesList(ReplyItem replyItem, PostItem postItem) {
        for (PostRepliesListAdapterItem postRepliesListAdapterItem : postsRepliesList) {
            if (postRepliesListAdapterItem.getPostItem() == postItem) {
                postRepliesListAdapterItem.getRepliesListAdapter().addReplyToRepliesList(replyItem);
                postRepliesListAdapterItem.getExpanderViewGroup().setVisibility(View.VISIBLE);
                postRepliesListAdapterItem.getExpandableViewGroup().setLabelBefore("View " + postItem.getPostRepliesList().size() + " replies");
                break;
            }
        }
    }
}
