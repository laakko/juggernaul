    package com.aijat.juggernaul;

    import android.os.Bundle;
    import android.support.design.widget.FloatingActionButton;
    import android.support.v4.app.Fragment;
    import android.view.Gravity;
    import android.view.LayoutInflater;
    import android.view.MotionEvent;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ArrayAdapter;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.ListView;
    import android.widget.PopupWindow;

    import java.util.ArrayList;
    import java.util.Arrays;
    import java.util.List;

    import static android.content.Context.LAYOUT_INFLATER_SERVICE;

    public class ListTab extends Fragment {

        private PopupWindow popup;
        private ListView list;


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


            FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
                    View layout = inflater.inflate(R.layout.add_task,
                            (ViewGroup) view.findViewById(R.id.tab1_main_layout));
                    popup = new PopupWindow(layout, 1080, 900, true);
                    //popup.setAnimationStyle(R.anim.fadein);
                    popup.showAtLocation(layout, Gravity.TOP, 0, 100);


                    // Add items - action
                    Button btnPop = (Button)layout.findViewById(R.id.btnCreateTask);
                    final EditText txtPop = (EditText) layout.findViewById(R.id.taskName);
                    btnPop.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            items_list.add(txtPop.getText().toString());
                            arrayAdapter.notifyDataSetChanged();

                        }
                    });


                }
            });
            return view;

        }
    }
