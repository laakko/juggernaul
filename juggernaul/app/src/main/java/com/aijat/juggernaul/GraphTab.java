package com.aijat.juggernaul;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;

import com.db.chart.animation.Animation;
import com.db.chart.model.LineSet;
import com.db.chart.view.LineChartView;

import java.util.ArrayList;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class GraphTab extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_graph, container, false);
        SharedPreferences sharedPrefs = this.getActivity().getSharedPreferences("com.aijat.juggernaul", MODE_PRIVATE);

        Map taskTimeline = TaskService.GetTaskTimeline(getContext().getApplicationContext());

        ArrayList<String> labelsList = new ArrayList(taskTimeline.keySet());
        ArrayList<Float> valuesList = new ArrayList(taskTimeline.values());

        labelsList = removeYearPart(labelsList);

        String[] labels = new String[labelsList.size()];
        labels = labelsList.toArray(labels);
        float[] values = new float[valuesList.size()];
        int i = 0;
        float maxValue = 0;
        for (Float f : valuesList) {
            values[i++] = (f != null ? f : Float.NaN);
            if (f > maxValue) {
                maxValue = f;
            }
        }

        if (labels.length != 0) {
            LineSet dataset = new LineSet(labels, values);

            dataset = CustomizeDataset(dataset, sharedPrefs);

            LineChartView chart = view.findViewById(R.id.linechart);
            chart.addData(dataset);
            chart = CustomizeChart(chart, sharedPrefs, maxValue);
            Animation anim = new Animation(1500).setInterpolator(new BounceInterpolator()).fromAlpha(0);
            chart.show(anim);
            chart.show();
        }

        return view;
    }

    // Handle action menu
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        // Hide sort options in GraphTab
        MenuItem sort1 = menu.findItem(R.id.sort1); sort1.setVisible(false);
        MenuItem sort2 = menu.findItem(R.id.sort2); sort2.setVisible(false);
        MenuItem sort3 = menu.findItem(R.id.sort3); sort3.setVisible(false);
        MenuItem sort4 = menu.findItem(R.id.sort4); sort4.setVisible(false);
        MenuItem check1 = menu.findItem(R.id.checkOther); check1.setVisible(false);
        MenuItem check2 = menu.findItem(R.id.checkWork); check2.setVisible(false);
        MenuItem check3 = menu.findItem(R.id.checkSchool); check3.setVisible(false);

    } @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {

            case R.id.settings:
                // Go to Settings View
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                return true;
            case R.id.joingroup:
                // Go to Join Group View
                startActivity(new Intent(getActivity(), JoinGroupActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public ArrayList<String> removeYearPart(ArrayList<String> list) {
        ArrayList<String> withoutYear = new ArrayList<>();
        for (String s : list) {
            String[] parts = s.split("\\.");
            String dayAndMonth = parts[0] + "." + parts[1];
            withoutYear.add(dayAndMonth);
        }
        return withoutYear;
    }

    public LineChartView CustomizeChart(LineChartView chart, SharedPreferences prefs, float maxValue) {
        Paint paint = new Paint();
        if (prefs.getBoolean("dark", true)) {
            chart.setAxisColor(Color.WHITE);
            paint.setColor(Color.WHITE);
            chart.setLabelsColor(Color.WHITE);
        } else {
            chart.setAxisColor(Color.BLACK);
            paint.setColor(Color.BLACK);
            chart.setLabelsColor(Color.BLACK);
        }
        chart.setAxisBorderValues(0.0f, maxValue, 1.0f);
        chart.setGrid(Math.round(maxValue), 1, paint);
        chart.setFontSize(36);

        return chart;
    }

    public LineSet CustomizeDataset(LineSet dataset, SharedPreferences prefs) {
        if (prefs.getBoolean("dark", true)) {
            dataset.setDotsColor(Color.parseColor("#ead75d"));
            dataset.setColor(Color.parseColor("#6a8fc9"));
        } else {
            dataset.setDotsColor(getResources().getColor(R.color.colorPrimary));
            dataset.setColor(Color.parseColor("#aabbcc"));
        }
        dataset.setSmooth(true);
        dataset.setShadow(10.0f, 0.0f, 10.0f, Color.BLACK);

        return dataset;
    }
}