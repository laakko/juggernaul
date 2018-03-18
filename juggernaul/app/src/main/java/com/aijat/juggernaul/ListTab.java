    package com.aijat.juggernaul;

    import android.app.Activity;
    import android.app.AlertDialog;
    import android.app.DatePickerDialog;
    import android.content.DialogInterface;
    import android.content.Intent;
    import android.os.Bundle;
    import android.support.design.widget.FloatingActionButton;
    import android.support.v4.app.Fragment;
    import android.support.v4.widget.SwipeRefreshLayout;
    import android.util.Log;
    import android.view.Gravity;
    import android.view.LayoutInflater;
    import android.view.Menu;
    import android.view.MenuInflater;
    import android.view.MenuItem;
    import android.view.View;
    import android.view.ViewGroup;
    import android.view.animation.AnimationUtils;
    import android.widget.AdapterView;
    import android.widget.ArrayAdapter;
    import android.widget.Button;
    import android.widget.DatePicker;
    import android.widget.EditText;
    import android.widget.ListView;
    import android.widget.PopupWindow;
    import android.widget.SeekBar;
    import android.widget.Spinner;
    import android.widget.Toast;

    import java.util.ArrayList;
    import java.util.Calendar;
    import java.util.Comparator;
    import java.util.Date;
    import java.util.List;

    import static android.content.Context.LAYOUT_INFLATER_SERVICE;

    public class ListTab extends Fragment {

        private PopupWindow popup;
        private ListView listView;
        TaskArrayAdapter taskArrayAdapter;
        public static ArrayList<Task> allTasks;
        private Spinner categorySpinner, prioritySpinner;
        public boolean titlesort, dlsort, priosort, statussort;
        public boolean titleasc, dlasc, prioasc, statusasc;
        public Task.TaskCategory temp_category;
        public Task.Priority temp_priority;

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
                    int selectedTaskId = taskArrayAdapter.getItem(i).getId();
                    //int selectedTaskId = taskArrayAdapter.allTasks.get(i).getId();
                    Intent intent = new Intent(getActivity(), TaskActivity.class);
                    intent.putExtra("taskId", selectedTaskId);
                    startActivityForResult(intent, 0);

                }
            });

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                    long_click_alert("Choose Action", "Delete Task", "Change Status", taskArrayAdapter.getItem(i));
                    return true;
                }
            });

            final SwipeRefreshLayout swipe = view.findViewById(R.id.swipeRefresh);
            swipe.setOnRefreshListener(
                    new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            refreshContentWithSwipe(swipe);
                        }
                    }
            );

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
                    popup = new PopupWindow(layout, 1080, 1150, true); // TODO: make it scalable, now its for full hd screens only
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
                            temp_priority = Task.Priority.values()[i];

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
                            temp_category = Task.TaskCategory.values()[i];

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
                            newTask.setCategory(temp_category);
                            newTask.setPriority(temp_priority);
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
                case R.id.joingroup:
                    // Go to Join Group View
                    startActivity(new Intent(getActivity(), JoinGroupActivity.class));
                    return true;
                case R.id.sort1:
                    // Sort listview by title

                    if(titleasc) {
                        titleasc = false;
                        sortByTitle();
                    } else {
                        titleasc = true;
                        sortByTitle();
                    }
                    titlesort = true;
                    dlsort = false;
                    statussort = false;
                    priosort = false;
                    return true;

                case R.id.sort2:
                    // Sort listview by DL
                    if(dlasc) {
                        dlasc = false;
                        sortByDeadline();
                    } else {
                        dlasc = true;
                        sortByDeadline();
                    }
                    dlsort = true;
                    titlesort = false;
                    statussort = false;
                    priosort = false;
                    return true;
                case R.id.sort3:
                    // Sort listview by Prio
                    if(prioasc) {
                        prioasc = false;
                        sortByPriority();
                    } else {
                        prioasc = true;
                        sortByPriority();
                    }
                    titlesort = false;
                    dlsort = false;
                    statussort = false;
                    priosort = true;
                    return true;
                case R.id.sort4:
                    // Sort listview by Status
                    if(statusasc) {
                        statusasc = false;
                        sortByStatus();
                    } else {
                        statusasc = true;
                        sortByStatus();
                    }
                    titlesort = false;
                    dlsort = false;
                    priosort = false;
                    statussort = true;
                    return true;

                default:
                    return super.onOptionsItemSelected(item);
            }

        }


        public void sortByTitle() {
            taskArrayAdapter.sort(new Comparator<Task>() {
                @Override
                public int compare(Task task, Task t1) {
                    if(titleasc){
                        return task.getTitle().toString().compareTo(t1.getTitle().toString());
                    } else {
                        return t1.getTitle().toString().compareTo(task.getTitle().toString());
                    }

                }
            });
            taskArrayAdapter.notifyDataSetChanged();
        }

        public void sortByDeadline() {
            taskArrayAdapter.sort(new Comparator<Task>() {
                @Override
                public int compare(Task task, Task t1) {
                    if(dlasc) {
                        return task.getDeadline().compareTo(t1.getDeadline());
                    } else {
                        return t1.getDeadline().compareTo(task.getDeadline());
                    }

                }
            });
            taskArrayAdapter.notifyDataSetChanged();
        }

        public void sortByPriority() {
            taskArrayAdapter.sort(new Comparator<Task>() {
                @Override
                public int compare(Task task, Task t1) {
                    if(prioasc) {
                        return task.getPriority().compareTo(t1.getPriority());
                    } else {
                        return t1.getPriority().compareTo(task.getPriority());
                    }

                }
            });
            taskArrayAdapter.notifyDataSetChanged();

        }

        public void sortByStatus() {
            taskArrayAdapter.sort(new Comparator<Task>() {
                @Override
                public int compare(Task task, Task t1) {
                    if(statusasc) {
                        return task.getStatus().compareTo(t1.getStatus());
                    } else {
                        return t1.getStatus().compareTo(task.getStatus());
                    }

                }
            });
            taskArrayAdapter.notifyDataSetChanged();

        }


        // Popup for long click of a list item
        public void long_click_alert(String message, String positive_value, String negative_value, final Task task) {
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
                            taskArrayAdapter.notifyDataSetChanged();
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
                            taskArrayAdapter.notifyDataSetChanged();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }

        public void refreshContent() {
            allTasks = TaskService.GetAllNotDeletedTasks(getActivity().getApplicationContext());
            taskArrayAdapter.clear();
            taskArrayAdapter.addAll(allTasks);
            // Stupid workaround but it works
            if(titlesort){
                sortByTitle();
            }
            if(dlsort) {
                sortByDeadline();
            }
            if(priosort) {
                sortByPriority();
            }
            if(statussort) {
                sortByStatus();
            }
            taskArrayAdapter.notifyDataSetChanged();

        }

        private void refreshContentWithSwipe(SwipeRefreshLayout swipe) {
            allTasks = TaskService.GetAllNotDeletedTasks(getActivity().getApplicationContext());
            taskArrayAdapter.clear();
            taskArrayAdapter.addAll(allTasks);
            // Stupid workaround but it works
            if(titlesort){
                sortByTitle();
            }
            if(dlsort) {
                sortByDeadline();
            }
            if(priosort) {
                sortByPriority();
            }
            if(statussort) {
                sortByStatus();
            }
            taskArrayAdapter.notifyDataSetChanged();
            swipe.setRefreshing(false);
        }


    }



