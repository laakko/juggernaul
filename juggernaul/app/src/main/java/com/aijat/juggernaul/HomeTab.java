package com.aijat.juggernaul;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static com.aijat.juggernaul.ListTab.hiddenCategories;
import static com.aijat.juggernaul.ListTab.updateTabs;
import static com.aijat.juggernaul.MainActivity.hidecompleted;
import static com.aijat.juggernaul.SettingsActivity.completedtasks;
import static com.aijat.juggernaul.SettingsActivity.duetasks;
import static com.aijat.juggernaul.SettingsActivity.importanttasks;
import static com.aijat.juggernaul.SettingsActivity.scheduledtasks;

public class HomeTab extends Fragment implements View.OnClickListener {

    public static ArrayList<Task> importantTasks;
    public static ArrayList<Task> thisweeksTasks;
    public static ArrayList<Task> completedTasks;
    public static ArrayList<Task> scheduledTasks;
    TaskArrayHomeAdapter taskArrayHomeAdapter;
    TaskArrayHomeAdapter taskArrayHomeAdapter2;
    TaskArrayHomeAdapter taskArrayHomeAdapter3;
    TaskArrayHomeAdapter taskArrayHomeAdapter4;

    public void onResume() {
        super.onResume();
        refreshContent();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (FileService.readFile(getContext().getApplicationContext(), "tasks.json") == null) {
            TaskService.ResetEverything(getContext().getApplicationContext());
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Welcome to Juggernaul!")
                    .setCancelable(true).setIcon(R.mipmap.ic_launcher)
                    .setMessage("Say goodbye to your other apps.\n\nJuggernaul is the best solution for time management and scheduling!\n\nStart improving your life right now by adding new tasks!\n\n\n\n© Aijat");

            AlertDialog alert = builder.create();
            alert.getWindow().setGravity(Gravity.CENTER_VERTICAL);
            alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(230, 68, 88, 120)));
            alert.show();
            alert.getWindow().setLayout(900,1200);
            TextView message = alert.findViewById(android.R.id.message);
            message.setTextSize(20);
            message.setGravity(Gravity.CENTER);
        }

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);
        GridView gridView = view.findViewById(R.id.gridview);
        GridView gridView2 = view.findViewById(R.id.gridview2);
        GridView gridView3 = view.findViewById(R.id.gridview3);
        GridView gridView4 = view.findViewById(R.id.gridview4);
        TextView textView1 = view.findViewById(R.id.txtImpTasks);
        TextView textView2 = view.findViewById(R.id.txtThisWeek);
        TextView textView3 = view.findViewById(R.id.txtCompleted);
        TextView textView4 = view.findViewById(R.id.txtScheduled);
        LinearLayout linearLayout1 = view.findViewById(R.id.linearLayout1);


        if(importanttasks) {
            importantTasks = TaskService.GetImportantNotDeletedTasks(getActivity().getApplication());
            if(importantTasks.isEmpty()) {
               // textView1.setVisibility(View.INVISIBLE);
                linearLayout1.removeView(gridView);
                linearLayout1.removeView(textView1);
            } else {
                textView1.setVisibility(View.VISIBLE);
            }
            taskArrayHomeAdapter = new TaskArrayHomeAdapter(getContext().getApplicationContext(), importantTasks);


            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    int selectedTaskId = taskArrayHomeAdapter.getItem(i).getId();
                    Intent intent = new Intent(getActivity(), TaskActivity.class);
                    intent.putExtra("taskId", selectedTaskId);
                    startActivityForResult(intent, 0);
                }
            });

            gridView.setAdapter(taskArrayHomeAdapter);
        } else {
            linearLayout1.removeView(gridView);
            linearLayout1.removeView(textView1);
        }


        if(scheduledtasks) {

                scheduledTasks = TaskService.GetScheduledTasks(getActivity().getApplication());
                if(scheduledTasks == null || scheduledTasks.isEmpty()) {
                    textView4.setVisibility(View.INVISIBLE);
                    linearLayout1.removeView(gridView4);
                    linearLayout1.removeView(textView4);
                } else {
                    textView4.setVisibility(View.VISIBLE);
                }
                taskArrayHomeAdapter4 = new TaskArrayHomeAdapter(getContext().getApplicationContext(), scheduledTasks);
                gridView4.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        int selectedTaskId = taskArrayHomeAdapter4.getItem(i).getId();
                        Intent intent = new Intent(getActivity(), TaskActivity.class);
                        intent.putExtra("taskId", selectedTaskId);
                        startActivityForResult(intent, 0);
                    }
                });

                gridView4.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                        remove_scheduled_alert(taskArrayHomeAdapter4.getItem(i));
                        return true;
                    }
                });

                gridView4.setAdapter(taskArrayHomeAdapter4);


        } else {
            linearLayout1.removeView(gridView4);
            linearLayout1.removeView(textView4);
        }



        if(duetasks) {
            thisweeksTasks = TaskService.GetThisWeeksTasks(getActivity().getApplication());
            if(thisweeksTasks.isEmpty()) {
                textView2.setVisibility(View.INVISIBLE);
                linearLayout1.removeView(gridView2);
                linearLayout1.removeView(textView2);
            } else {
                textView2.setVisibility(View.VISIBLE);
            }


            taskArrayHomeAdapter2 = new TaskArrayHomeAdapter(getContext().getApplicationContext(), thisweeksTasks);

            gridView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    int selectedTaskId = taskArrayHomeAdapter2.getItem(i).getId();
                    Intent intent = new Intent(getActivity(), TaskActivity.class);
                    intent.putExtra("taskId", selectedTaskId);
                    startActivityForResult(intent, 0);
                }
            });

            gridView2.setAdapter(taskArrayHomeAdapter2);
        } else {
            textView2.setVisibility(View.INVISIBLE);
            linearLayout1.removeView(gridView2);
            linearLayout1.removeView(textView2);
        }



        if(completedtasks) {
            completedTasks = TaskService.GetCompletedTasks(getActivity().getApplication());
            if(completedTasks.isEmpty()) {
                textView3.setVisibility(View.INVISIBLE);
                linearLayout1.removeView(gridView3);
                linearLayout1.removeView(textView3);
            } else {
                textView3.setVisibility(View.VISIBLE);
            }


            taskArrayHomeAdapter3 = new TaskArrayHomeAdapter(getContext().getApplicationContext(), completedTasks);

            gridView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    int selectedTaskId = taskArrayHomeAdapter3.getItem(i).getId();
                    Intent intent = new Intent(getActivity(), TaskActivity.class);
                    intent.putExtra("taskId", selectedTaskId);
                    startActivityForResult(intent, 0);
                }
            });

            gridView3.setAdapter(taskArrayHomeAdapter3);
        } else {
            linearLayout1.removeView(gridView3);
            linearLayout1.removeView(textView3);
            textView3.setVisibility(View.INVISIBLE);
        }

        try {
            if((importantTasks != null && importantTasks.isEmpty() && thisweeksTasks!= null && thisweeksTasks.isEmpty() && scheduledTasks != null && scheduledTasks.isEmpty())
                    || (!importanttasks && (thisweeksTasks == null || thisweeksTasks.isEmpty()) && (scheduledTasks == null || scheduledTasks.isEmpty()))
                    || (importantTasks != null && importantTasks.isEmpty() && !duetasks && scheduledTasks != null && scheduledTasks.isEmpty())
                    || (importantTasks != null && importantTasks.isEmpty() && thisweeksTasks != null && thisweeksTasks.isEmpty() && !scheduledtasks)){
                linearLayout1.removeView(textView3);
                linearLayout1.removeView(gridView3);
                linearLayout1.addView(textView1);

                if(!completedTasks.isEmpty()) {
                    linearLayout1.addView(textView3);
                    linearLayout1.addView(gridView3);
                }
                textView1.setText("No important, due or scheduled tasks. \n Enjoy your light schedule! \n\n\n ");
            } else {
                textView1.setText("Your most important tasks:");
            }

        } catch(NullPointerException e) {
            e.printStackTrace();
        }

        return view;
    }



    // Handle action menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        // Hide sort options in HomeTab
        /*
        MenuItem sort1 = menu.findItem(R.id.sort1); sort1.setVisible(false);
        MenuItem sort2 = menu.findItem(R.id.sort2); sort2.setVisible(false);
        MenuItem sort3 = menu.findItem(R.id.sort3); sort3.setVisible(false);
        MenuItem sort4 = menu.findItem(R.id.sort4); sort4.setVisible(false);
        MenuItem check1 = menu.findItem(R.id.checkOther); check1.setVisible(false);
        MenuItem check2 = menu.findItem(R.id.checkWork); check2.setVisible(false);
        MenuItem check3 = menu.findItem(R.id.checkSchool); check3.setVisible(false);
        */

    } @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {

            case R.id.settings:
                // Go to Settings View
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            case R.id.joingroup:
                // Go to Join Group View
                startActivity(new Intent(getActivity(), JoinGroupActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void refreshContent() {

        // Handle grids

        if(scheduledtasks) {
            scheduledTasks = TaskService.GetScheduledTasks(getActivity().getApplicationContext());
            taskArrayHomeAdapter4.clear();
            taskArrayHomeAdapter4.addAll(scheduledTasks);
            taskArrayHomeAdapter4.notifyDataSetChanged();
            hideCompletedTasks(taskArrayHomeAdapter4, scheduledTasks);
        }


        if(importanttasks) {
            importantTasks = TaskService.GetImportantNotDeletedTasks(getActivity().getApplicationContext());
            taskArrayHomeAdapter.clear();
            taskArrayHomeAdapter.addAll(importantTasks);
            taskArrayHomeAdapter.notifyDataSetChanged();
            hideCompletedTasks(taskArrayHomeAdapter, importantTasks);
           // updateHiddenCategories(taskArrayHomeAdapter, importantTasks);
        }


        if(duetasks) {
            thisweeksTasks = TaskService.GetThisWeeksTasks(getActivity().getApplication());
            taskArrayHomeAdapter2.clear();
            taskArrayHomeAdapter2.addAll(thisweeksTasks);
            taskArrayHomeAdapter2.notifyDataSetChanged();
            hideCompletedTasks(taskArrayHomeAdapter2, thisweeksTasks);
           // updateHiddenCategories(taskArrayHomeAdapter2, thisweeksTasks);
        }


        if(completedtasks) {
            completedTasks = TaskService.GetCompletedTasks(getActivity().getApplication());
            taskArrayHomeAdapter3.clear();
            taskArrayHomeAdapter3.addAll(completedTasks);
            taskArrayHomeAdapter3.notifyDataSetChanged();
         //   updateHiddenCategories(taskArrayHomeAdapter3, completedTasks);
        }

    }

    public void onClick(View v) {
    }


    public void hideCompletedTasks(TaskArrayHomeAdapter taskArrayAdapter, ArrayList<Task> allTasks) {
        if (hidecompleted) {
            for (int i = 0; i < allTasks.size(); ++i) {
                if (taskArrayAdapter.getItem(i).getStatus() == Task.Status.DONE) {
                    taskArrayAdapter.hideItem(i);
                }
            }
        }
    }

    // Handle hidden categories
    public void updateHiddenCategories(TaskArrayHomeAdapter taskArrayAdapter, ArrayList<Task> hometabtasks) {

        // Clear all
        taskArrayAdapter.clearHiddenItems();

        if(!hiddenCategories.isEmpty()) {
            // Hide categories again
            if(hiddenCategories.contains("WORK")) {
                for(int i=0; i<hometabtasks.size(); ++i) {
                    if(taskArrayAdapter.getItem(i).getCategory() == Task.TaskCategory.WORK) {
                        taskArrayAdapter.hideItem(i);
                    }
                }
            } if(hiddenCategories.contains("SCHOOL")) {
                for(int i=0; i<hometabtasks.size(); ++i) {
                    if(taskArrayAdapter.getItem(i).getCategory() == Task.TaskCategory.SCHOOL) {
                        taskArrayAdapter.hideItem(i);
                    }
                }
            } if(hiddenCategories.contains("OTHER")) {
                for(int i=0; i<hometabtasks.size(); ++i) {
                    if(taskArrayAdapter.getItem(i).getCategory() == Task.TaskCategory.OTHER) {
                        taskArrayAdapter.hideItem(i);
                    }
                }
            }
            taskArrayAdapter.notifyDataSetChanged();
        }




    }

    public void remove_scheduled_alert(final Task task) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Remove scheduled item?")
                .setCancelable(false)
                .setPositiveButton("Remove", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int id) {
                        // Delete the task and go back to MainMenu
                        task.setScheduled(false);
                        task.SaveToFile(getContext());
                        refreshContent();
                        updateTabs = true;
                        Toast toast = Toast.makeText(getContext().getApplicationContext(), "Task removed from your schedule.", Toast.LENGTH_SHORT);
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

