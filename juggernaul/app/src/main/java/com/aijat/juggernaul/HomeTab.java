package com.aijat.juggernaul;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

public class HomeTab extends Fragment implements View.OnClickListener {

    public static ArrayList<Task> importantTasks;
    public static ArrayList<Task> thisweeksTasks;
    TaskArrayHomeAdapter taskArrayHomeAdapter;
    TaskArrayHomeAdapter taskArrayHomeAdapter2;

    public void onResume() {
        super.onResume();
        refreshContent();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);
        GridView gridView = view.findViewById(R.id.gridview);
        GridView gridView2 = view.findViewById(R.id.gridview2);
        TextView textView2 = view.findViewById(R.id.txtThisWeek);

        importantTasks = TaskService.GetImportantNotDeletedTasks(getActivity().getApplication());
        taskArrayHomeAdapter = new TaskArrayHomeAdapter(getContext().getApplicationContext(), importantTasks);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int selectedTaskId = taskArrayHomeAdapter.getItem(i).getId();
                Intent intent = new Intent(getActivity(), TaskActivity.class);
                intent.putExtra("taskId", selectedTaskId);
                startActivityForResult(intent, 0);
            }
        });
        
        gridView.setAdapter(taskArrayHomeAdapter);


        thisweeksTasks = TaskService.GetThisWeeksTasks(getActivity().getApplication());
        if(thisweeksTasks.isEmpty())
            textView2.setVisibility(View.INVISIBLE);
        else
            textView2.setVisibility(View.VISIBLE);

        taskArrayHomeAdapter2 = new TaskArrayHomeAdapter(getContext().getApplicationContext(), thisweeksTasks);

        gridView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int selectedTaskId = taskArrayHomeAdapter2.getItem(i).getId();
                Intent intent = new Intent(getActivity(), TaskActivity.class);
                intent.putExtra("taskId", selectedTaskId);
                startActivityForResult(intent, 0);
            }
        });

        gridView2.setAdapter(taskArrayHomeAdapter2);


        return view;
    }

    // Handle action menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        // Hide sort options in HomeTab
        MenuItem sort1 = menu.findItem(R.id.sort1); sort1.setVisible(false);
        MenuItem sort2 = menu.findItem(R.id.sort2); sort2.setVisible(false);
        MenuItem sort3 = menu.findItem(R.id.sort3); sort3.setVisible(false);
        MenuItem sort4 = menu.findItem(R.id.sort4); sort4.setVisible(false);
        MenuItem check1 = menu.findItem(R.id.checkOther); check1.setVisible(false);
        MenuItem check2 = menu.findItem(R.id.checkWork); check2.setVisible(false);
        MenuItem check3 = menu.findItem(R.id.checkSchool); check3.setVisible(false);

    } @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {

            case R.id.settings:
                // Go to Settings View
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            case R.id.joingroup:
                // Go to Join Group View
                startActivity(new Intent(getActivity(), JoinGroupActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void refreshContent() {
        importantTasks = TaskService.GetImportantNotDeletedTasks(getActivity().getApplicationContext());
        taskArrayHomeAdapter.clear();
        taskArrayHomeAdapter.addAll(importantTasks);
        taskArrayHomeAdapter.notifyDataSetChanged();

        thisweeksTasks = TaskService.GetThisWeeksTasks(getActivity().getApplication());
        taskArrayHomeAdapter2.clear();
        taskArrayHomeAdapter2.addAll(thisweeksTasks);
        taskArrayHomeAdapter2.notifyDataSetChanged();


    }

    public void onClick(View v) {
    }
}

