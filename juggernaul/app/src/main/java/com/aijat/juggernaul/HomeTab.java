package com.aijat.juggernaul;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.List;

// This is the home view!

/*

TODO

 */


public class HomeTab extends Fragment implements View.OnClickListener {

    private CheckBox chkBx;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        chkBx = view.findViewById(R.id.checkBoxi);
        chkBx.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        chkBx.setText("Jees");
        Date date = new Date();
        Task testiTask = new Task(  "Tämä on title",
                                    "Tämä on description",
                                    Task.Priority.LOW,
                                    date,
                                    Task.TaskCategory.OTHER,
                                    Task.Status.TODO,
                                    "Ilmari",
                                    "Ryhmärämä");

        TaskService.ResetEverything(getActivity().getApplicationContext());
        List<Task> shouldBeEmpty = TaskService.GetAllTasks(getActivity().getApplicationContext());
        TaskService.CreateTask(getActivity().getApplicationContext(), testiTask);
        List<Task> shouldHaveOne = TaskService.GetAllTasks(getActivity().getApplicationContext());

        for (int i = 0; i<15; i++) {
            boolean created = TaskService.CreateTask(getActivity().getApplicationContext(), testiTask);
            if (!created) {
                Log.i("TaskCreationError", "Could not create a task.");
                return;
            }
        }

        List<Task> allTasks = TaskService.GetAllTasks(getActivity().getApplicationContext());
        for (int i = 0; i<allTasks.size(); i++) {
            Log.i("task", allTasks.get(i).JSONify().toString());
        }
    }

}

