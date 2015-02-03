package de.tu_dresden.inf.es.workedout.workedout;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

import de.tu_dresden.inf.es.workedout.workedout.models.Exercise;
import de.tu_dresden.inf.es.workedout.workedout.models.WorkOutPlan;
import de.tu_dresden.inf.es.workedout.workedout.utils.Nfc;


public class MainActivity extends ActionBarActivity {

    NfcAdapter mNfcAdapter;
    List<String> mRecentWorkouts = new ArrayList<>();
    private ListAdapter mRecentWorkoutsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        mRecentWorkoutsAdapter = new ArrayAdapter<>(getApplicationContext(),
                R.layout.list_item_black_text, R.id.black_text, mRecentWorkouts);

        ListView recentWorkoutsView = (ListView) findViewById(R.id.listView);
        recentWorkoutsView.setAdapter(mRecentWorkoutsAdapter);

        // list item selection handling
        ((ListView)findViewById(R.id.listView)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // get selected entry
                String entry = (String) parent.getItemAtPosition(position);

                if (!entry.equals("No workouts yet!")) {
                    // start workout plan
                    Intent intent = new Intent(MainActivity.this, WorkOutPlanActivity.class);
                    intent.putExtra("workOutPlan", entry);
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mNfcAdapter != null && mNfcAdapter.isEnabled())
            Nfc.setupForegroundDispatch(this, mNfcAdapter);

        // Fill list of recent workouts
        mRecentWorkouts.clear();
        List<WorkOutPlan> workOutPlans = new Select().from(WorkOutPlan.class).execute();
        for (WorkOutPlan w: workOutPlans)
            mRecentWorkouts.add(w.name);
        if (mRecentWorkouts.isEmpty())
            mRecentWorkouts.add(getString(R.string.no_workouts));
        ((BaseAdapter) mRecentWorkoutsAdapter).notifyDataSetChanged();
    }

    @Override
    protected void onPause() {
        if(mNfcAdapter != null && mNfcAdapter.isEnabled())
            Nfc.stopForegroundDispatch(this, mNfcAdapter);
        super.onPause();
    }

    public void onStartNewWorkout(View view) {
        final EditText name = new EditText(this);
        new AlertDialog.Builder(this)
                .setTitle("Neuer Workout Plan")
                .setMessage("Gib dem neuen Workout Plan einen Namen:")
                .setView(name)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Intent intent = new Intent(MainActivity.this, WorkOutPlanActivity.class);
                        intent.putExtra("name", name.getText().toString());
                        startActivity(intent);
                    }
                }).setNegativeButton("Abbrechen", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                }).show();
    }


    public void onStatistics(View view) {
        Intent intent = new Intent(this, SelectExerciseActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK) {
            Exercise ex = Exercise.load(Exercise.class, data.getLongExtra("exercise", 0));
            Intent intent = new Intent(this, StatisticsActivity.class);
            startActivity(intent);
        }
    }


    /*
        NFC handling
     */

    @Override
    protected void onNewIntent(Intent intent) {
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_settings:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
