package com.aijat.juggernaul;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;

import static com.aijat.juggernaul.ListTab.allTasks;

public class TaskActivity extends AppCompatActivity {

    private TextView txtTaskTitle;
    private EditText txtDescription;
    private View rect;
    private TextView txtTaskDeadline;
    private Spinner statusSpinner;
    private Button saveButton;
    private Button cancelButton;
    private FloatingActionButton sharebutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        final int taskId = getIntent().getIntExtra("taskId", -1);

        final Task copyTask = allTasks.get(taskId);

        rect = (View) findViewById(R.id.rectPriority);
        rect.setBackgroundColor(Color.rgb(137, 244, 66));

        txtTaskTitle = findViewById(R.id.txtTaskTitle);
        txtTaskTitle.setText(copyTask.getTitle());

        txtDescription = findViewById(R.id.txtDescription);
        txtDescription.setText(copyTask.getDescription());
        txtDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                copyTask.setDescription(editable.toString());
            }
        });

        txtTaskDeadline = findViewById(R.id.txtTaskDeadline);
        txtTaskDeadline.setText("Deadline:\n" + copyTask.getDeadlinePretty().toString());

        statusSpinner = findViewById(R.id.statusSpinner);
        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Task.Status selectedStatus = Task.Status.values()[i];
                if (selectedStatus != copyTask.getStatus()) {
                    copyTask.setStatus(selectedStatus);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.i("Error", "You must select something from the status list");
            }
        });
        statusSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Task.Status.values()));
        statusSpinner.setSelection(allTasks.get(taskId).getStatus().ordinal()); // Get status value from task and update spinner accordingly

        saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                boolean saved = copyTask.SaveToFile(getApplication().getApplicationContext());
                if (saved) {
                    Toast toast = Toast.makeText(getApplicationContext().getApplicationContext(), "Task successfully saved!", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext().getApplicationContext(), "Saving failed!", Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });
        saveButton.setEnabled(true);

        cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setEnabled(true);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    setResult(Activity.RESULT_OK);
                    finish();
                }
            }
        });


        sharebutton = findViewById(R.id.fab_share);
        sharebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TaskActivity.this, ShareActivity.class));
            }
        });
    }
}
