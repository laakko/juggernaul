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
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import static com.aijat.juggernaul.Task.Priority.HIGH;
import static com.aijat.juggernaul.Task.Priority.LOW;
import static com.aijat.juggernaul.Task.Priority.MEDIUM;
import static com.aijat.juggernaul.Task.Status.DONE;
import static com.aijat.juggernaul.Task.Status.INPROGRESS;

public class TaskArrayAdapter extends ArrayAdapter<Task> {

    private Context mContext;
    private List<Task> taskList = new ArrayList<>();
    public final static SortedSet<Integer> hiddenTasks = new TreeSet<>();

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

        // Handle hidden tasks
        for(Integer hiddenId : hiddenTasks) {
            if(hiddenId <= position) {
                position += 1;
            } else {
                break;
            }
        }


        Task currentTask = taskList.get(position);

        TextView titleTextView = listItem.findViewById(R.id.listTaskTitle);
        titleTextView.setText(currentTask.getTitle());

        TextView deadlineTextView = listItem.findViewById(R.id.listTaskDeadline);

        deadlineTextView.setText("DL: " + currentTask.getDeadlinePretty() + ", " + currentTask.daysUntilDeadline() + " days left");

        TextView categoryTextView = listItem.findViewById(R.id.listTaskCategory);
        categoryTextView.setText(currentTask.getCategory().toString());

        ImageView categoryImageView = listItem.findViewById(R.id.listTaskCategoryImage);
        setCategoryImage(currentTask.getCategory(), categoryImageView);

        setPriorityColor(currentTask.getPriority(), titleTextView);

        ImageView statusImageView = listItem.findViewById(R.id.listTaskStatus);
        setStatusImage(currentTask.getStatus(), statusImageView);

        return listItem;
    }



    // Handle hidden tasks
    public final void hideItem(int itemToHide) {
        hiddenTasks.add(itemToHide);
        notifyDataSetChanged();

    }

    public final void restoreItem(int itemToRestore) {
        hiddenTasks.remove(itemToRestore);
        notifyDataSetChanged();
    }

    public final void clearHiddenItems() {
        hiddenTasks.clear();
        notifyDataSetChanged();
    }

    @Override
    public final int getCount() {
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
        }else {
            if(priority == LOW) {
                title.setTextColor(Color.parseColor("#509753"));
            } else if(priority == MEDIUM) {
                title.setTextColor(Color.parseColor("#dc8c16"));
            } else if(priority == HIGH) {
                title.setTextColor(Color.parseColor("#bf360c"));

            }
        }
    }

    // Function to change task status image
    public void setStatusImage(Task.Status status, ImageView img) {
        if(status == Task.Status.TODO) {
            img.setImageResource(R.drawable.open);
        } else if(status == INPROGRESS) {
            img.setImageResource(R.drawable.inprog2);
        } else if(status == DONE) {
            img.setImageResource(R.drawable.done);
        }
    }

    public void setCategoryImage(Task.TaskCategory category, ImageView img) {
        SharedPreferences prefs = this.getContext().getSharedPreferences("com.aijat.juggernaul", Context.MODE_PRIVATE);
        if (prefs.getBoolean("dark", true)) {
            if (category == Task.TaskCategory.OTHER) {
                img.setImageResource(R.drawable.other_icon_grey);
            } else if (category == Task.TaskCategory.SCHOOL) {
                img.setImageResource(R.drawable.school_icon_grey);
            } else if (category == Task.TaskCategory.WORK) {
                img.setImageResource(R.drawable.work_icon_grey);
            }
        } else {
            if (category == Task.TaskCategory.OTHER) {
                img.setImageResource(R.drawable.other_icon_black);
            } else if (category == Task.TaskCategory.SCHOOL) {
                img.setImageResource(R.drawable.school_icon_black);
            } else if (category == Task.TaskCategory.WORK) {
                img.setImageResource(R.drawable.work_icon_black);
            }
        }
    }
}