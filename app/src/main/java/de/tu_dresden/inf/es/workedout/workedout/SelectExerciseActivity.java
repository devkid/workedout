package de.tu_dresden.inf.es.workedout.workedout;

import android.app.Activity;
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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.activeandroid.query.Select;

import java.util.ArrayList;
import java.util.List;

import de.tu_dresden.inf.es.workedout.workedout.models.Device;
import de.tu_dresden.inf.es.workedout.workedout.models.DeviceExercises;
import de.tu_dresden.inf.es.workedout.workedout.models.Exercise;
import de.tu_dresden.inf.es.workedout.workedout.models.ExerciseBodyParts;
import de.tu_dresden.inf.es.workedout.workedout.utils.Nfc;


public class SelectExerciseActivity extends ActionBarActivity {

    private NfcAdapter mNfcAdapter;
    private List<String> mExercises;
    private List<String> mPreSearchExercises;
    private ListAdapter mExercisesListAdapter;

    public SelectExerciseActivity() {
        super();
        mExercises = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_exercise);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // list and adapter for recommended exercises
        mExercisesListAdapter = new ArrayAdapter<>(getApplicationContext(),
                R.layout.list_item_black_text, R.id.black_text, mExercises);
        ListView exercisesView = (ListView) findViewById(R.id.exerciseListView);
        exercisesView.setAdapter(mExercisesListAdapter);

        final Intent intent = getIntent();

        // intent came from NFC event
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(mNfcAdapter != null && mNfcAdapter.isEnabled() && Nfc.isNfcIntent(intent)) {
            handleNfcIntent(intent);
        }
        else {
            if(intent.hasExtra("bodyPartId"))
                getExercisesFromBodyPart(intent.getLongExtra("bodyPartId", 0));
            else if (intent.hasExtra("device"))
                getExercisesFromDevice(intent.getStringExtra("device"));
            else {
                mExercises.clear();
                List<Exercise> exercises = new Select().from(Exercise.class).execute();
                for (Exercise e : exercises) {
                    mExercises.add(e.name);
                }
            }

        }

        // list item selection handling
        exercisesView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // get selected entry
                String entry = (String) parent.getItemAtPosition(position);

                // back to WorkOut Plan
                Intent intent = new Intent();
                intent.putExtra("exercise", new Select().from(Exercise.class).where("name = ?", entry).executeSingle().getId());
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });

        // search handling
        SearchView searchView = (SearchView) findViewById(R.id.searchExercises);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(mPreSearchExercises == null)
                    mPreSearchExercises = new ArrayList<>(mExercises);

                mExercises.clear();

                String searchText = "%" + newText + "%";

                List<Exercise> exercises;
                if (newText.equals(""))
                    exercises = Exercise.all(Exercise.class);
                else
                    exercises = new Select().from(Exercise.class).where("Exercises.name LIKE ?", searchText).execute();

                for (Exercise e : exercises) {
                    mExercises.add(e.name);
                }
                ((BaseAdapter) mExercisesListAdapter).notifyDataSetChanged();

                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                if(mPreSearchExercises != null) {
                    mExercises.clear();
                    mExercises.addAll(mPreSearchExercises);
                    mPreSearchExercises = null;
                }
                return false;
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
        if(device == null)
            return;

        mExercises.clear();
        Device d = new Select().from(Device.class).where("text_id = ?", device).executeSingle();
        if(d == null) {
            Toast.makeText(this, "Unknown device", Toast.LENGTH_SHORT).show();
            return;
        }
        List<Exercise> exercises = new Select().from(Exercise.class).innerJoin(DeviceExercises.class).on("DeviceExercises.exercise = Exercises.id").where("DeviceExercises.device = ?", d.getId()).execute();
        for(Exercise e: exercises) {
            mExercises.add(e.name);
        }
        ((BaseAdapter) mExercisesListAdapter).notifyDataSetChanged();
    }

    public void getExercisesFromBodyPart(long bodyPartId) {
        mExercises.clear();
        List<Exercise> exercises = new Select().from(Exercise.class).innerJoin(ExerciseBodyParts.class).on("ExerciseBodyParts.exercise = Exercises.id").where("ExerciseBodyParts.bodyPart = ?", bodyPartId).execute();
        for(Exercise e: exercises) {
            mExercises.add(e.name);
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
        if(mNfcAdapter != null && mNfcAdapter.isEnabled())
            Nfc.stopForegroundDispatch(this, mNfcAdapter);
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
