package com.aijat.juggernaul;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import static com.aijat.juggernaul.Task.Priority.HIGH;
import static com.aijat.juggernaul.Task.Priority.LOW;
import static com.aijat.juggernaul.Task.Priority.MEDIUM;

public class TaskArrayHomeAdapter extends ArrayAdapter<Task> {

    private Context mContext;
    private List<Task> taskList = new ArrayList<>();
    private SortedSet<Integer> hiddenTasks = new TreeSet<>();

    public TaskArrayHomeAdapter(@NonNull Context context, ArrayList<Task> list) {
        super(context, 0, list);
        mContext = context;
        taskList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.layout_task_home, parent, false);

        // Handle hidden tasks
        for (Integer hiddenId : hiddenTasks) {
            if (hiddenId <= position) {
                position += 1;
            } else {
                break;
            }
        }

        Task currentTask = taskList.get(position);

        TextView titleTextView = listItem.findViewById(R.id.homeTaskTitle);
        titleTextView.setText(currentTask.getTitle());
        TextView deadlineTextView = listItem.findViewById(R.id.homeTaskDeadline);

        deadlineTextView.setText("DL: " + currentTask.getDeadlinePretty() + ", " + currentTask.daysUntilDeadline() + " days left");

        SharedPreferences prefs = this.getContext().getSharedPreferences("com.aijat.juggernaul", Context.MODE_PRIVATE);
        if (prefs.getBoolean("dark", true)) {
            deadlineTextView.setTextColor(Color.LTGRAY);
        } else {
            deadlineTextView.setTextColor(Color.DKGRAY);
        }

        setPriorityColor(currentTask.getPriority(), titleTextView);

        return listItem;
    }


    // Handle hidden tasks
    public final void hideItem(int itemToHide) {
        hiddenTasks.add(itemToHide);
        Task currentTask = taskList.get(itemToHide);
        notifyDataSetChanged();

    }

    public final void restoreItem(int itemToRestore) {
        hiddenTasks.remove(itemToRestore);
        Task currentTask = taskList.get(itemToRestore);
        notifyDataSetChanged();
    }

    public final void clearHiddenItems() {
        hiddenTasks.clear();
    }

    @Override
    public int getCount() {
        return taskList.size() - hiddenTasks.size();
    }


    // Function to change task color based on priority
    public void setPriorityColor(Task.Priority priority, TextView title) {
        SharedPreferences prefs = this.getContext().getSharedPreferences("com.aijat.juggernaul", Context.MODE_PRIVATE);
        if (prefs.getBoolean("dark", true)) {
            if (priority == LOW) {
                title.setTextColor(Color.parseColor("#66bb6a"));
            } else if (priority == MEDIUM) {
                title.setTextColor(Color.parseColor("#ffa726"));
            } else if (priority == HIGH) {
                title.setTextColor(Color.parseColor("#bf360c"));
            }
        } else {
            if (priority == LOW) {
                title.setTextColor(Color.parseColor("#509753"));
            } else if (priority == MEDIUM) {
                title.setTextColor(Color.parseColor("#dc8c16"));
            } else if (priority == HIGH) {
                title.setTextColor(Color.parseColor("#bf360c"));

            }
        }
    }
}