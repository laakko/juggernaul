package com.aijat.juggernaul;


import android.annotation.TargetApi;
import android.app.LauncherActivity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.RemoteViewsService.RemoteViewsFactory;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.aijat.juggernaul.Task.Priority.HIGH;
import static com.aijat.juggernaul.Task.Priority.LOW;
import static com.aijat.juggernaul.Task.Priority.MEDIUM;

public class WidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent){
        Log.i("remoteviewsfactory","here");
        return (new ListProvider(this.getApplicationContext(), intent));
    }
}

class ListProvider implements RemoteViewsFactory {

    private List<Task> widgetTaskList = new ArrayList<>();
    private Context context = null;
    private int appWidgetId;

    public ListProvider(Context context, Intent intent) {
        this.context = context;
        updateWidgetList();
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

        Log.i("AppWidgetId", String.valueOf(appWidgetId));

    }

    @TargetApi(Build.VERSION_CODES.N)
    private void updateWidgetList(){
      //  widgetTaskList.clear();
        Log.i("Updating widgetlist..", "true");
        try {
            widgetTaskList = TaskService.GetAllNotDeletedTasks(context);

            // Sort by DL. (Built-in sort works only if version > Android 7.0)
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                widgetTaskList.sort(new Comparator<Task>() {
                    @Override
                    public int compare(Task task, Task t1) {
                        return task.getDeadline().compareTo(t1.getDeadline());
                    }
                });
            }
        } catch(NullPointerException e) {
            e.printStackTrace();
        }


        /*
        try {
            for (Task task : ListTab.allTasks) {
                widgetTaskList.add(task);
            }
        } catch(NullPointerException e) {
            e.printStackTrace();
        }
        */
    }

    @Override
    public void onCreate(){
        updateWidgetList();
    }

    @Override
    public void onDataSetChanged(){
        updateWidgetList();
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    public void onDestroy(){
        widgetTaskList.clear();
    }


    public RemoteViews getLoadingView(){
        return null;
    }

    @Override
    public final boolean hasStableIds() {
        return true;
    }
    @Override
    public int getCount() {
        return widgetTaskList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RemoteViews getViewAt(int position){
        final RemoteViews remoteView = new RemoteViews(
                context.getPackageName(), R.layout.layout_task_home);

        updateWidgetList();
        Task currentTask = widgetTaskList.get(position);
        remoteView.setTextViewText(R.id.homeTaskTitle, currentTask.getTitle());
        remoteView.setTextViewText(R.id.homeTaskDeadline, "DL: " + currentTask.getDeadlinePretty() + ", " + currentTask.daysUntilDeadline() + " days left");


        if (currentTask.getPriority() == LOW) {
            remoteView.setTextColor(R.id.homeTaskTitle, Color.parseColor("#66bb6a"));
        } else if (currentTask.getPriority() == MEDIUM) {
            remoteView.setTextColor(R.id.homeTaskTitle, Color.parseColor("#ffa726"));
        } else if (currentTask.getPriority() == HIGH) {
           // remoteView.setTextColor(R.id.homeTaskTitle, Color.parseColor("#bf360c"));
            remoteView.setTextColor(R.id.homeTaskTitle, Color.parseColor("#750c0c"));
        }



        // Set intent for handling clicks
        Bundle extras = new Bundle();
        extras.putInt(NewAppWidget.EXTRA_ITEM, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        remoteView.setOnClickFillInIntent(R.id.homeTaskTitle, fillInIntent);



      return remoteView;
    }
}
