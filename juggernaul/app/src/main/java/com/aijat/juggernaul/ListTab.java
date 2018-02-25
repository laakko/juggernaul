    package com.aijat.juggernaul;

    import android.content.Context;
    import android.content.Intent;
    import android.graphics.Color;
    import android.os.Bundle;
    import android.support.design.widget.FloatingActionButton;
    import android.support.v4.app.Fragment;
    import android.util.Log;
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

    import org.json.JSONArray;
    import org.json.JSONException;
    import org.json.JSONObject;

    import java.util.ArrayList;
    import java.util.Arrays;
    import java.util.Date;
    import java.util.List;

    import static android.content.Context.LAYOUT_INFLATER_SERVICE;

    public class ListTab extends Fragment {

        private PopupWindow popup;
        private ListView list;
        static String test_title;


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View view = inflater.inflate(R.layout.fragment_list, container, false);



            // Initialize list view
            list = (ListView) view.findViewById(R.id.taskList);



            String[] items = new String[] {
                    "add tasks:"
            };


            // final List<String> items_list = new ArrayList<String>(Arrays.asList(items));

            // TODO: parse the big JSON-file here and add to list adapter

            // Get all tasks

            ArrayList<String> tasks = new ArrayList<String>();
            JSONArray jArray  = TaskService.ReadTasks(getActivity().getApplicationContext());

            for(int i=0; i< jArray.length() ; i++) {

                try {
                    String name = jArray.getJSONObject(i).getString("title");
                    tasks.add(name);
                } catch (JSONException e) {

                }
            }

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(view.getContext(),
                    android.R.layout.simple_list_item_1, tasks);
            list.setAdapter(arrayAdapter);


           //  list.setAdapter(new listview_adapter(getActivity().getApplicationContext(), jArray));



            // Click on an item to open it into a new view for modification
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    test_title = (String) adapterView.getItemAtPosition(i);
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


                          //  items_list.add(txtPop.getText().toString());


                            //arrayAdapter.notifyDataSetChanged();

                            // Close popup window
                            popup.dismiss();
                        }
                    });


                }
            });


            return view;

        }



        //-------------- (NOT DONE YET) ---------------------------------------------
        // This is for creating a custom list item (e.g. our task-item)
        public class listview_adapter extends ArrayAdapter {
            private Context context;
            private LayoutInflater inflater;

            private JSONArray jArray;


            // Constructor
            public listview_adapter(Context context, JSONArray jArray) {
                super(context, R.layout.layout_task); // Use the custom layout_task xml
                this.context = context;
                this.jArray = jArray;


                inflater = LayoutInflater.from(context); // Inflate the task
            }


            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (null == convertView) {
                    convertView = inflater.inflate(R.layout.layout_task, parent, false);
                }

                // Set task parameters here

                final String titles_list[] = new String[jArray.length()];

                for(int i=0; i< jArray.length() ; i++) {

                    try {
                        titles_list[i] = jArray.getJSONObject(i).getString("title");

                        // Set title
                        TextView listTaskTitle = (TextView) convertView.findViewById(R.id.listTaskTitle);
                        listTaskTitle.setText(titles_list[i]);

                    } catch (JSONException e) {

                    }
                }





                /*
                // Set deadline
                TextView listTaskDeadline = (TextView) convertView.findViewById(R.id.listTaskDeadline);
                listTaskDeadline.setText(deadlines[position]);

                // Set category
                TextView listTaskCategory = (TextView) convertView.findViewById(R.id.listTaskCategory);
                listTaskCategory.setText(categories[position]);


                // TODO: set status image
               //  ImageView listTaskStatus = (ImageView) convertView.findViewById(R.id.listTaskStatus);

                // Set priority color
                priority_color(priorities[position], convertView);
                */

                return convertView;

            }

        }

        /*
        public parse_JSON() {

        }
        */



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



