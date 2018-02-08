    package com.aijat.juggernaul;

    import android.content.Context;
    import android.content.Intent;
    import android.os.Bundle;
    import android.support.design.widget.FloatingActionButton;
    import android.support.v4.app.Fragment;
    import android.view.Gravity;
    import android.view.LayoutInflater;
    import android.view.MotionEvent;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.AdapterView;
    import android.widget.ArrayAdapter;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.ImageView;
    import android.widget.ListView;
    import android.widget.PopupWindow;
    import android.widget.SeekBar;
    import android.widget.TextView;

    import java.util.ArrayList;
    import java.util.Arrays;
    import java.util.List;

    import static android.content.Context.LAYOUT_INFLATER_SERVICE;

    public class ListTab extends Fragment {

        private PopupWindow popup;
        private ListView list;
        static String temp_title;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.fragment_list, container, false);



            // Initialize list view
            list = (ListView) view.findViewById(R.id.taskList);

            String[] items = new String[] {
                    "add tasks:"
            };
            final List<String> items_list = new ArrayList<String>(Arrays.asList(items));
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>
                    (view.getContext(), android.R.layout.simple_list_item_1, items_list);
            list.setAdapter(arrayAdapter);


            // Click on an item to open it into a new view for modification
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    temp_title = (String) adapterView.getItemAtPosition(i);
                    startActivity(new Intent(getActivity(), TaskActivity.class));
                }
            });


            // Add new items
            FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
                    View layout = inflater.inflate(R.layout.add_task,
                            (ViewGroup) view.findViewById(R.id.tab1_main_layout));


                    popup = new PopupWindow(layout, 1080, 900, true); // TODO: make it scalable, now its for full hd screens only
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

                    btnPop.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            items_list.add(txtPop.getText().toString());

                            // Create json -> add to json list

                            arrayAdapter.notifyDataSetChanged();

                        }
                    });


                }
            });


            return view;

        }




        // This is for creating a custom list item (e.g. our task-item)
        public class listview_adapter extends ArrayAdapter {
            private Context context;
            private LayoutInflater inflater;

            private String[] json_task;

            // Constructor, adapter takes JSON of a task as input
            public listview_adapter(Context context, String[] json_task) {
                super(context, R.layout.layout_task); // Use the custom layout_task xml
                this.context = context;
                this.json_task = json_task;

                inflater = LayoutInflater.from(context); // Inflate the task
            }


            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (null == convertView) {
                    convertView = inflater.inflate(R.layout.layout_task, parent, false);
                }

                // Set task parameters here

                // Set title
                TextView listTaskTitle = (TextView) convertView.findViewById(R.id.listTaskTitle);

                // Set deadline
                TextView listTaskDeadline = (TextView) convertView.findViewById(R.id.listTaskDeadline);

                // Set category
                TextView listTaskCategory = (TextView) convertView.findViewById(R.id.listTaskCategory);

                // Set status image
                ImageView listTaskStatus = (ImageView) convertView.findViewById(R.id.listTaskStatus);

                return convertView;

            }

        }



    }



