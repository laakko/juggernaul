package com.aijat.juggernaul;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
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
import java.util.Random;

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
    private FloatingActionButton notificationButton;
    private FloatingActionButton scheduleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPrefs = getSharedPreferences("com.aijat.juggernaul", MODE_PRIVATE);
        if (sharedPrefs.getBoolean("dark",true)) {
            TaskActivity.this.setTheme(R.style.Dark);
        } else {
            TaskActivity.this.setTheme(R.style.light);
        }
        setContentView(R.layout.activity_task);
        final int taskId = getIntent().getIntExtra("taskId", -1);
        int taskIndexTemp = 0;
        for (Task oneTask : allTasks) {
            if (oneTask.getId() == taskId) {
                taskIndexTemp = allTasks.indexOf(oneTask);
            }
        }

        final int taskIndex = taskIndexTemp;

        final Task taskInEditing = allTasks.get(taskIndex);

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
                    ListTab.konfetti = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.i("Error", "You must select something from the status list");
            }
        });
        statusSpinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Task.Status.values()));
        statusSpinner.setSelection(allTasks.get(taskIndex).getStatus().ordinal()); // Get status value from task and update spinner accordingly

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
        categorySpinner.setSelection(allTasks.get(taskIndex).getCategory().ordinal()); // Get status value from task and update spinner accordingly

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
                    if(taskInEditing.getStatus() != Task.Status.DONE) {
                        ListTab.konfetti=false;
                        Toast toast = Toast.makeText(getApplicationContext().getApplicationContext(), "Task successfully saved!", Toast.LENGTH_SHORT);
                        toast.show();
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext().getApplicationContext(), "\n Well done, \n you JuggerNaul'd the task! \n *Finlandia plays* \n", Toast.LENGTH_LONG);
                        toast.show();
                    }

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
                taskInEditing.Assign(allTasks.get(taskIndex));
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
                delete_alert("Are you sure you want to delete this Task?", "Delete", "Cancel", taskInEditing);
            }
        });

        // Pin As Notification button
        notificationButton = findViewById(R.id.fab_notification);
        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Pin task details as a notification
                Log.i("content", taskInEditing.getDescription().toString());
                String task_content = "";
                if(taskInEditing.getDescription().startsWith("(Optional)")) {
                    task_content = "DL: " + taskInEditing.getDeadlinePretty().toString();
                } else {
                    task_content = "DL: " + taskInEditing.getDeadlinePretty().toString() + "\n" + taskInEditing.getDescription().toString();
                }


                if(taskInEditing.getCategory() == Task.TaskCategory.SCHOOL) {
                    AddNotification(taskInEditing.getTitle().toString(), task_content, R.drawable.school_icon_black);
                } else if(taskInEditing.getCategory() == Task.TaskCategory.WORK) {
                    AddNotification(taskInEditing.getTitle().toString(), task_content, R.drawable.work_icon_black);
                } else {
                    AddNotification(taskInEditing.getTitle().toString(), task_content, R.drawable.other_icon_black);
                }
            }
        });

        // Add to shcedule button
        scheduleButton = findViewById(R.id.fab_schedule);
        scheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(taskInEditing.getStatus() != Task.Status.DONE)
                    taskInEditing.setScheduled(true);
                if(taskInEditing.isScheduled()) {
                    Toast toast = Toast.makeText(getApplication().getApplicationContext(), "Task added to your schedule!", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(getApplication().getApplicationContext(), "Can't add completed task to schedule, change status first!", Toast.LENGTH_SHORT);
                    toast.show();
                }
                taskInEditing.SaveToFile(getApplication().getApplicationContext());
            }
        });

    }


    public void priority_dialog(final Task task) {
        final String[] items = new String[] {
                "Low", "Medium", "High"
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose a new priority").setIcon(R.mipmap.ic_launcher).setItems(items, new DialogInterface.OnClickListener() {
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
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(230,68, 88, 120)));
        dialog.show();
    }

    public void delete_alert(String message, String positive_value, String negative_value, final Task task) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setCancelable(false)
                .setNegativeButton(positive_value, new DialogInterface.OnClickListener() {

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
                .setPositiveButton(negative_value, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(230, 68, 88, 120))); // ColorPrimary
        alert.show();
    }


    public void AddNotification(String title, String content, int icon) {

        // Necessary for Android Oreo -> )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            CharSequence name = "Task details notification";
            String description = "Juggernaul pinned task";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("juggerID", name, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(description);
            // Register the channel with the system
            NotificationManager notificationManager =
                    (NotificationManager) getApplication().getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
        }


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "juggerID")
                .setSmallIcon(icon)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(new NotificationCompat.BigTextStyle()
                .bigText(content));


        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendint = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        mBuilder.setContentIntent(pendint);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        // notificationId is a unique int for each notification that you must define
        Random rand = new Random(); int notificationId = rand.nextInt(10000);
        notificationManager.notify(notificationId, mBuilder.build());


    }

}
