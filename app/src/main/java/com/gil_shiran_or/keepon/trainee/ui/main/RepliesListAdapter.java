package com.gil_shiran_or.keepon.trainee.ui.main;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gil_shiran_or.keepon.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RepliesListAdapter extends RecyclerView.Adapter<RepliesListAdapter.RepliesViewHolder> {

    private List<ReplyItem> repliesList;
    private PostItem postItem;
    private AddReplyDialog addReplyDialog;
    private MainFragment mainFragment;
    private Context context;

    public static class RepliesViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView replyWriterCircleImageView;
        public TextView replyWriterNameTextView;
        public TextView replyDateTextView;
        public TextView replyToTitleTextView;
        public TextView replyBodyTextView;
        public ImageView replyLikeImageView;
        public TextView replyLikesNumTextView;
        public ImageView replyDislikeImageView;
        public TextView replyDislikesNumTextView;
        public TextView replyReplyButtonTextView;

        public RepliesViewHolder(View itemView) {
            super(itemView);
            replyWriterCircleImageView = itemView.findViewById(R.id.reply_writer_img);
            replyWriterNameTextView = itemView.findViewById(R.id.reply_writer_name);
            replyDateTextView = itemView.findViewById(R.id.reply_date);
            replyToTitleTextView = itemView.findViewById(R.id.reply_to_title);
            replyBodyTextView = itemView.findViewById(R.id.reply_body);
            replyLikeImageView = itemView.findViewById(R.id.reply_like);
            replyLikesNumTextView = itemView.findViewById(R.id.likes_num);
            replyDislikeImageView = itemView.findViewById(R.id.reply_dislike);
            replyDislikesNumTextView = itemView.findViewById(R.id.dislikes_num);
            replyReplyButtonTextView = itemView.findViewById(R.id.reply_reply_button);
        }
    }

    public RepliesListAdapter(List<ReplyItem> repliesList, PostItem postItem, MainFragment mainFragment, Context context) {
        this.repliesList = repliesList;
        this.postItem = postItem;
        this.mainFragment = mainFragment;
        this.context = context;
    }

    @Override
    public RepliesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reply_item, parent, false);

        return new RepliesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RepliesViewHolder holder, int position) {
        final ReplyItem currentReplyItem = repliesList.get(position);

        setReplyHeader(holder, currentReplyItem);
        setReplyLikes(holder, currentReplyItem);
        setReplyDislikes(holder, currentReplyItem);

        holder.replyReplyButtonTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addReplyDialog = new AddReplyDialog();

                addReplyDialog.setPostItem(postItem);
                addReplyDialog.setReplyItem(currentReplyItem);
                addReplyDialog.setIsReplyToPost(false);
                addReplyDialog.setTargetFragment(mainFragment, 0);
                addReplyDialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "add reply dialog");
            }
        });
    }

    private void setReplyHeader(RepliesViewHolder holder, ReplyItem currentReplyItem) {
        holder.replyWriterNameTextView.setText(currentReplyItem.getReplyWriterName());
        holder.replyDateTextView.setText(currentReplyItem.getReplyDate());
        holder.replyToTitleTextView.setText(currentReplyItem.getReplyToTitle());
        holder.replyBodyTextView.setText(currentReplyItem.getReplyBody());
    }

    private void setReplyLikes(final RepliesViewHolder holder, ReplyItem currentReplyItem) {
        holder.replyLikesNumTextView.setText(Integer.toString(currentReplyItem.getReplyLikesNum()));
        holder.replyLikeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.replyLikesNumTextView.setText(Integer.toString(Integer.parseInt(holder.replyLikesNumTextView.getText().toString()) + 1));
                holder.replyLikeImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_like_pressed));
                holder.replyLikeImageView.setClickable(false);
                holder.replyLikeImageView.setFocusable(false);
                holder.replyDislikeImageView.setClickable(false);
                holder.replyDislikeImageView.setFocusable(false);
            }
        });
    }

    private void setReplyDislikes(final RepliesViewHolder holder, ReplyItem currentReplyItem) {
        holder.replyDislikesNumTextView.setText(Integer.toString(currentReplyItem.getReplyDislikesNum()));
        holder.replyDislikeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.replyDislikesNumTextView.setText(Integer.toString(Integer.parseInt(holder.replyDislikesNumTextView.getText().toString()) + 1));
                holder.replyDislikeImageView.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_dislike_pressed));
                holder.replyLikeImageView.setClickable(false);
                holder.replyLikeImageView.setFocusable(false);
                holder.replyDislikeImageView.setClickable(false);
                holder.replyDislikeImageView.setFocusable(false);
            }
        });
    }

    @Override
    public int getItemCount() {
        return repliesList.size();
    }

    public void addReplyToRepliesList(ReplyItem replyItem) {
        repliesList.add(replyItem);
        this.notifyItemInserted(repliesList.size() - 1);
    }
}
