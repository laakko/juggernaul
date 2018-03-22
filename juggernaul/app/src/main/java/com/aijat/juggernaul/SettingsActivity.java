package com.aijat.juggernaul;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    private Button resetBtn;
    private Button generateMockDataBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().setTitle("Juggernaul - Settings");
        resetBtn = findViewById(R.id.resetButton);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                resetButtonClicked(v);
            }
        });
        generateMockDataBtn = findViewById(R.id.loadMockButton);
        generateMockDataBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                generateMockDataButtonClicked(v);
            }
        });
    }

    public void resetButtonClicked(View v) {
        TaskService.ResetEverything(getApplicationContext());
        Toast toast = Toast.makeText(getApplicationContext(), "App reset successful!", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void generateMockDataButtonClicked(View v) {
        TaskService.ResetEverything(getApplicationContext());
        List<Task> mockTasks = new ArrayList<>();
        for (int i = 1; i < 26; i++) {
            int realIndex = i-1;
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.WEEK_OF_MONTH, 1);
            Date date = c.getTime();
            Task newTask = new Task("Title" + Integer.toString(realIndex), "Description" + Integer.toString(realIndex), Task.Priority.LOW, date, Task.TaskCategory.OTHER, Task.Status.TODO, "User" + Integer.toString(realIndex), "Group" + Integer.toString(realIndex), false);
            mockTasks.add(newTask);
        }
        for (Task task : mockTasks) {
            task.SaveToFile(getApplicationContext());
        }
        Toast toast = Toast.makeText(getApplicationContext(), "Mock data loaded!", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void onClick(View v) {

    }
}
