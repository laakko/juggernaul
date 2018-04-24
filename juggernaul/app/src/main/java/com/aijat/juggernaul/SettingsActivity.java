package com.aijat.juggernaul;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    private Button resetBtn;
    private Button generateMockDataBtn;
    private Button deleteCompletedBtn;
    private ImageButton backBtn;
    private Switch darkSwitch, completedSwitch, dueSwitch, importantSwitch, scheduleSwitch, switchHideCompleted, switchAutoDelete;
    public static boolean completedtasks, duetasks, importanttasks, scheduledtasks;
    // Handle back button
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (keyCode == KeyEvent.KEYCODE_BACK ) {
            startActivity(new Intent(SettingsActivity.this, MainActivity.class));
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPrefs = getSharedPreferences("com.aijat.juggernaul", MODE_PRIVATE);
        if (sharedPrefs.getBoolean("dark",true)) {
            SettingsActivity.this.setTheme(R.style.Dark);
        } else {
            SettingsActivity.this.setTheme(R.style.light);
        }
        setContentView(R.layout.activity_settings);
        darkSwitch = findViewById(R.id.switchDark);
        completedSwitch = findViewById(R.id.switchCompleted);
        dueSwitch = findViewById(R.id.switchDueWeek);
        importantSwitch = findViewById(R.id.switchImportant);
        scheduleSwitch = findViewById(R.id.switchSchedule);

        switchHideCompleted = findViewById(R.id.switchHideCompleted);
        switchAutoDelete = findViewById(R.id.switchAutoDelete);

        // Save switch states
        darkSwitch.setChecked(sharedPrefs.getBoolean("dark", true));
        completedSwitch.setChecked(sharedPrefs.getBoolean("completedtasks", true));
        dueSwitch.setChecked(sharedPrefs.getBoolean("duetasks", true));
        importantSwitch.setChecked(sharedPrefs.getBoolean("importanttasks", true));
        scheduleSwitch.setChecked(sharedPrefs.getBoolean("scheduledtasks", true));
        switchHideCompleted.setChecked(sharedPrefs.getBoolean("hidecompleted", true));
        switchAutoDelete.setChecked(sharedPrefs.getBoolean("autodelete", true));


        // Set app background to dark
        darkSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(darkSwitch.isChecked()){
                    SettingsActivity.this.setTheme(R.style.Dark);
                    SharedPreferences.Editor editor = getSharedPreferences("com.aijat.juggernaul", MODE_PRIVATE).edit();
                    editor.putBoolean("dark", true);
                    editor.commit();
                    startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
                } else {
                    SettingsActivity.this.setTheme(R.style.AppTheme);
                    SharedPreferences.Editor editor = getSharedPreferences("com.aijat.juggernaul", MODE_PRIVATE).edit();
                    editor.putBoolean("dark", false);
                    editor.commit();
                    startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
                }

            }
        });


        // completed tasks - switch
        completedSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(completedSwitch.isChecked()){
                    SharedPreferences.Editor editor = getSharedPreferences("com.aijat.juggernaul", MODE_PRIVATE).edit();
                    editor.putBoolean("completedtasks", true);
                    editor.commit();
                    startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
                } else {
                    SharedPreferences.Editor editor = getSharedPreferences("com.aijat.juggernaul", MODE_PRIVATE).edit();
                    editor.putBoolean("completedtasks", false);
                    editor.commit();
                    startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
                }

            }
        });

        // due this week - switch
        dueSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(dueSwitch.isChecked()){
                    SharedPreferences.Editor editor = getSharedPreferences("com.aijat.juggernaul", MODE_PRIVATE).edit();
                    editor.putBoolean("duetasks", true);
                    editor.commit();
                    startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
                } else {
                    SharedPreferences.Editor editor = getSharedPreferences("com.aijat.juggernaul", MODE_PRIVATE).edit();
                    editor.putBoolean("duetasks", false);
                    editor.commit();
                    startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
                }

            }
        });

        // your important tasks - switch
        importantSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(importantSwitch.isChecked()){
                    SharedPreferences.Editor editor = getSharedPreferences("com.aijat.juggernaul", MODE_PRIVATE).edit();
                    editor.putBoolean("importanttasks", true);
                    editor.commit();
                    startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
                } else {
                    SharedPreferences.Editor editor = getSharedPreferences("com.aijat.juggernaul", MODE_PRIVATE).edit();
                    editor.putBoolean("importanttasks", false);
                    editor.commit();
                    startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
                }

            }
        });

        // scheduled tasks - switch
        scheduleSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(scheduleSwitch.isChecked()){
                    SharedPreferences.Editor editor = getSharedPreferences("com.aijat.juggernaul", MODE_PRIVATE).edit();
                    editor.putBoolean("scheduledtasks", true);
                    editor.commit();
                    startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
                } else {
                    SharedPreferences.Editor editor = getSharedPreferences("com.aijat.juggernaul", MODE_PRIVATE).edit();
                    editor.putBoolean("scheduledtasks", false);
                    editor.commit();
                    startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
                }

            }
        });


        // Hide completed tasks - switch
        switchHideCompleted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(switchHideCompleted.isChecked()){
                    SharedPreferences.Editor editor = getSharedPreferences("com.aijat.juggernaul", MODE_PRIVATE).edit();
                    editor.putBoolean("hidecompleted", true);
                    editor.commit();
                    startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
                } else {
                    SharedPreferences.Editor editor = getSharedPreferences("com.aijat.juggernaul", MODE_PRIVATE).edit();
                    editor.putBoolean("hidecompleted", false);
                    editor.commit();
                    startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
                }

            }
        });

        // Auto Delete when DL passes - switch
        switchAutoDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(switchAutoDelete.isChecked()){
                    SharedPreferences.Editor editor = getSharedPreferences("com.aijat.juggernaul", MODE_PRIVATE).edit();
                    editor.putBoolean("autodelete", true);
                    editor.commit();
                    startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
                } else {
                    SharedPreferences.Editor editor = getSharedPreferences("com.aijat.juggernaul", MODE_PRIVATE).edit();
                    editor.putBoolean("autodelete", false);
                    editor.commit();
                    startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
                }

            }
        });


        deleteCompletedBtn = findViewById(R.id.btnDeleteCompleted);
        deleteCompletedBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                deleteCompletedAlert();
            }
        });

        resetBtn = findViewById(R.id.resetButton);
        resetBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                resetAlert();
            }
        });
        generateMockDataBtn = findViewById(R.id.loadMockButton);
        generateMockDataBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                generateMockDataButtonClicked(v);
            }
        });

        backBtn = findViewById(R.id.backButton);
        backBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(SettingsActivity.this, MainActivity.class));
            }
        });

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
            Task newTask = new Task("Mock data title no. " + Integer.toString(realIndex), "Description" + Integer.toString(realIndex), Task.Priority.LOW, date, Task.TaskCategory.OTHER, Task.Status.TODO, "User" + Integer.toString(realIndex), "Group" + Integer.toString(realIndex), false, false);
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

    public void resetAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to reset? This will delete all tasks")
                .setCancelable(false)
                .setPositiveButton("Yes, reset everything", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        TaskService.ResetEverything(getApplicationContext());
                        Toast toast = Toast.makeText(getApplicationContext(), "App reset successful!", Toast.LENGTH_SHORT);
                        toast.show();

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(230, 68, 88, 120))); // ColorPrimary
        alert.show();
    }

    public void deleteCompletedAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete all completed tasks?")
                .setCancelable(false)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        ArrayList<Task> alltasks = TaskService.GetAllNotDeletedTasks(getApplication().getApplicationContext());
                        for(Task oneTask : alltasks) {
                            if(oneTask.getStatus() == Task.Status.DONE) {
                                oneTask.setDeleted(true);
                                oneTask.SaveToFile(getApplication().getApplicationContext());
                            }
                        }

                        Toast toast = Toast.makeText(getApplicationContext(), "Completed tasks removed!", Toast.LENGTH_SHORT);
                        toast.show();

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(230, 68, 88, 120))); // ColorPrimary
        alert.show();
    }
}
