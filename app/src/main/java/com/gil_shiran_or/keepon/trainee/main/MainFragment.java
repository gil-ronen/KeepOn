package com.gil_shiran_or.keepon.trainee.main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gil_shiran_or.keepon.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

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
        getMvpTrainees();
        adjustAddPostButton();

        Toast.makeText(getContext(), "Loading Posts...", Toast.LENGTH_SHORT).show();
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

        mPostsListAdapter.setPostToFirebase(new Post("E0NB5lGKN2dCULl6yzAHTLCut862", formatter.format(date), postTitle, postBody));
    }

    @Override
    public void applyReply(String replyBody, String postId) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

        mPostsListAdapter.setReplyPostToFirebase(new Reply("E0NB5lGKN2dCULl6yzAHTLCut862", formatter.format(date), replyBody), postId);
    }

    private void getMvpTrainees() {
        DatabaseReference databaseTraineesReference = FirebaseDatabase.getInstance().getReference().child("Users/Trainees");

        databaseTraineesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String firstPlaceName = null, secondPlaceName = null, thirdPlaceName = null;
                String firstPlaceImageUrl = null, secondPlaceImageUrl = null, thirdPlaceImageUrl = null;
                int firstPlaceScore = -1, secondPlaceScore = -1, thirdPlaceScore = -1;

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    int score = data.child("total_score").getValue(Integer.class);

                    if (firstPlaceScore < score) {
                        thirdPlaceScore = secondPlaceScore;
                        thirdPlaceName = secondPlaceName;
                        thirdPlaceImageUrl = secondPlaceImageUrl;
                        secondPlaceScore = firstPlaceScore;
                        secondPlaceName = firstPlaceName;
                        secondPlaceImageUrl = firstPlaceImageUrl;
                        firstPlaceScore = score;
                        firstPlaceName = data.child("username").getValue(String.class);
                        firstPlaceImageUrl = data.child("profilePhotoUri").getValue(String.class);
                    }
                    else if (secondPlaceScore < score) {
                        thirdPlaceScore = secondPlaceScore;
                        thirdPlaceName = secondPlaceName;
                        thirdPlaceImageUrl = secondPlaceImageUrl;
                        secondPlaceScore = score;
                        secondPlaceName = data.child("username").getValue(String.class);
                        secondPlaceImageUrl = data.child("profilePhotoUri").getValue(String.class);
                    }
                    else if (thirdPlaceScore < score) {
                        thirdPlaceScore = score;
                        thirdPlaceName = data.child("username").getValue(String.class);
                        thirdPlaceImageUrl = data.child("profilePhotoUri").getValue(String.class);
                    }
                }

                CircleImageView goldPlaceTraineeCircleImageView = getView().findViewById(R.id.gold_place_trainee_img);
                TextView goldPlaceTraineeNameTextView = getView().findViewById(R.id.gold_place_trainee_name);
                TextView goldPlaceTraineeScoreTextView = getView().findViewById(R.id.gold_place_trainee_score);

                CircleImageView silverPlaceTraineeCircleImageView = getView().findViewById(R.id.silver_place_trainee_img);
                TextView silverPlaceTraineeNameTextView = getView().findViewById(R.id.silver_place_trainee_name);
                TextView silverPlaceTraineeScoreTextView = getView().findViewById(R.id.silver_place_trainee_score);

                CircleImageView bronzePlaceTraineeCircleImageView = getView().findViewById(R.id.bronze_place_trainee_img);
                TextView bronzePlaceTraineeNameTextView = getView().findViewById(R.id.bronze_place_trainee_name);
                TextView bronzePlaceTraineeScoreTextView = getView().findViewById(R.id.bronze_place_trainee_score);

                Picasso.with(getContext()).load(firstPlaceImageUrl).fit().into(goldPlaceTraineeCircleImageView);
                goldPlaceTraineeNameTextView.setText(firstPlaceName);
                goldPlaceTraineeScoreTextView.setText(Integer.toString(firstPlaceScore));

                Picasso.with(getContext()).load(secondPlaceImageUrl).fit().into(silverPlaceTraineeCircleImageView);
                silverPlaceTraineeNameTextView.setText(secondPlaceName);
                silverPlaceTraineeScoreTextView.setText(Integer.toString(secondPlaceScore));

                Picasso.with(getContext()).load(thirdPlaceImageUrl).fit().into(bronzePlaceTraineeCircleImageView);
                bronzePlaceTraineeNameTextView.setText(thirdPlaceName);
                bronzePlaceTraineeScoreTextView.setText(Integer.toString(thirdPlaceScore));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}


