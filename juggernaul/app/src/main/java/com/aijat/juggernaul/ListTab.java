    package com.aijat.juggernaul;

    import android.os.Bundle;
    import android.support.design.widget.FloatingActionButton;
    import android.support.v4.app.Fragment;
    import android.view.Gravity;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.PopupWindow;

    import static android.content.Context.LAYOUT_INFLATER_SERVICE;

    public class ListTab extends Fragment {

        private PopupWindow popup;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            //return inflater.inflate(R.layout.fragment2, container, false);

            View view = inflater.inflate(R.layout.fragment_list, container, false);

            FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
                    View layout = inflater.inflate(R.layout.add_task,
                            (ViewGroup) view.findViewById(R.id.tab1_main_layout));
                    popup = new PopupWindow(layout, 700, 800, true);
                    //popup.setAnimationStyle(R.anim.fadein);
                    popup.showAtLocation(layout, Gravity.TOP, 0, 100);


                }
            });
            return view;

        }
    }
