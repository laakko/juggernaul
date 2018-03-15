package com.aijat.juggernaul;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;

public class HomeTab extends Fragment implements View.OnClickListener {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        GridView gridView = view.findViewById(R.id.gridview);

        //ArrayList<Task> tasks = TaskService.GetAllTasks(getActivity().getApplication());
        ArrayList<Task> tasks = TaskService.GetImportantTasks(getActivity().getApplication());

        TaskArrayHomeAdapter taskArrayHomeAdapter = new TaskArrayHomeAdapter(getContext().getApplicationContext(), tasks);
        gridView.setAdapter(taskArrayHomeAdapter);


        return view;
    }

    public void onClick(View v) {

    }
}

