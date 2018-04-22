    package com.aijat.juggernaul;

    import android.app.AlertDialog;
    import android.app.DatePickerDialog;
    import android.app.NotificationChannel;
    import android.app.NotificationManager;
    import android.app.PendingIntent;
    import android.content.Context;
    import android.content.DialogInterface;
    import android.content.Intent;
    import android.graphics.Color;
    import android.graphics.Point;
    import android.graphics.drawable.ColorDrawable;
    import android.os.Build;
    import android.os.Bundle;
    import android.support.design.widget.FloatingActionButton;
    import android.support.v4.app.Fragment;
    import android.support.v4.app.NotificationCompat;
    import android.support.v4.app.NotificationManagerCompat;
    import android.util.Log;
    import android.view.Display;
    import android.view.Gravity;
    import android.view.LayoutInflater;
    import android.view.Menu;
    import android.view.MenuInflater;
    import android.view.MenuItem;
    import android.view.View;
    import android.view.ViewGroup;
    import android.view.WindowManager;
    import android.view.animation.AnimationUtils;
    import android.widget.AdapterView;
    import android.widget.ArrayAdapter;
    import android.widget.Button;
    import android.widget.DatePicker;
    import android.widget.EditText;
    import android.widget.ListView;
    import android.widget.PopupWindow;
    import android.widget.Spinner;
    import android.widget.Toast;

    import java.util.ArrayList;
    import java.util.Calendar;
    import java.util.Comparator;
    import java.util.Date;
    import java.util.List;
    import java.util.Random;

    import static android.content.Context.LAYOUT_INFLATER_SERVICE;

    public class ListTab extends Fragment {

        private PopupWindow popup;
        private ListView listView;
        TaskArrayAdapter taskArrayAdapter;
        public static ArrayList<Task> allTasks;
        private Spinner categorySpinner, prioritySpinner;
        public boolean titleSort, dlSort, prioritySort, statusSort;
        public boolean titleAsc, dlAsc, priorityAsc, statusAsc;
        public Task.TaskCategory tempCategory;
        public Task.Priority tempPriority;
        public static List<String> hiddenCategories = new ArrayList<String>();

        @Override
        public void onResume() {
            super.onResume();
            refreshContent();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.fragment_list, container, false);
            setHasOptionsMenu(true);
            allTasks = TaskService.GetAllNotDeletedTasks(getActivity().getApplicationContext());
            taskArrayAdapter = new TaskArrayAdapter(view.getContext(), allTasks);
            taskArrayAdapter.addAll(allTasks);

            listView = view.findViewById(R.id.taskList);
            listView.setAdapter(taskArrayAdapter);
            listView.setLongClickable(true);
            taskArrayAdapter.notifyDataSetChanged();
            // Click on an item to open it into a new view for modification
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    // i is not a valid index if there are hidden tasks -> calculate new indexes
                    if(!hiddenCategories.isEmpty()) {
                        for (Integer hiddenId : TaskArrayAdapter.hiddenTasks) {
                            if (hiddenId <= i) {
                                i += 1;
                            } else {
                                break;
                            }
                        }
                    }
                    int selectedTaskId = taskArrayAdapter.getItem(i).getId();
                    Intent intent = new Intent(getActivity(), TaskActivity.class);
                    intent.putExtra("taskId", selectedTaskId);
                    startActivityForResult(intent, 0);
                }
            });

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                    // i is not a valid index if there are hidden tasks -> calculate new indexes
                    if(!hiddenCategories.isEmpty()) {
                        for(Integer hiddenId : TaskArrayAdapter.hiddenTasks) {
                            if(hiddenId <= i) {
                                i += 1;
                            } else {
                                break;
                            }
                        }
                    }
                    //longClickAlert("Choose Action", "Delete Task", "Pin As Notification", "Change Status", taskArrayAdapter.getItem(i));
                    longClickAlert2(taskArrayAdapter.getItem(i));
                    return true;
                }
            });
