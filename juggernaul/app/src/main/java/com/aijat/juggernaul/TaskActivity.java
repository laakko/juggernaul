package com.aijat.juggernaul;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;

import static com.aijat.juggernaul.ListTab.test_title;

public class TaskActivity extends AppCompatActivity {

    private TextView txtTaskTitle;
    private View rect;
    private TextView txtTaskDeadline;
    private TextView txtTaskStatus;
    private FloatingActionButton sharebutton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        txtTaskTitle = (TextView) findViewById(R.id.txtTaskTitle);

        // Temp ----------------------
        rect = (View) findViewById(R.id.rectPriority);
        rect.setBackgroundColor(Color.rgb(137, 244, 66));
        txtTaskTitle.setText(test_title);

        txtTaskDeadline = (TextView) findViewById(R.id.txtTaskDeadline);
        txtTaskDeadline.setText("15 Days left");
        txtTaskStatus = (TextView) findViewById(R.id.txtTaskStatus);
        txtTaskStatus.setText("In progress");

        // -----------------------------

        sharebutton = (FloatingActionButton) findViewById(R.id.fab_share);
        sharebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(TaskActivity.this, ShareActivity.class));

            }
        });
    }

}
