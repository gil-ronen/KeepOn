package com.gil_shiran_or.keepon.trainee.ui.status;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gil_shiran_or.keepon.R;

import java.util.List;

public class WeeklyTasksListAdapter extends RecyclerView.Adapter<WeeklyTasksListAdapter.WeeklyTasksListViewHolder> {

    private List<WeeklyTaskItem> weeklyTasksList;

    public static class WeeklyTasksListViewHolder extends RecyclerView.ViewHolder {

        public TextView taskDescriptionTextView;
        public TextView taskScoreTextView;

        public WeeklyTasksListViewHolder(View itemView) {
            super(itemView);
            taskDescriptionTextView = itemView.findViewById(R.id.weekly_task_item_description);
            taskScoreTextView = itemView.findViewById(R.id.weekly_task_item_score);
        }
    }

    public WeeklyTasksListAdapter(List<WeeklyTaskItem> weeklyTasksList) {
        this.weeklyTasksList = weeklyTasksList;
    }

    @Override
    public WeeklyTasksListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weekly_task_item, parent, false);

        return new WeeklyTasksListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WeeklyTasksListViewHolder holder, int position) {
        WeeklyTaskItem currentWeeklyTaskItem = weeklyTasksList.get(position);

        holder.taskDescriptionTextView.setText(currentWeeklyTaskItem.getDescription());
        holder.taskScoreTextView.setText(Integer.toString(currentWeeklyTaskItem.getScore()));
    }

    @Override
    public int getItemCount() {
        return weeklyTasksList.size();
    }
}
