    package com.aijat.juggernaul;

    import android.app.DatePickerDialog;
    import android.content.Intent;
    import android.graphics.Color;
    import android.os.Bundle;
    import android.support.design.widget.FloatingActionButton;
    import android.support.v4.app.Fragment;
    import android.support.v4.widget.SwipeRefreshLayout;
    import android.view.Gravity;
    import android.view.LayoutInflater;
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
    import java.util.Date;

    import static android.content.Context.LAYOUT_INFLATER_SERVICE;

    public class ListTab extends Fragment {

        private PopupWindow popup;
        private ListView listView;
        TaskArrayAdapter taskArrayAdapter;
        public static ArrayList<Task> allTasks;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.fragment_list, container, false);

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
                    int selectedTaskId = allTasks.get(i).getId();
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

        public void refreshContent() {
            allTasks = TaskService.GetAllNotDeletedTasks(getActivity().getApplicationContext());
            taskArrayAdapter.clear();
            taskArrayAdapter.addAll(allTasks);
            taskArrayAdapter.notifyDataSetChanged();
        }

        private void refreshContentWithSwipe(SwipeRefreshLayout swipe) {
            allTasks = TaskService.GetAllNotDeletedTasks(getActivity().getApplicationContext());
            taskArrayAdapter.clear();
            taskArrayAdapter.addAll(allTasks);
            taskArrayAdapter.notifyDataSetChanged();
            swipe.setRefreshing(false);
        }


    }



