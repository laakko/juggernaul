    package com.aijat.juggernaul;

    import android.app.DatePickerDialog;
    import android.content.Intent;
    import android.graphics.Color;
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
    import android.widget.AdapterView;
    import android.widget.Button;
    import android.widget.DatePicker;
    import android.widget.EditText;
    import android.widget.ListView;
    import android.widget.PopupWindow;
    import android.widget.SeekBar;

    import java.util.ArrayList;
    import java.util.Calendar;
    import java.util.Collections;
    import java.util.Comparator;
    import java.util.Date;

    import static android.content.Context.LAYOUT_INFLATER_SERVICE;

    public class ListTab extends Fragment {

        private PopupWindow popup;
        private ListView listView;
        TaskArrayAdapter taskArrayAdapter;
        public static ArrayList<Task> allTasks;
        public boolean titlesort, dlsort, priosort, statussort;
        public boolean titleasc, dlasc, prioasc, statusasc;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.fragment_list, container, false);
            setHasOptionsMenu(true);
            allTasks = TaskService.GetAllNotDeletedTasks(getActivity().getApplicationContext());

            taskArrayAdapter = new TaskArrayAdapter(view.getContext(), allTasks);
            taskArrayAdapter.addAll(allTasks);

            listView = view.findViewById(R.id.taskList);
            listView.setAdapter(taskArrayAdapter);

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
                    final SeekBar sbPriority = layout.findViewById(R.id.taskPriority);

                    // Category
                    final EditText txtCategory = layout.findViewById(R.id.taskCategory);

                    // Description
                    final EditText txtDescription = layout.findViewById(R.id.taskDescription);

                    btnPop.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            // Get all user-added parameters
                            String temp_title = txtTitle.getText().toString();
                            Date temp_deadline = calendar.getTime();
                            String temp_category = txtCategory.getText().toString();
                            String temp_description = txtDescription.getText().toString();
                            int temp_priority = sbPriority.getProgress();

                            Task newTask = new Task();
                            newTask.setTitle(temp_title);
                            newTask.setDescription(temp_description);
                            newTask.setDeadline(temp_deadline);

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
                    startActivity(new Intent(getActivity(), SettingsActivity.class));
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



