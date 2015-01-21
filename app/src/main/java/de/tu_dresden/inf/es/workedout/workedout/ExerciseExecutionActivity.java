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


public class ExerciseExecutionActivity extends ActionBarActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    private float mLastSensorEvent[] = {0, 0, 0};
    private int mLastDirection = 1;
    private int mCounter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_execution);

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
        TextView textView = (TextView) findViewById(R.id.counter);
        textView.setText(String.format("%.3f\n%.3f\n%.3f", event.values[0], event.values[1], event.values[2]));
        float vector = (float) Math.sqrt(Math.pow(mLastSensorEvent[0], 2) +
                Math.pow(mLastSensorEvent[1], 2) +
                Math.pow(mLastSensorEvent[2], 2));
        if(vector != 0) {
            float scalarProduct = (mLastSensorEvent[0] * event.values[0] +
                                   mLastSensorEvent[1] * event.values[1] +
                                   mLastSensorEvent[2] * event.values[2]) / vector;
            if ((scalarProduct < 0 && mLastDirection == 1) ||
                    (scalarProduct > 0 && mLastDirection == -1)) {
                mLastDirection *= -1;
                updateCounter();
            }
        }
        System.arraycopy(event.values, 0, mLastSensorEvent, 0, 3);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_exercise_execution, menu);
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

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}
