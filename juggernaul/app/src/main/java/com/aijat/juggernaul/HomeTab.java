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
import android.widget.GridView;

import java.util.ArrayList;

public class HomeTab extends Fragment implements View.OnClickListener {

    public static ArrayList<Task> importantTasks;
    TaskArrayHomeAdapter taskArrayHomeAdapter;

    public void onResume() {
        super.onResume();
        refreshContent();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);
        GridView gridView = view.findViewById(R.id.gridview);


        importantTasks = TaskService.GetImportantTasks(getActivity().getApplication());

        taskArrayHomeAdapter = new TaskArrayHomeAdapter(getContext().getApplicationContext(), importantTasks);
        gridView.setAdapter(taskArrayHomeAdapter);


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
        importantTasks = TaskService.GetImportantTasks(getActivity().getApplicationContext());
        taskArrayHomeAdapter.clear();
        taskArrayHomeAdapter.addAll(importantTasks);
        taskArrayHomeAdapter.notifyDataSetChanged();
    }


    public void onClick(View v) {

    }
}

