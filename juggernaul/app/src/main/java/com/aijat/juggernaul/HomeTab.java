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
        TaskService.ResetEverything(getActivity().getApplicationContext());
        Toast toast = Toast.makeText(getContext().getApplicationContext(), "App reset successful!", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void generateMockDataButtonClicked(View v) {
        TaskService.ResetEverything(getActivity().getApplicationContext());
        List<Task> mockTasks = new ArrayList<>();
        for (int i = 1; i < 26; i++) {
            int realIndex = i-1;
            Task newTask = new Task("Title" + Integer.toString(realIndex), "Description" + Integer.toString(realIndex), Task.Priority.HIGH, new Date(), Task.TaskCategory.OTHER, Task.Status.TODO, "User" + Integer.toString(realIndex), "Group" + Integer.toString(realIndex));
            mockTasks.add(newTask);
        }
        for (Task task : mockTasks) {
            task.SaveToFile(getActivity().getApplicationContext());
        }
        Toast toast = Toast.makeText(getContext().getApplicationContext(), "Mock data loaded!", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void onClick(View v) {

    }
}

