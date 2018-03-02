package com.aijat.juggernaul;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// This is the home view!

/*

TODO

 */


public class HomeTab extends Fragment implements View.OnClickListener {

    private Button resetBtn;
    private Button generateMockDataBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        resetBtn = view.findViewById(R.id.resetButton);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                resetButtonClicked(v);
            }
        });
        generateMockDataBtn = view.findViewById(R.id.loadMockButton);
        generateMockDataBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                generateMockDataButtonClicked(v);
            }
        });
        return view;
    }

    public void resetButtonClicked(View v) {
        TaskService.ResetEverything(v, getActivity().getApplicationContext());
        Snackbar resetNotification = Snackbar.make(v, "App reset successful!", Snackbar.LENGTH_SHORT);
        resetNotification.show();
    }

    public void generateMockDataButtonClicked(View v) {
        TaskService.ResetEverything(v, getActivity().getApplicationContext());
        List<Task> mockTasks = new ArrayList<Task>();
        for (int i = 1; i < 26; i++) {
            Task newTask = new Task("Title" + Integer.toString(i), "Description" + Integer.toString(i), Task.Priority.HIGH, new Date(), Task.TaskCategory.OTHER, Task.Status.TODO, "User" + Integer.toString(i), "Group" + Integer.toString(i));
            mockTasks.add(newTask);
        }
        for (Task task : mockTasks) {
            task.SaveToFile(getActivity().getApplicationContext());
        }
        Snackbar loadMockDataNotification = Snackbar.make(v, "Mock data loaded!", Snackbar.LENGTH_SHORT);
        loadMockDataNotification.show();
    }

    public void onClick(View v) {

    }
}