/*
            final SwipeRefreshLayout swipe = view.findViewById(R.id.swipeRefresh);
            swipe.setOnRefreshListener(
                    new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            refreshContentWithSwipe(swipe);
                        }
                    }
            );
*/
            // Add new task
            FloatingActionButton fab = view.findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
                    View layout = inflater.inflate(R.layout.add_task,
                            (ViewGroup) view.findViewById(R.id.tab1_main_layout));
                    layout.setAnimation(AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.fab_scale_up));

                    // New task
                    WindowManager wm = (WindowManager) getActivity().getSystemService(
                            Context.WINDOW_SERVICE);
                    Point size = new Point();
                    Display display1 = wm.getDefaultDisplay();
                    display1.getSize(size);
                    int deviceWidth = size.x;
                    int deviceHeight = size.y;

                    popup = new PopupWindow(layout, deviceWidth, (int) (deviceHeight*0.6), true);
                    popup.showAtLocation(layout, Gravity.TOP, 0, 100);
                    Button btnPop = layout.findViewById(R.id.btnCreateTask);
                    final EditText txtTitle = layout.findViewById(R.id.taskName);
                    final Calendar calendar = Calendar.getInstance();
                    final EditText txtDeadline = layout.findViewById(R.id.taskDL);
                    final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear,
                                              int dayOfMonth) {
                            calendar.set(Calendar.YEAR, year);
                            calendar.set(Calendar.MONTH, monthOfYear);
                            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            calendar.set(Calendar.HOUR_OF_DAY, 23);
                            calendar.set(Calendar.MINUTE, 59);
                            calendar.set(Calendar.SECOND, 59);
                            String prettyDeadline = calendar.get(Calendar.DAY_OF_MONTH) + "." + calendar.get(Calendar.MONTH) + "." + calendar.get(Calendar.YEAR);
                            txtDeadline.setText(prettyDeadline);
                        }
                    };
                    txtDeadline.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            new DatePickerDialog(getContext(), date, calendar
                                    .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                                    calendar.get(Calendar.DAY_OF_MONTH)).show();
                        }
                    });

                    // Priority
                    prioritySpinner = layout.findViewById(R.id.prioritySpinner);
                    prioritySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            tempPriority = Task.Priority.values()[i];

                        }   @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                            Log.i("Error", "You must select something from the category list");
                        }
                    });

                    prioritySpinner.setAdapter(new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, Task.Priority.values()));

                    // Category
                    categorySpinner = layout.findViewById(R.id.categorySpinner );
                    categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            tempCategory = Task.TaskCategory.values()[i];

                        }   @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {
                            Log.i("Error", "You must select something from the category list");
                        }
                    });

                    categorySpinner.setAdapter(new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, Task.TaskCategory.values()));

                    // Description
                    final EditText txtDescription = layout.findViewById(R.id.taskDescription);

                    btnPop.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            // Get all user-added parameters
                            String temp_title = txtTitle.getText().toString();
                            Date temp_deadline = calendar.getTime();
                            String temp_description = txtDescription.getText().toString();

                            Task newTask = new Task();
                            newTask.setTitle(temp_title);
                            newTask.setDescription(temp_description);
                            newTask.setDeadline(temp_deadline);
                            newTask.setCategory(tempCategory);
                            newTask.setPriority(tempPriority);
                            newTask.setScheduled(false);
                            TaskService.CreateNewTask(getActivity().getApplicationContext(), newTask);
                            refreshContent();

                            popup.dismiss();
                        }
                    });
                }
            });
            return view;
        }

        @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            refreshContent();
        }


        // Handle action menu
        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.menu, menu);
            super.onCreateOptionsMenu(menu, inflater);
        } @Override
        public boolean onOptionsItemSelected(MenuItem item) {

            switch(item.getItemId()) {

                case R.id.settings:
                    // Go to Settings View
                    startActivity(new Intent(getActivity(), SettingsActivity.class));
                    return true;
                case R.id.checkOther:
                    item.setChecked(!item.isChecked());
                    if(!item.isChecked()) {
                        // Loop "other"-category tasks -> hide items from listView
                        for(int i=0; i<allTasks.size(); ++i) {
                            if(taskArrayAdapter.getItem(i).getCategory() == Task.TaskCategory.OTHER) {
                                taskArrayAdapter.hideItem(i);
                                hiddenCategories.add("OTHER");
                            }
                        }
                    } else {
                        // Loop "other"-category tasks -> restore items to ListView
                        for(int i=0; i<allTasks.size(); ++i) {
                            if(taskArrayAdapter.getItem(i).getCategory() == Task.TaskCategory.OTHER) {
                                taskArrayAdapter.restoreItem(i);
                                hiddenCategories.remove("OTHER");
                            }
                        }
                    }
                    return true;
                case R.id.checkSchool:
                    item.setChecked(!item.isChecked());
                    if(!item.isChecked()) {
                        // Loop "school"-category tasks -> hide items from listView
                        for(int i=0; i<allTasks.size(); ++i) {
                            if(taskArrayAdapter.getItem(i).getCategory() == Task.TaskCategory.SCHOOL) {
                                taskArrayAdapter.hideItem(i);
                                hiddenCategories.add("SCHOOL");
                            }
                        }
                    } else {
                        // Loop "school"-category tasks -> restore items to ListView
                        for(int i=0; i<allTasks.size(); ++i) {
                            if(taskArrayAdapter.getItem(i).getCategory() == Task.TaskCategory.SCHOOL) {
                                taskArrayAdapter.restoreItem(i);
                                hiddenCategories.remove("SCHOOL");
                                try { // Epic workaround to prevent duplicates
                                    hiddenCategories.remove("SCHOOL");
                                } finally {
                                    // do nothing
                                }
                            }
                        }
                    }
                    return true;
                case R.id.checkWork:
                    item.setChecked(!item.isChecked());
                    if(!item.isChecked()) {
                        // Loop "work"-category tasks -> hide items from listView
                        for(int i=0; i<allTasks.size(); ++i) {
                            if(taskArrayAdapter.getItem(i).getCategory() == Task.TaskCategory.WORK) {
                                taskArrayAdapter.hideItem(i);
                                hiddenCategories.add("WORK");
                            }
                        }
                    } else {
                        // Loop "work"-category tasks -> restore items to ListView
                        for(int i=0; i<allTasks.size(); ++i) {
                            if(taskArrayAdapter.getItem(i).getCategory() == Task.TaskCategory.WORK) {
                                taskArrayAdapter.restoreItem(i);
                                hiddenCategories.remove("WORK");

                            }
                        }
                    }
                    return true;
                case R.id.joingroup:
                    // Go to Join Group View
                    startActivity(new Intent(getActivity(), JoinGroupActivity.class));
                    return true;
                case R.id.sort1:
                    // Sort listview by title

                    if(titleAsc) {
                        titleAsc = false;
                        sortByTitle();
                    } else {
                        titleAsc = true;
                        sortByTitle();
                    }
                    titleSort = true;
                    dlSort = false;
                    statusSort = false;
                    prioritySort = false;
                    return true;

                case R.id.sort2:
                    // Sort listview by DL
                    if(dlAsc) {
                        dlAsc = false;
                        sortByDeadline();
                    } else {
                        dlAsc = true;
                        sortByDeadline();
                    }
                    dlSort = true;
                    titleSort = false;
                    statusSort = false;
                    prioritySort = false;
                    return true;
                case R.id.sort3:
                    // Sort listview by Prio
                    if(priorityAsc) {
                        priorityAsc = false;
                        sortByPriority();
                    } else {
                        priorityAsc = true;
                        sortByPriority();
                    }
                    titleSort = false;
                    dlSort = false;
                    statusSort = false;
                    prioritySort = true;
                    return true;
                case R.id.sort4:
                    // Sort listview by Status
                    if(statusAsc) {
                        statusAsc = false;
                        sortByStatus();
                    } else {
                        statusAsc = true;
                        sortByStatus();
                    }
                    titleSort = false;
                    dlSort = false;
                    prioritySort = false;
                    statusSort = true;
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }

        public void sortByTitle() {
            taskArrayAdapter.sort(new Comparator<Task>() {
                @Override
                public int compare(Task task, Task t1) {
                    if(titleAsc){
                        return task.getTitle().toString().compareTo(t1.getTitle().toString());
                    } else {
                        return t1.getTitle().toString().compareTo(task.getTitle().toString());
                    }
                }
            });

            updateHiddenCategories();
            taskArrayAdapter.notifyDataSetChanged();
        }

        public void sortByDeadline() {
            taskArrayAdapter.sort(new Comparator<Task>() {
                @Override
                public int compare(Task task, Task t1) {
                    if(dlAsc) {
                        return task.getDeadline().compareTo(t1.getDeadline());
                    } else {
                        return t1.getDeadline().compareTo(task.getDeadline());
                    }
                }
            });

            updateHiddenCategories();
            taskArrayAdapter.notifyDataSetChanged();
        }

        public void sortByPriority() {
            taskArrayAdapter.sort(new Comparator<Task>() {
                @Override
                public int compare(Task task, Task t1) {
                    if(priorityAsc) {
                        return task.getPriority().compareTo(t1.getPriority());
                    } else {
                        return t1.getPriority().compareTo(task.getPriority());
                    }
                }
            });

            updateHiddenCategories();
            taskArrayAdapter.notifyDataSetChanged();

        }

        public void sortByStatus() {
            taskArrayAdapter.sort(new Comparator<Task>() {
                @Override
                public int compare(Task task, Task t1) {
                    if(statusAsc) {
                        return task.getStatus().compareTo(t1.getStatus());
                    } else {
                        return t1.getStatus().compareTo(task.getStatus());
                    }
                }
            });

            updateHiddenCategories();
            taskArrayAdapter.notifyDataSetChanged();
        }


        // Workaround for sorting breaking the hidden tasks
        public void updateHiddenCategories() {

            // Clear all
            taskArrayAdapter.clearHiddenItems();

            // Hide categories again
            if(hiddenCategories.contains("WORK")) {
                for(int i=0; i<allTasks.size(); ++i) {
                    if(taskArrayAdapter.getItem(i).getCategory() == Task.TaskCategory.WORK) {
                        taskArrayAdapter.hideItem(i);
                    }
                }
            } if(hiddenCategories.contains("SCHOOL")) {
                for(int i=0; i<allTasks.size(); ++i) {
                    if(taskArrayAdapter.getItem(i).getCategory() == Task.TaskCategory.SCHOOL) {
                        taskArrayAdapter.hideItem(i);
                    }
                }
            } if(hiddenCategories.contains("OTHER")) {
                for(int i=0; i<allTasks.size(); ++i) {
                    if(taskArrayAdapter.getItem(i).getCategory() == Task.TaskCategory.OTHER) {
                        taskArrayAdapter.hideItem(i);
                    }
                }
            }

        }

        // Popup for long click of a list item
        public void longClickAlert(String message, String positive_value, String neutral_value, String negative_value, final Task task) {
            final String[] actions = new String[] {
                    "Pin As Notification", "Change Status", "Delete Task"
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(message)
                    .setCancelable(true)
                    .setPositiveButton(positive_value, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int id) {
                            // Delete the task and go back to MainMenu
                            task.setDeleted(true);
                            if(task.isDeleted()) {
                                Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Task successfully deleted!", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                            task.SaveToFile(getActivity().getApplicationContext());
                            refreshContent();
                        }
                    })
                    .setNeutralButton(neutral_value, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Necessary for Android Oreo -> )
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                // Create the NotificationChannel, but only on API 26+ because
                                // the NotificationChannel class is new and not in the support library
                                CharSequence name = "Task details notification";
                                String description = "Juggernaul pinned task";
                                int importance = NotificationManager.IMPORTANCE_DEFAULT;
                                NotificationChannel channel = new NotificationChannel("juggernaulID", name, NotificationManager.IMPORTANCE_DEFAULT);
                                channel.setDescription(description);
                                // Register the channel with the system
                                NotificationManager notificationManager =
                                        (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                                notificationManager.createNotificationChannel(channel);
                            }

                            String task_content;
                            if(task.getDescription().startsWith("(Optional)")) {
                                task_content = "DL: " + task.getDeadlinePretty().toString();
                            } else {
                                task_content = "DL: " + task.getDeadlinePretty().toString() + "\n" + task.getDescription().toString();
                            }

                            int task_icon;
                            if(task.getCategory() == Task.TaskCategory.SCHOOL) {
                                task_icon =  R.drawable.school_icon_black;
                            } else if(task.getCategory() == Task.TaskCategory.WORK) {
                                task_icon =  R.drawable.work_icon_black;
                            } else {
                                task_icon =  R.drawable.other_icon_black;
                            }

                            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getContext(), "juggernaulID").setSmallIcon(task_icon)
                                    .setContentTitle(task.getTitle().toString())
                                    .setContentText(task_content)
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .bigText(task_content));

                            Intent intent = new Intent(getContext(), MainActivity.class);
                            PendingIntent pendint = PendingIntent.getActivity(getContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                            mBuilder.setContentIntent(pendint);


                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());
                            // notificationId is a unique int for each notification that you must define
                            Random rand = new Random(); int notificationId = rand.nextInt(10000);
                            notificationManager.notify(notificationId, mBuilder.build());
                        }
                    })
                    .setNegativeButton(negative_value, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if(task.getStatus() == Task.Status.TODO)
                                task.setStatus(Task.Status.INPROGRESS);
                            else if(task.getStatus() == Task.Status.INPROGRESS)
                                task.setStatus(Task.Status.DONE);
                            else
                                task.setStatus(Task.Status.TODO);
                            Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Task status changed!", Toast.LENGTH_SHORT);
                            toast.show();
                            task.SaveToFile(getActivity().getApplicationContext());
                            refreshContent();
                        }
                    });

            AlertDialog alert = builder.create();
            alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alert.show();
        }


        // Popup for long click of a list item
        public void longClickAlert2(final Task task) {
            final String[] actions = new String[] {
                    "Pin As Notification", "Change Status", "Add to Schedule", "Delete Task"
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Choose an action:")
                    .setCancelable(true).setIcon(R.mipmap.ic_launcher)
                    .setItems(actions,  new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    if(actions[i] == "Pin As Notification") {

                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                            // Create the NotificationChannel, but only on API 26+ because
                                            // the NotificationChannel class is new and not in the support library
                                            CharSequence name = "Task details notification";
                                            String description = "Juggernaul pinned task";
                                            int importance = NotificationManager.IMPORTANCE_DEFAULT;
                                            NotificationChannel channel = new NotificationChannel("juggernaulID", name, NotificationManager.IMPORTANCE_DEFAULT);
                                            channel.setDescription(description);
                                            // Register the channel with the system
                                            NotificationManager notificationManager =
                                                    (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                                            notificationManager.createNotificationChannel(channel);
                                        }

                                        String task_content;
                                        if(task.getDescription().startsWith("(Optional)")) {
                                            task_content = "DL: " + task.getDeadlinePretty().toString();
                                        } else {
                                            task_content = "DL: " + task.getDeadlinePretty().toString() + "\n" + task.getDescription().toString();
                                        }

                                        int task_icon;
                                        if(task.getCategory() == Task.TaskCategory.SCHOOL) {
                                            task_icon =  R.drawable.school_icon_grey;
                                        } else if(task.getCategory() == Task.TaskCategory.WORK) {
                                            task_icon =  R.drawable.work_icon_grey;
                                        } else {
                                            task_icon =  R.drawable.other_icon_grey;
                                        }

                                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getContext(), "juggernaulID").setSmallIcon(task_icon)
                                                .setContentTitle(task.getTitle().toString())
                                                .setContentText(task_content)
                                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                                .setStyle(new NotificationCompat.BigTextStyle()
                                                        .bigText(task_content));

                                        Intent intent = new Intent(getContext(), MainActivity.class);
                                        PendingIntent pendint = PendingIntent.getActivity(getContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                                        mBuilder.setContentIntent(pendint);


                                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getContext());
                                        // notificationId is a unique int for each notification that you must define
                                        Random rand = new Random(); int notificationId = rand.nextInt(10000);
                                        notificationManager.notify(notificationId, mBuilder.build());

                                    } else if(actions[i] == "Change Status") {

                                        if(task.getStatus() == Task.Status.TODO)
                                            task.setStatus(Task.Status.INPROGRESS);
                                        else if(task.getStatus() == Task.Status.INPROGRESS)
                                            task.setStatus(Task.Status.DONE);
                                        else
                                            task.setStatus(Task.Status.TODO);
                                        Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Task status changed!", Toast.LENGTH_SHORT);
                                        toast.show();
                                        task.SaveToFile(getActivity().getApplicationContext());
                                        refreshContent();

                                    } else if(actions[i] == "Delete Task") {

                                        task.setDeleted(true);
                                        if(task.isDeleted()) {
                                            Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Task successfully deleted!", Toast.LENGTH_SHORT);
                                            toast.show();
                                        }
                                        task.SaveToFile(getActivity().getApplicationContext());
                                        refreshContent();
                                    } else if(actions[i] == "Add to Schedule") {
                                        task.setScheduled(true);
                                        if(task.isScheduled()) {
                                            Toast toast = Toast.makeText(getActivity().getApplicationContext(), "Task added to your schedule!", Toast.LENGTH_SHORT);
                                            toast.show();
                                        }
                                        task.SaveToFile(getActivity().getApplicationContext());
                                        refreshContent();

                                    }

                                }
                            });

            AlertDialog alert = builder.create();
            alert.getWindow().setGravity(Gravity.CENTER_VERTICAL);
            //alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(230, 68, 88, 120))); // ColorPrimary
            //alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(60, 146, 205, 207))); // ColorAccent
            alert.show();
        }

        public void refreshContent() {
            allTasks = TaskService.GetAllNotDeletedTasks(getActivity().getApplicationContext());
            taskArrayAdapter.clear();
            taskArrayAdapter.addAll(allTasks);
            if(titleSort){
                sortByTitle();
            }
            if(dlSort) {
                sortByDeadline();
            }
            if(prioritySort) {
                sortByPriority();
            }
            if(statusSort) {
                sortByStatus();
            }
            taskArrayAdapter.notifyDataSetChanged();

        }
        /*
        private void refreshContentWithSwipe(SwipeRefreshLayout swipe) {
            allTasks = TaskService.GetAllNotDeletedTasks(getActivity().getApplicationContext());
            taskArrayAdapter.clear();
            taskArrayAdapter.addAll(allTasks);
            if(titleSort){
                sortByTitle();
            }
            if(dlSort) {
                sortByDeadline();
            }
            if(prioritySort) {
                sortByPriority();
            }
            if(statusSort) {
                sortByStatus();
            }
            taskArrayAdapter.notifyDataSetChanged();
            swipe.setRefreshing(false);
        }
        */
    }



