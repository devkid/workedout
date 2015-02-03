package de.tu_dresden.inf.es.workedout.workedout;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.activeandroid.query.Select;

import de.tu_dresden.inf.es.workedout.workedout.models.Exercise;
import de.tu_dresden.inf.es.workedout.workedout.models.WorkOutPlan;


public class WorkOutPlanActivity extends ActionBarActivity {
    WorkOutPlan workOutPlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_out_plan);

        Intent intent= getIntent();
        if(savedInstanceState==null) Log.v("SavedInstance","==null");
        if(savedInstanceState!=null) this.handelSavedInstanceState(savedInstanceState,getIntent());

        else if(intent.hasExtra("newPlan")) {
            workOutPlan = new WorkOutPlan("new Workoutplan");
            workOutPlan.save();
            Log.v("1", "1");
        }
        else if (intent.hasExtra("workOutPlan")){
            Log.v("2", "2");
            workOutPlan=new Select().from(WorkOutPlan.class).where("name=?",intent.getStringExtra("workOutPlan")).executeSingle();
        }


        if(workOutPlan==null) Log.v("Workoutplan","=null");
        TextView workoutName= (TextView)findViewById(R.id.workOutPlanName);
        workoutName.setText(workOutPlan.name);
        ExerciseAdapter adapter = new ExerciseAdapter(this, R.layout.atom_exercise_list_view,workOutPlan );
        ListView atomPaysListView = (ListView)findViewById(R.id.exerciseListView);
        atomPaysListView.setAdapter(adapter);



    }

    private void handelSavedInstanceState(Bundle savedInstanceState,Intent intent) {
        Log.v("SavedInstance","");
        this.workOutPlan=WorkOutPlan.load(WorkOutPlan.class,savedInstanceState.getLong("planid"));
        if(intent.hasExtra("execise")){
            Log.v("SavedInstance","hasExecise");
            this.workOutPlan.addExercise((Exercise)new Select().from(Exercise.class).where("name=?", intent.getStringExtra("exercise")).executeSingle());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_work_out_plan, menu);
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
        Log.v("","onSaveexecuted");
        workOutPlan.save();
        Intent intent=new Intent(this, SaveWorkOutPlanActivity.class);
        intent.putExtra("workoutplan", workOutPlan.getId());
        startActivity(intent);

    }

    public void onAddNewExercise(View view) {
        Intent intent = new Intent(this,SelectBodyPartActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        Log.v("saved","executed");
        workOutPlan.save();
        outState.putLong("planid",workOutPlan.getId());
        super.onSaveInstanceState(outState);
    }
}
