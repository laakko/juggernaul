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
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.aijat.juggernaul.Task.Priority.HIGH;
import static com.aijat.juggernaul.Task.Priority.LOW;
import static com.aijat.juggernaul.Task.Priority.MEDIUM;
import static com.aijat.juggernaul.Task.Status.DONE;
import static com.aijat.juggernaul.Task.Status.INPROGRESS;

public class TaskArrayHomeAdapter extends ArrayAdapter<Task> {

    private Context mContext;
    private List<Task> taskList = new ArrayList<>();

    public TaskArrayHomeAdapter(@NonNull Context context, ArrayList<Task> list) {
        super(context, 0 , list);
        mContext = context;
        taskList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.layout_task,parent,false);

        Task currentTask = taskList.get(position);

        TextView titleTextView = listItem.findViewById(R.id.listTaskTitle);
        titleTextView.setText(currentTask.getTitle());

        TextView deadlineTextView = listItem.findViewById(R.id.listTaskDeadline);

        deadlineTextView.setText("DL: " + currentTask.getDeadlinePretty() + ", " + currentTask.daysUntilDeadline() + " days left");
        deadlineTextView.setTextColor(Color.DKGRAY);

        TextView categoryTextView = listItem.findViewById(R.id.listTaskCategory);
        categoryTextView.setText(currentTask.getCategory().toString());

        priority_color(currentTask.getPriority(), titleTextView);

        return listItem;
    }

    // Function to change task color based on priority
    public void priority_color(Task.Priority priority, TextView title) {
        if (priority == LOW) {
            title.setTextColor(Color.parseColor("#66bb6a"));
        } else if (priority == MEDIUM) {
            title.setTextColor(Color.parseColor("#ffa726"));
        } else if (priority == HIGH) {
            title.setTextColor(Color.parseColor("#bf360c"));
        } else {
            title.setBackgroundColor(Color.GRAY);
        }
    }
}