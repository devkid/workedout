package de.tu_dresden.inf.es.workedout.workedout;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

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
        recentWorkouts.add(getString(R.string.no_workouts));
        ListAdapter adapter = new ArrayAdapter<>(getApplicationContext(),
                R.layout.list_item_black_text, R.id.black_text, recentWorkouts);

        ListView recentWorkoutsView = (ListView) findViewById(R.id.listView);
        recentWorkoutsView.setAdapter(adapter);
    }

    public void onStartNewWorkout(View view) {
        Intent intent = new Intent(this, SelectBodyPartActivity.class);
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
