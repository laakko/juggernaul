    package com.aijat.juggernaul;

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
    import android.widget.ArrayAdapter;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.ListView;
    import android.widget.PopupWindow;
    import android.widget.SeekBar;

    import java.util.List;

    import static android.content.Context.LAYOUT_INFLATER_SERVICE;

    public class ListTab extends Fragment {

        private PopupWindow popup;
        private ListView list;
        static String test_title;

        public static List<Task> allTasks;

        /*
        @Override
        public void onResume() {
            super.onResume();

        }
        */

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.fragment_list, container, false);

            // Initialize list view(
            list = view.findViewById(R.id.taskList);

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(view.getContext(),
                    android.R.layout.simple_list_item_1);

            // Populate arrayAdapter. Stupid but works, for now.
            allTasks = TaskService.GetAllTasks(getActivity().getApplicationContext());
            for (Task task : allTasks) {
                arrayAdapter.add(task.getTitle());
            }
            list.setAdapter(arrayAdapter);

            // Click on an item to open it into a new view for modification
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    test_title = (String) adapterView.getItemAtPosition(i);
                    startActivity(new Intent(getActivity(), TaskActivity.class));
                }
            });

            final SwipeRefreshLayout swipe = view.findViewById(R.id.swipeRefresh);
            swipe.setOnRefreshListener(
                    new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh() {
                            refreshContentWithSwipe(arrayAdapter, swipe);
                        }
                    }
            );

            // Add new items
            FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
                    View layout = inflater.inflate(R.layout.add_task,
                            (ViewGroup) view.findViewById(R.id.tab1_main_layout));

                    popup = new PopupWindow(layout, 1080, 1150, true); // TODO: make it scalable, now its for full hd screens only
                    //popup.setAnimationStyle(R.anim.fadein);
                    popup.showAtLocation(layout, Gravity.TOP, 0, 100);

                    // Add items - action
                    Button btnPop = (Button)layout.findViewById(R.id.btnCreateTask);

                    // Title
                    final EditText txtPop = (EditText) layout.findViewById(R.id.taskName);

                    // Deadline
                    final EditText txtDeadline = (EditText) layout.findViewById(R.id.taskDL);

                    // Priority
                    final SeekBar sbPriority = (SeekBar) layout.findViewById(R.id.taskPriority);

                    // Category
                    final EditText txtCategory = (EditText) layout.findViewById(R.id.taskCategory);

                    // Description (optional)
                    final EditText txtDescription = (EditText) layout.findViewById(R.id.taskDescription);

                    btnPop.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            // Get all user-added parameters
                            String temp_title = txtPop.getText().toString();
                            String temp_deadline = txtDeadline.getText().toString();
                            String temp_category = txtCategory.getText().toString();
                            String temp_description = txtDescription.getText().toString();
                            int temp_priority = sbPriority.getProgress();

                            Task newTask = new Task();
                            newTask.setTitle(temp_title);
                            newTask.setDescription(temp_description);

                            TaskService.CreateNewTask(getActivity().getApplicationContext(), newTask);

                            refreshContent(arrayAdapter);

                            // Close popup window
                            popup.dismiss();
                        }
                    });
                }
            });
            return view;
        }

        private void refreshContent(ArrayAdapter arrayAdapter) {
            allTasks = TaskService.GetAllTasks(getActivity().getApplicationContext());
            arrayAdapter.clear();
            for (Task task : allTasks) {
                arrayAdapter.add(task.getTitle());
            }
            arrayAdapter.notifyDataSetChanged();
        }

        private void refreshContentWithSwipe(ArrayAdapter arrayAdapter, SwipeRefreshLayout swipe) {
            allTasks = TaskService.GetAllTasks(getActivity().getApplicationContext());
            arrayAdapter.clear();
            for (Task task : allTasks) {
                arrayAdapter.add(task.getTitle());
            }
            arrayAdapter.notifyDataSetChanged();
            swipe.setRefreshing(false);
        }

        // Function to change task color based on priority
        public void priority_color(int priority_value, View convertView) {
            // Temp, change to actual colors: green -> red
            if(priority_value < 10) {
                convertView.setBackgroundColor(Color.CYAN);
            } else if(priority_value < 20) {
                convertView.setBackgroundColor(Color.GRAY);
            } else if(priority_value < 30) {
                convertView.setBackgroundColor(Color.GREEN);
            } else {
                convertView.setBackgroundColor(Color.RED);
            }

        }



    }



