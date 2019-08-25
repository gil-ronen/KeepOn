package com.gil_shiran_or.keepon.trainee.ui.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gil_shiran_or.keepon.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class MainFragment extends Fragment implements AddPostDialog.AddPostListener, AddReplyDialog.AddReplyListener {

    private List<PostItem> postsList;
    private RecyclerView recyclerView;
    private PostsListAdapter postsListAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private AddPostDialog addPostDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_trainee_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle(getResources().getString(R.string.main_page_title));

        createPostsList();
        buildRecyclerView();
        adjustAddPostButton();
    }

    private void createPostsList() {
        postsList = new ArrayList<>();
        List<ReplyItem> postRepliesList = new ArrayList<>();

        postRepliesList.add(new ReplyItem("Or Fisher", "18.08.2019", "reply @Shiran Avidov", "This is the first reply ever in this page", 10, 2));


        postsList.add(new PostItem("Shiran Avidov", "18.08.2019",
                "First Post", "This is the first post ever in this page", 25, 5, postRepliesList));

    }

    private void buildRecyclerView() {
        recyclerView = getView().findViewById(R.id.posts_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getContext());
        postsListAdapter = new PostsListAdapter(postsList, getContext(), this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(postsListAdapter);
    }

    private void adjustAddPostButton() {
        final FloatingActionButton addPostButton = Objects.requireNonNull(getView()).findViewById(R.id.add_post);
        final MainFragment fragment = this;

        addPostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPostDialog = new AddPostDialog();

                addPostDialog.setTargetFragment(fragment, 0);
                addPostDialog.show(getFragmentManager(), "add post dialog");
            }
        });
    }

    @Override
    public void applyPost(String postTitle, String postBody) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

        postsList.add(0, new PostItem("Shiran Avidov", formatter.format(date), postTitle, postBody, 0, 0, null));
        postsListAdapter.notifyItemInserted(0);
    }

    @Override
    public void applyReply(String replyBody, String replyToWriterName, PostItem postItem) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

        postsListAdapter.addReplyToRepliesList(new ReplyItem("Shiran Avidov", formatter.format(date),
                "reply @" + replyToWriterName, replyBody, 0, 0), postItem);
    }
}


