package com.aijat.juggernaul;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;

import static com.aijat.juggernaul.ListTab.allTasks;

public class TaskActivity extends AppCompatActivity {

    private TextView txtTaskTitle;
    private TextView txtDescription;
    private View rect;
    private TextView txtTaskDeadline;
    private Spinner statusSpinner;
    private FloatingActionButton sharebutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        final int taskId = getIntent().getIntExtra("taskId", -1);

        rect = (View) findViewById(R.id.rectPriority);
        rect.setBackgroundColor(Color.rgb(137, 244, 66));


        txtTaskTitle = findViewById(R.id.txtTaskTitle);
        txtDescription = findViewById(R.id.txtDescription);
        txtTaskDeadline = findViewById(R.id.txtTaskDeadline);
        statusSpinner = findViewById(R.id.statusSpinner);
        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Task.Status selectedStatus = Task.Status.values()[i];
                if (selectedStatus != allTasks.get(taskId).getStatus()) {
                    allTasks.get(taskId).setStatus(selectedStatus);
                    allTasks.get(taskId).SaveToFile(getApplication().getApplicationContext());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.i("Error", "You must select something from the status list");
            }
        });
        statusSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Task.Status.values()));
        statusSpinner.setSelection(allTasks.get(taskId).getStatus().ordinal()); // Get status value from task and update spinner accordingly

        txtTaskTitle.setText(allTasks.get(taskId).getTitle());
        txtDescription.setText(allTasks.get(taskId).getDescription());
        txtTaskDeadline.setText("Deadline:\n" + allTasks.get(taskId).getDeadlinePretty().toString());


        sharebutton = (FloatingActionButton) findViewById(R.id.fab_share);
        sharebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TaskActivity.this, ShareActivity.class));
            }
        });
    }
}
