package com.aijat.juggernaul;

import android.content.Context;
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

import static com.aijat.juggernaul.Task.Priority.HIGH;
import static com.aijat.juggernaul.Task.Priority.LOW;
import static com.aijat.juggernaul.Task.Priority.MEDIUM;
import static com.aijat.juggernaul.Task.Status.DONE;
import static com.aijat.juggernaul.Task.Status.INPROGRESS;

public class TaskArrayAdapter extends ArrayAdapter<Task> {

    private Context mContext;
    private List<Task> taskList = new ArrayList<>();

    public TaskArrayAdapter(@NonNull Context context, ArrayList<Task> list) {
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
        Date now = new Date();
        float diff = currentTask.getDeadline().getTime() - now.getTime();
        int diffInDays = (int) (diff / (1000*60*60*24));
        deadlineTextView.setText("DL: " + currentTask.getDeadlinePretty() + ", " + diffInDays + " days left");

        TextView categoryTextView = listItem.findViewById(R.id.listTaskCategory);
        categoryTextView.setText(currentTask.getCategory().toString());

        priority_color(currentTask.getPriority(), titleTextView);

        ImageView statusImageView = listItem.findViewById(R.id.listTaskStatus);
        // TODO find good icons for status
        // status_image(currentTask.getStatus(), statusImageView);

        return listItem;
    }


    // Function to change task color based on priority
    public void priority_color(Task.Priority priority, TextView title) {
        if(priority == LOW) {
            title.setTextColor(Color.parseColor("#66bb6a"));
        } else if(priority == MEDIUM) {
            title.setTextColor(Color.parseColor("#ffa726"));
        } else if(priority == HIGH) {
            title.setTextColor(Color.parseColor("#bf360c"));
        } else {
            title.setBackgroundColor(Color.GRAY);
        }

    }

    // Function to change task status image
    public void status_image(Task.Status status, ImageView img) {
        if(status == Task.Status.TODO) {
            // img.setImageBitmap()
        } else if(status == INPROGRESS) {
            // img.setImageBitmap();
        } else if(status == DONE) {
            // img.setImageBitmap();
        }

    }
}