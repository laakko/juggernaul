package com.aijat.juggernaul;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import java.util.Date;

// This is the home view!

/*

TODO

 */


public class HomeTab extends Fragment implements View.OnClickListener {

    private CheckBox chkBx;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        chkBx = view.findViewById(R.id.checkBoxi);
        chkBx.setOnClickListener(this);
        return view;
    }

    // This checkbox is for the sake of verifying that the Task class works as intended.
    @Override
    public void onClick(View v) {
        chkBx.setText("Jees");
        Date date = new Date();
        Task testiTask = new Task("Tämä on title", "Tämä on description", Task.Priority.LOW, date, Task.Status.TODO, "Ilmari", "Ryhmärämä");
        Log.i("json", testiTask.JSONify().toString());
    }

}

