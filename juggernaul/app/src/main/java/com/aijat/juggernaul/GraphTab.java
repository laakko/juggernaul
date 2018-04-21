package com.aijat.juggernaul;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.db.chart.model.LineSet;
import com.db.chart.model.Point;
import com.db.chart.view.ChartView;
import com.db.chart.view.LineChartView;
import com.google.android.gms.vision.text.Line;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.Series;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GraphTab extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_graph, container, false);

        Map taskTimeline = TaskService.GetTaskTimeline(getContext().getApplicationContext());

        ArrayList<String> labelsList = new ArrayList(taskTimeline.keySet());
        ArrayList<Float> valuesList = new ArrayList(taskTimeline.values());

        String[] labels = new String[labelsList.size()];
        labels = labelsList.toArray(labels);
        float[] values = new float[valuesList.size()];
        int i = 0;
        for (Float f : valuesList) {
            values[i++] = (f != null ? f : Float.NaN);
        }

        if (labels.length != 0) {
            LineSet dataset = new LineSet(labels, values);

            dataset = CustomizeDataset(dataset);

            LineChartView chart = view.findViewById(R.id.linechart);
            chart.addData(dataset);
            chart = CustomizeChart(chart);
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

    public LineChartView CustomizeChart(LineChartView chart) {
        chart.setAxisBorderValues(0.0f, 4.0f, 1.0f);
        // chart.setAxisColor((255 & 0xff) << 24 | (200 & 0xff) << 16 | (200 & 0xff) << 8 | (200 & 0xff));
        chart.setAxisColor(Color.WHITE);
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        chart.setGrid(4, 1, paint);
        chart.setFontSize(24);
        chart.setLabelsColor(Color.WHITE);
        return chart;
    }

    public LineSet CustomizeDataset(LineSet dataset) {
        dataset.setDotsColor(32000);
        dataset.setSmooth(true);
        dataset.setColor(60000);

        return dataset;
    }
}