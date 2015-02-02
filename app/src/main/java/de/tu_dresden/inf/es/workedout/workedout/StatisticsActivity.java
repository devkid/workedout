package de.tu_dresden.inf.es.workedout.workedout;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;


public class StatisticsActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ArrayList<Entry> weight = new ArrayList<>();
        weight.add(new Entry(20, 0));
        weight.add(new Entry(30, 1));
        weight.add(new Entry(50, 2));
        weight.add(new Entry(10, 3));

        final LineDataSet weightDataSet = new LineDataSet(weight, "Gewicht");
        weightDataSet.setColor(getResources().getColor(android.R.color.holo_blue_dark));
        weightDataSet.setCircleColor(getResources().getColor(android.R.color.holo_blue_light));
        weightDataSet.setLineWidth(3);
        weightDataSet.setCircleSize(8);

        ArrayList<Entry> repetitions = new ArrayList<>();
        repetitions.add(new Entry(41, 0));
        repetitions.add(new Entry(30, 1));
        repetitions.add(new Entry(7,  2));
        repetitions.add(new Entry(36, 3));

        final LineDataSet repetitionsDataSet = new LineDataSet(repetitions, "Wiederholungen");
        repetitionsDataSet.setColor(getResources().getColor(android.R.color.holo_green_dark));
        repetitionsDataSet.setCircleColor(getResources().getColor(android.R.color.holo_green_light));
        repetitionsDataSet.setLineWidth(3);
        repetitionsDataSet.setCircleSize(8);

        LineData data = new LineData(new String[] { "0", "1", "2", "3" },
                new ArrayList<LineDataSet>() {{ add(weightDataSet); add(repetitionsDataSet); }});
        LineChart chartView = (LineChart) findViewById(R.id.chart);
        chartView.setData(data);
        chartView.setDescription(getString(R.string.your_achievments));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_statistics, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_settings:
                return true;
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
