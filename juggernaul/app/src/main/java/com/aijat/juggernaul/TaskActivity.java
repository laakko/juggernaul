package com.aijat.juggernaul;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;

import java.util.List;

public class TaskActivity extends AppCompatActivity {

    private TextView txtTaskTitle;
    private View rect;
    private TextView txtTaskDeadline;
    private TextView txtTaskStatus;
    private FloatingActionButton sharebutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        int taskId = getIntent().getIntExtra("taskId", -1);

        List<Task> allTasks = TaskService.GetAllTasks(getApplication().getApplicationContext());

        // Temp ----------------------
        rect = (View) findViewById(R.id.rectPriority);
        rect.setBackgroundColor(Color.rgb(137, 244, 66));


        txtTaskTitle = (TextView) findViewById(R.id.txtTaskTitle);
        txtTaskDeadline = (TextView) findViewById(R.id.txtTaskDeadline);
        txtTaskStatus = (TextView) findViewById(R.id.txtTaskStatus);

        txtTaskTitle.setText(allTasks.get(taskId).getTitle());
        txtTaskDeadline.setText("DL: " + allTasks.get(taskId).getDeadline().toString());
        txtTaskStatus.setText("Status: " + allTasks.get(taskId).getStatus().toString());

        // -----------------------------

        sharebutton = (FloatingActionButton) findViewById(R.id.fab_share);
        sharebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TaskActivity.this, ShareActivity.class));
            }
        });
    }

}
