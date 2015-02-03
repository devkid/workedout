package de.tu_dresden.inf.es.workedout.workedout;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.activeandroid.query.Select;

import de.tu_dresden.inf.es.workedout.workedout.models.Exercise;
import de.tu_dresden.inf.es.workedout.workedout.models.WorkOutPlan;


public class WorkOutPlanActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_out_plan);
        WorkOutPlan w=new Select().from(WorkOutPlan.class).executeSingle();
        if(w==null) Log.v("Test","NULL");
        Log.v("Exerweaw",(String.valueOf(w.getExercises().size())));
        ExerciseAdapter adapter = new ExerciseAdapter(this, R.layout.atom_exercise_list_view,w );
        ListView atomPaysListView = (ListView)findViewById(R.id.exerciseListView);
        atomPaysListView.setAdapter(adapter);



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

}
