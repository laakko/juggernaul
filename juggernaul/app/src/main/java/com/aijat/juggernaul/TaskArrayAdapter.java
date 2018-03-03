package com.aijat.juggernaul;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

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
        deadlineTextView.setText("DL: " + currentTask.getDeadlinePretty());

        TextView categoryTextView = listItem.findViewById(R.id.listTaskCategory);
        categoryTextView.setText(currentTask.getCategory().toString());

        return listItem;
    }
}