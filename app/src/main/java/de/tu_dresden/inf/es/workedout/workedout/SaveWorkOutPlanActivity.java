package de.tu_dresden.inf.es.workedout.workedout;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import de.tu_dresden.inf.es.workedout.workedout.MainActivity;
import de.tu_dresden.inf.es.workedout.workedout.R;
import de.tu_dresden.inf.es.workedout.workedout.models.Exercise;
import de.tu_dresden.inf.es.workedout.workedout.models.WorkOutPlan;
import de.tu_dresden.inf.es.workedout.workedout.models.WorkOutPlanExercises;

public class SaveWorkOutPlanActivity extends ActionBarActivity {
    WorkOutPlan workOutPlan;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_work_out_plan);
        workOutPlan=WorkOutPlan.load(WorkOutPlan.class,getIntent().getLongExtra("workoutplan",-1));
        editText = (EditText) findViewById(R.id.editTextSaveWorkout);
        editText.setText(workOutPlan.name);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_save_work_out_plan, menu);
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

    public void onSaveWorkoutPlan(View view) {
        String text=editText.getText().toString();
        if (workOutPlan.name.equals(text)) {
            workOutPlan.trueSave();

        }
        else {
            Log.v("Arraysize",String.valueOf(workOutPlan.getExercises().size()));
                WorkOutPlan newWorkoutplan =new WorkOutPlan(text);
                newWorkoutplan.save();
                for( Exercise e :workOutPlan.getExercises()){
                    WorkOutPlanExercises wE  =new WorkOutPlanExercises(newWorkoutplan,e);
                    wE.save();
                }
                newWorkoutplan.trueSave();
            }
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
