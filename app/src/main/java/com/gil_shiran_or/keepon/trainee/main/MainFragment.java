package com.gil_shiran_or.keepon.trainee.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.gil_shiran_or.keepon.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class MainFragment extends Fragment implements AddPostDialog.AddPostListener, AddReplyDialog.AddReplyListener {

    private PostsListAdapter mPostsListAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trainee_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle(getResources().getString(R.string.main_page_title));

        buildPostsListView();
        adjustAddPostButton();
    }

    private void buildPostsListView() {
        ListView mPostsListView = getView().findViewById(R.id.posts_list);
        mPostsListAdapter = new PostsListAdapter(this);

        mPostsListView.setAdapter(mPostsListAdapter);
    }

    private void adjustAddPostButton() {
        final FloatingActionButton addPostButton = Objects.requireNonNull(getView()).findViewById(R.id.add_post);
        final MainFragment fragment = this;

        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddPostDialog addPostDialog = new AddPostDialog();

                addPostDialog.setTargetFragment(fragment, 0);
                addPostDialog.show(getFragmentManager(), "add post dialog");
            }
        });
    }

    @Override
    public void applyPost(String postTitle, String postBody) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

        mPostsListAdapter.setPostToFirebase(new Post("5xpO2eIuy8S3bvxI34B8S973zi12", formatter.format(date), postTitle, postBody));
    }

    @Override
    public void applyReply(String replyBody, String postId) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

        mPostsListAdapter.setReplyPostToFirebase(new Reply("5xpO2eIuy8S3bvxI34B8S973zi12", formatter.format(date), replyBody), postId);
    }
}


