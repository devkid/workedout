package de.tu_dresden.inf.es.workedout.workedout;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Fill list of recent workouts
        List recentWorkouts = new ArrayList<String>();
        recentWorkouts.add(getString(R.string.no_workouts));
        ListAdapter adapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.list_item_black_text, R.id.black_text, recentWorkouts);

        ListView recentWorkoutsView = (ListView) findViewById(R.id.listView);
        recentWorkoutsView.setAdapter(adapter);
    }

    public void onStartNewWorkout(View view) {
        Intent intent = new Intent(this, SelectBodyPartActivity.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
