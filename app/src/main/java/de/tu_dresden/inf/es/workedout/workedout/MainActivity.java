package de.tu_dresden.inf.es.workedout.workedout;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

import de.tu_dresden.inf.es.workedout.workedout.models.WorkOutPlan;
import de.tu_dresden.inf.es.workedout.workedout.utils.Nfc;


public class MainActivity extends ActionBarActivity {

    NfcAdapter mNfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        // Fill list of recent workouts
        List<String> recentWorkouts = new ArrayList<>();
        List<WorkOutPlan> workOutPlanlIST=new Select().from(WorkOutPlan.class).where("saveflag=1").execute();
        for (WorkOutPlan w :workOutPlanlIST) recentWorkouts.add(w.name);
        if (recentWorkouts.isEmpty())
            recentWorkouts.add(getString(R.string.no_workouts));

        ListAdapter adapter = new ArrayAdapter<>(getApplicationContext(),
                R.layout.list_item_black_text, R.id.black_text, recentWorkouts);

        ListView recentWorkoutsView = (ListView) findViewById(R.id.listView);
        recentWorkoutsView.setAdapter(adapter);
        // list item selection handling
        ((ListView)findViewById(R.id.listView)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // get selected entry
                String entry = (String) parent.getItemAtPosition(position);

                // start exercise
                Intent intent = new Intent(MainActivity.this, WorkOutPlanActivity.class);
                intent.putExtra("workOutPlan", entry);
                startActivity(intent);
            }
        });
    }

    public void onStartNewWorkout(View view) {

        Intent intent = new Intent(this, WorkOutPlanActivity.class);
        intent.putExtra("newPlan",true);
        startActivity(intent);
    }


    public void onStatistics(View view) {
        Intent intent = new Intent(this, StatisticsActivity.class);
        startActivity(intent);
    }


    /*
        NFC handling
     */

    @Override
    protected void onResume() {
        super.onResume();
        if(mNfcAdapter != null && mNfcAdapter.isEnabled())
            Nfc.setupForegroundDispatch(this, mNfcAdapter);
    }

    @Override
    protected void onPause() {
        if(mNfcAdapter != null && mNfcAdapter.isEnabled())
            Nfc.stopForegroundDispatch(this, mNfcAdapter);
        super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        String device = new String(Nfc.getNdefRecord(intent).getPayload());
        Intent newIntent = new Intent(this, SelectExerciseActivity.class);
        newIntent.putExtra("device", device);
        startActivity(newIntent);
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
