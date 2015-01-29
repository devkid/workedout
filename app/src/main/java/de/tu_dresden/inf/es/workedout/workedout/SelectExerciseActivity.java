package de.tu_dresden.inf.es.workedout.workedout;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import de.tu_dresden.inf.es.workedout.workedout.utils.Nfc;


public class SelectExerciseActivity extends ActionBarActivity {

    private NfcAdapter mNfcAdapter;
    private List<String> mExercises;
    private ListAdapter mExercisesListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_exercise);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // list and adapter for recommended exercises
        mExercises = new ArrayList<>();
        mExercisesListAdapter = new ArrayAdapter<>(getApplicationContext(),
                R.layout.list_item_black_text, R.id.black_text, mExercises);
        ListView exercisesView = (ListView) findViewById(R.id.exerciseListView);
        exercisesView.setAdapter(mExercisesListAdapter);

        Intent intent = getIntent();

        // intent came from NFC event
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(mNfcAdapter != null && mNfcAdapter.isEnabled() &&
           Nfc.isNfcIntent(intent)) {
            handleNfcIntent(intent);
        }
        else {
            if(intent.hasExtra("bodyPartId")) {
                getExercisesFromBodyPart(intent.getIntExtra("bodyPartId", 0));
            } else if (intent.hasExtra("device")) {
                getExercisesFromDevice(intent.getStringExtra("device"));
            }
        }

        // list item selection handling
        exercisesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // get selected entry
                String entry = (String) parent.getItemAtPosition(position);

                // start exercise
                Intent intent = new Intent(SelectExerciseActivity.this, ExerciseExecutionActivity.class);
                intent.putExtra("exercise", entry);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_select_exercise, menu);
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

    /*
        List filling
     */

    public void getExercisesFromDevice(String device) {
        mExercises.clear();
        if(device.equals("Bench press")) {
            mExercises.add("Bankdrücken");
            mExercises.add("Schräges Bankdrücken");
        }
        ((BaseAdapter) mExercisesListAdapter).notifyDataSetChanged();
    }

    public void getExercisesFromBodyPart(int bodyPartId) {
        mExercises.clear();
        if(bodyPartId == 1) {
            mExercises.add("Shrugs");
            mExercises.add("Einarmiges Kurzhantel-Rudern");
            mExercises.add("Reverse Flys");
        }
        ((BaseAdapter) mExercisesListAdapter).notifyDataSetChanged();
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
        Nfc.stopForegroundDispatch(this, mNfcAdapter);
        if(mNfcAdapter != null && mNfcAdapter.isEnabled())
            super.onPause();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleNfcIntent(intent);
    }

    public void handleNfcIntent(Intent intent) {
        String device = new String(Nfc.getNdefRecord(intent).getPayload());
        getExercisesFromDevice(device);
    }
}
