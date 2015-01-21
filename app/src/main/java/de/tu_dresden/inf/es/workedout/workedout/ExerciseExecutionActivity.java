package de.tu_dresden.inf.es.workedout.workedout;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import de.tu_dresden.inf.es.workedout.workedout.utils.Vector;


public class ExerciseExecutionActivity extends ActionBarActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    private Vector mLastVector = null;
    private Vector mGravity = new Vector(0, 0, 0);
    private int mLastDirection = 1;
    private int mCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_execution);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        updateCounter();

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    private void updateCounter() {
        TextView textView = (TextView) findViewById(R.id.counter);
        textView.setText(String.format("%d / %d", ++mCounter/2, 15));
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        final double alpha = 0.8;
        final double thresh = 0.4;

        Vector v = new Vector(event.values[0], event.values[1], event.values[2]);
        double length = v.length();

        // detect gravity by a low pass filter
        mGravity = mGravity.multiplied(alpha).added(v.multiplied(1 - alpha));

        // real acceleration
        v.subtract(mGravity);

        // Debug Output
        ((TextView) findViewById(R.id.debug)).setText(v.toString("\n"));

        if(mLastVector != null && length != 0) {
            double scalarProduct = v.scalarProduct(mLastVector);
            if (((scalarProduct < 0 && mLastDirection ==  1) ||
                 (scalarProduct > 0 && mLastDirection == -1)) &&
                (mLastVector.length() - v.length()) > thresh) {
                mLastDirection *= -1;
                updateCounter();
            }
        }
        mLastVector = v;
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

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
