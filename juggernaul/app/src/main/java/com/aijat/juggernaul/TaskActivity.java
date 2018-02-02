package com.aijat.juggernaul;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import static com.aijat.juggernaul.ListTab.temp_title;

public class TaskActivity extends AppCompatActivity {

    private TextView txtTaskTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        txtTaskTitle = (TextView)findViewById(R.id.txtTaskTitle);

        txtTaskTitle.setText(temp_title);


    }
}
