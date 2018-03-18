package com.aijat.juggernaul;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;

import java.util.Calendar;

import static com.aijat.juggernaul.ListTab.allTasks;
import static com.aijat.juggernaul.Task.Priority.HIGH;
import static com.aijat.juggernaul.Task.Priority.LOW;
import static com.aijat.juggernaul.Task.Priority.MEDIUM;

public class TaskActivity extends AppCompatActivity {

    private EditText txtTaskTitle;
    private EditText txtDescription;
    private View rect;
    private EditText txtTaskDeadline;
    private Calendar calendar;
    private Spinner statusSpinner;
    private Spinner categorySpinner;
    private Button saveButton;
    private Button cancelButton;
    private FloatingActionButton sharebutton;
    private FloatingActionButton deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        final int taskId = getIntent().getIntExtra("taskId", -1);

        final Task taskInEditing = allTasks.get(taskId);

        // Title
        txtTaskTitle = findViewById(R.id.txtTaskTitle);
        txtTaskTitle.setText(taskInEditing.getTitle());
        txtTaskTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                taskInEditing.setTitle(editable.toString());
            }
        });

        // Deadline
        calendar = Calendar.getInstance();
        txtTaskDeadline = findViewById(R.id.txtTaskDeadline);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, monthOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                taskInEditing.setDeadline(calendar.getTime());
                txtTaskDeadline.setText("Deadline:\n" + taskInEditing.getDeadlinePretty().toString());
            }
        };
        txtTaskDeadline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(TaskActivity.this, date, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        txtTaskDeadline.setText("Deadline:\n" + taskInEditing.getDeadlinePretty().toString());

        // Priority
        rect = findViewById(R.id.rectPriority);
        rect.setOnClickListener(new View.OnClickListener() {
            @Override
        public void onClick(View v) {
            priority_dialog(taskInEditing);
        }
    });


        if(taskInEditing.getPriority() == LOW) {
            rect.setBackgroundColor(Color.parseColor("#66bb6a"));
        } else if (taskInEditing.getPriority() == MEDIUM) {
            rect.setBackgroundColor(Color.parseColor("#ffa726"));
        } else if (taskInEditing.getPriority() == HIGH) {
            rect.setBackgroundColor(Color.parseColor("#bf360c"));
        }


        // Status
        statusSpinner = findViewById(R.id.statusSpinner);
        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Task.Status selectedStatus = Task.Status.values()[i];
                if (selectedStatus != taskInEditing.getStatus()) {
                    taskInEditing.setStatus(selectedStatus);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.i("Error", "You must select something from the status list");
            }
        });
        statusSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Task.Status.values()));
        statusSpinner.setSelection(allTasks.get(taskId).getStatus().ordinal()); // Get status value from task and update spinner accordingly

        // Category
        categorySpinner = findViewById(R.id.categorySpinner );
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Task.TaskCategory selectedCategory = Task.TaskCategory.values()[i];
                if (selectedCategory != taskInEditing.getCategory()) {
                    taskInEditing.setCategory(selectedCategory);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.i("Error", "You must select something from the category list");
            }
        });
        categorySpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Task.TaskCategory.values()));
        categorySpinner.setSelection(allTasks.get(taskId).getCategory().ordinal()); // Get status value from task and update spinner accordingly

        // Description
        txtDescription = findViewById(R.id.txtDescription);
        txtDescription.setText(taskInEditing.getDescription());
        txtDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                taskInEditing.setDescription(editable.toString());
            }
        });

        // Save button
        saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    setResult(Activity.RESULT_OK);
                    finish();
                }
                boolean saved = taskInEditing.SaveToFile(getApplication().getApplicationContext());
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

        // Cancel button
        cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setEnabled(true);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                taskInEditing.Assign(allTasks.get(taskId));
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    setResult(Activity.RESULT_OK);
                    finish();
                }
            }
        });

        // Share button
        sharebutton = findViewById(R.id.fab_share);
        sharebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(TaskActivity.this, ShareActivity.class));
            }
        });

        // Delete button
        deleteButton = findViewById(R.id.fab_delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Prompt user about task deletion
                delete_alert("Are you sure you want to delete this Task?", "Yes", "No", taskInEditing);
            }
        });


    }


    public void priority_dialog(final Task task) {
        final String[] items = new String[] {
                "Low", "Medium", "High"
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose a new priority").setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(items[i] == "Low")
                    task.setPriority(LOW);
                else if(items[i] == "Medium")
                    task.setPriority(MEDIUM);
                else
                    task.setPriority(HIGH);

                if(task.getPriority() == LOW) {
                    rect.setBackgroundColor(Color.parseColor("#66bb6a"));
                } else if (task.getPriority() == MEDIUM) {
                    rect.setBackgroundColor(Color.parseColor("#ffa726"));
                } else if (task.getPriority() == HIGH) {
                    rect.setBackgroundColor(Color.parseColor("#bf360c"));
                }
                task.SaveToFile(getApplication().getApplicationContext());
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void delete_alert(String message, String positive_value, String negative_value, final Task task) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false)
                .setPositiveButton(positive_value, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        // Delete the task and go back to MainMenu
                        task.setDeleted(true);
                        if(task.isDeleted()) {
                            Toast toast = Toast.makeText(getApplicationContext().getApplicationContext(), "Task successfully deleted!", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        task.SaveToFile(getApplication().getApplicationContext());

                        setResult(Activity.RESULT_OK);
                        finish();

                    }
                })
                .setNegativeButton(negative_value, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
