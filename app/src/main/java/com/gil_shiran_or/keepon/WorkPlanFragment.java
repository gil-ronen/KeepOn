package com.gil_shiran_or.keepon;

import android.media.MediaPlayer;
import android.support.v4.app.Fragment; import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;


public class WorkPlanFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.workplan_main, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ArrayList userList = getListData();
        final ListView lv = (ListView) getView().findViewById(R.id.user_list);
        lv.setAdapter(new WorkPlanCustomListAdapter(this.getContext(), userList));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                WorkPlanListItem user = (WorkPlanListItem) lv.getItemAtPosition(position);
            }
        });
    }

    private ArrayList getListData() {
        ArrayList<WorkPlanListItem> results = new ArrayList<>();
        WorkPlanListItem user1 = new WorkPlanListItem();
        user1.setName("Exercise number one");
        user1.setDesignation("Number of repeats");
        user1.setLocation("Amount");
        results.add(user1);
        WorkPlanListItem user2 = new WorkPlanListItem();
        user2.setName("Exercise number two");
        user2.setDesignation("Number of repeats");
        user2.setLocation("Amount");
        results.add(user2);
        WorkPlanListItem user3 = new WorkPlanListItem();
        user3.setName("Exercise number three");
        user3.setDesignation("Number of repeats");
        user3.setLocation("Amount");
        results.add(user3);
        return results;
    }
}
