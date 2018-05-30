package com.aijat.juggernaul;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.aijat.juggernaul.ListTab.allTasks;

/**
 * Implementation of App Widget functionality.
 */
public class NewAppWidget extends AppWidgetProvider {

    public static final String CLICK_ACTION = "openTaskAction";
    public static final String EXTRA_ITEM = "itemPosition";
    public List<Task> wTaskList = new ArrayList<>();

    @Override
    public void onDeleted(Context context, int[] appWidgetIds)
    {
        super.onDeleted(context, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
        super.onEnabled(context);
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
        super.onDisabled(context);
    }
    @Override
    public void onReceive(Context context, Intent intent)
    {


        AppWidgetManager mgr = AppWidgetManager.getInstance(context);
        if(intent.getAction().equals(CLICK_ACTION)){
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            int position = intent.getIntExtra(EXTRA_ITEM, 0);
           // Toast.makeText(context, "touched: " + position, Toast.LENGTH_SHORT).show();
            Log.i("touched:", Integer.toString(position));


            // TODO Fix click to open item.
            /*
            wTaskList.clear();
            wTaskList = TaskService.GetAllNotDeletedTasks(context);
            int selectedTaskId = wTaskList.get(position).getId();
            */
            Intent intentTask = new Intent (context, MainActivity.class);
            intentTask.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
            //intentTask.putExtra("taskId", selectedTaskId);
            context.startActivity (intentTask);

        }

        super.onReceive(context, intent);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {



        final int N = appWidgetIds.length;
        for(int i=0; i<N; ++i){
            /*
            RemoteViews remoteViews = updateWidgetListView(context, appWidgetIds[i]);
            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
            */

            Log.i("Updating widget..", "true");
            // Create the widget as a RemoteView (ListView)


            RemoteViews remoteViews = updateWidgetListView(context, appWidgetIds[i]);

            // Pending Intents
            Intent clickIntent = new Intent(context, NewAppWidget.class);
            clickIntent.setAction(NewAppWidget.CLICK_ACTION);
            clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            clickIntent.setData(Uri.parse(clickIntent.toUri(Intent.URI_INTENT_SCHEME)));
            PendingIntent clickpIndent = PendingIntent.getBroadcast(context,0,clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setPendingIntentTemplate(R.id.widget_list, clickpIndent);

            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds[i], R.id.widget_list);
            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);


        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);

    }

    private RemoteViews updateWidgetListView(Context context, int appWidgetId){

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_app_widget);
        Intent lintent = new Intent(context, WidgetService.class);
        lintent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        lintent.setData(Uri.parse(lintent.toUri(Intent.URI_INTENT_SCHEME)));
        remoteViews.setRemoteAdapter(R.id.widget_list, lintent);

        return remoteViews;
    }





}

