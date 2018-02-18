package com.aijat.juggernaul;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

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
        Task testiTask = new Task("Tämä on title", "Tämä on description", Task.Priority.LOW, date, Task.TaskCategory.OTHER, Task.Status.TODO, "Ilmari", "Ryhmärämä");
        // Log.i("json", testiTask.JSONify().toString());


        Gson gson = new Gson();

        boolean deleted = FileService.deleteStorageFile(getActivity().getApplicationContext());
        Log.i("deleted", Boolean.toString(deleted));

        boolean created = FileService.createStorageFile(getActivity().getApplicationContext(), testiTask.JSONify().toString());
        Log.i("created", Boolean.toString(created));
        JSONObject jepajson = FileService.loadStorageJSON(getActivity().getApplicationContext());

        Log.i("ekajson", gson.toJson(testiTask).toString());

        Task testiTask2 = new Task("123", "aaa", Task.Priority.LOW, date, Task.TaskCategory.OTHER, Task.Status.TODO, "Ilmari", "Ryhmärämä");
        FileService.saveStorageJSON(getActivity().getApplicationContext(), testiTask2.JSONify());
        JSONObject jepajson2 = FileService.loadStorageJSON(getActivity().getApplicationContext());

        Log.i("overridden", gson.toJson(testiTask2).toString());

        Task testiTask3 = new Task("456", "abc", Task.Priority.LOW, date, Task.TaskCategory.OTHER, Task.Status.TODO, "Ilmari", "Ryhmärämä");
        FileService.appendToStorageFile(getActivity().getApplicationContext(), testiTask3.JSONify().toString());
        JSONObject jepajson3 = FileService.loadStorageJSON(getActivity().getApplicationContext());

        Log.i("overriddenAndAppended", gson.toJson(testiTask3).toString());
    }

}

