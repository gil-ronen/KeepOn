package com.gil_shiran_or.keepon.trainee.my_trainers;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gil_shiran_or.keepon.R;

import java.util.List;

public class MyTrainersListAdapter extends RecyclerView.Adapter<MyTrainersListAdapter.MyTrainersViewHolder> {

    private List<MyTrainerItem> myTrainersList;
    private Context context;

    public static class MyTrainersViewHolder extends RecyclerView.ViewHolder {

        public CardView trainerCardView;
        public TextView trainerNameTextView;

        public MyTrainersViewHolder(View itemView) {
            super(itemView);
            trainerCardView = itemView.findViewById(R.id.my_trainer_item);
            trainerNameTextView = itemView.findViewById(R.id.my_trainer_item_trainer_name);
        }
    }

    public MyTrainersListAdapter(List<MyTrainerItem> myTrainersList, Context context) {
        this.myTrainersList = myTrainersList;
        this.context = context;
    }

    @Override
    public MyTrainersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_trainer_item, parent, false);

        return new MyTrainersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyTrainersViewHolder holder, int position) {
        MyTrainerItem currentMyTrainerItem = myTrainersList.get(position);

        holder.trainerCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MyTrainerActivity.class);
                context.startActivity(intent);
            }
        });

        holder.trainerNameTextView.setText(currentMyTrainerItem.getTrainerName());
    }

    @Override
    public int getItemCount() {
        return myTrainersList.size();
    }
}
