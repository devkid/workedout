package de.tu_dresden.inf.es.workedout.workedout;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.activeandroid.query.Select;

import de.tu_dresden.inf.es.workedout.workedout.models.Exercise;
import de.tu_dresden.inf.es.workedout.workedout.models.WorkOutPlan;
import de.tu_dresden.inf.es.workedout.workedout.utils.Nfc;


public class WorkOutPlanActivity extends ActionBarActivity {
    WorkOutPlan workOutPlan;
    private ExerciseAdapter exerciseAdapter;

    NfcAdapter mNfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_out_plan);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        Intent intent = getIntent();

        if(intent.hasExtra("workOutPlan")) {
            workOutPlan = new Select().from(WorkOutPlan.class).where("name=?", intent.getStringExtra("workOutPlan")).executeSingle();
            Log.v("WorkOutPlan", "Using existing WorkOut Plan with ID");
        }
        else {
            workOutPlan = new WorkOutPlan(intent.getStringExtra("name"));
            workOutPlan.save();
            Log.v("WorkOutPlan", "Creating new WorkOut Plan");
        }

        Log.v("WorkOut Plan has ID", workOutPlan.getId().toString());

        TextView workoutName= (TextView) findViewById(R.id.workOutPlanName);
        workoutName.setText(workOutPlan.name);

        exerciseAdapter = new ExerciseAdapter(this, workOutPlan);
        ListView listView = (ListView) findViewById(R.id.exerciseListView);
        listView.setAdapter(exerciseAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mNfcAdapter != null && mNfcAdapter.isEnabled())
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
        startActivityForResult(newIntent, 1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_work_out_plan, menu);
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

    public void onAddNewExercise(View view) {
        Intent intent = new Intent(this,SelectBodyPartActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == Activity.RESULT_OK) {
            Exercise ex = Exercise.load(Exercise.class, data.getLongExtra("exercise", 0));
            workOutPlan.addExercise(ex);
            exerciseAdapter.notifyDataSetChanged();
        }
    }
}
