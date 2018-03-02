package com.aijat.juggernaul;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

// This is the home view!

/*

TODO

 */


public class HomeTab extends Fragment implements View.OnClickListener {

    private Button resetBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        resetBtn = view.findViewById(R.id.resetButton);
        resetBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        Task testiTask = new Task(  "Tämä on title",
                                    "Tämä on description",
                                    Task.Priority.LOW,
                                    new Date(),
                                    Task.TaskCategory.OTHER,
                                    Task.Status.TODO,
                                    "Ilmari",
                                    "Ryhmärämä");

        TaskService.ResetEverything(v, getActivity().getApplicationContext());
        testiTask.SaveToFile(getActivity().getApplicationContext());
        List<Task> allTasks = TaskService.GetAllTasks(getActivity().getApplicationContext());

        for (Task task : allTasks) {
            task.Print();
        }

        allTasks.get(1).setStatus(Task.Status.DELETED);
        allTasks.get(1).setTitle("Uusi title");
        allTasks.get(1).setDescription("Ja uutta descriptiota");
        allTasks.get(1).SaveToFile(getActivity().getApplicationContext());
        allTasks = TaskService.GetAllTasks(getActivity().getApplicationContext());

        for (Task task : allTasks) {
            task.Print();
        }
    }
}

