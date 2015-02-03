package de.tu_dresden.inf.es.workedout.workedout;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.TextView;

import de.tu_dresden.inf.es.workedout.workedout.utils.Vector;


public class ExerciseExecutionActivity extends ActionBarActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    private long mTime = 0;
    private Vector mGravity  = new Vector(0, 0, 0),
                   mVelocity = new Vector(0, 0, 0),
                   mDistance = new Vector(0, 0, 0);
    private boolean mLimited = false;
    private int mCounter = 0;
    private int mSet = 0;
    private long mExerciseId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_execution);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        updateCounter();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // set exercise name
        Intent intent = getIntent();
        String exercise;
        if(intent.hasExtra("exercise"))
            exercise = intent.getStringExtra("exercise");
        else
            exercise = "";
        ((TextView) findViewById(R.id.exerciseName)).setText(exercise);

        // set sets count
        ((TextView) findViewById(R.id.sets)).setText("1/3");

        // set weight
        NumberPicker np = (NumberPicker) findViewById(R.id.numberPicker);
        np.setMinValue(0);
        np.setMaxValue(250);
        np.setWrapSelectorWheel(true);

        onNextSet(null);
    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    private void updateCounter() {
        TextView textView = (TextView) findViewById(R.id.counter);
        textView.setText(String.format("%d / %d", ++mCounter / 2, 15));

        if(mCounter == 30) {
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(new long[] {0, 300, 100, 300, 100, 300}, -1);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        final double alpha = 0.8;
        final double thresh = 0.2;

        final long time = System.nanoTime();
        final double elapsedTime = (time - mTime) * 1e-9;

        if(mTime != 0) {
            Vector v = new Vector(event.values[0], event.values[1], event.values[2]);

            // detect gravity by a low pass filter
            mGravity = mGravity.multiplied(alpha).added(v.multiplied(1 - alpha));

            // real acceleration
            v.subtract(mGravity);

            // integrate to get velocity
            mVelocity.add(v.multiplied(elapsedTime));

            // low pass filtering
            mVelocity = mVelocity.multiplied(alpha).added(v.multiplied(1 - alpha));

            // integrate again to get distance
            mDistance.add(mVelocity.multiplied(elapsedTime));

            // Debug Output
            ((TextView) findViewById(R.id.debug)).setText(mVelocity.toString("\n") + "\n" + String.valueOf(mLimited));

            //double scalarProduct = mVelocity.scalarProduct(lastVelocity);

            // detect zero crossing of velocity and limit detection there
            if (mVelocity.length() <= thresh && !mLimited) {
                mLimited = true;
                updateCounter();
            } else if(mVelocity.length() > thresh && mLimited) {
                mLimited = false;
            }
        }

        mTime = time;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_exercise_execution, menu);
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

    public void onNextSet(View view) {
        if(mSet == 3) {
            finish();
        } else {
            TextView sets = (TextView) findViewById(R.id.sets);
            sets.setText(getString(R.string.set) + ": " + String.valueOf(++mSet) + "/3");
            if(mSet == 3) {
                ((Button) findViewById(R.id.next_set)).setText(getString(R.string.finish_exercise));
            }
        }
    }

    public void onStatistics(View view) {
        Intent intent = new Intent(this, StatisticsActivity.class);
        intent.putExtra("exercise", mExerciseId);
        startActivity(intent);
    }
}
