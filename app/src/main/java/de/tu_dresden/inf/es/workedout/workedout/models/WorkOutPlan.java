package de.tu_dresden.inf.es.workedout.workedout.models;


import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.ArrayList;

/**
 * Created by vincent on 29.01.15.
 */
@Table(name = "WorkOutPlans")
public class WorkOutPlan extends Model {
    @Column(name = "name")
    public String name;

    public WorkOutPlan(){
        super();
    }
    public WorkOutPlan(String name){
        super();
        this.name=name;
    }

    public void addExercise(Exercise e){
        WorkOutPlanExercises w = new WorkOutPlanExercises(this ,e);
        w.save();
    }

    public ArrayList<Exercise> getExercises(){
        ArrayList<Exercise> exerciseArrayList = new ArrayList<>();
        for(Model we: new Select().from(WorkOutPlanExercises.class).innerJoin(Exercise.class).on("WorkOutPlanExercises.exercise = Exercises.id").where("WorkOutPlanExercises.workOutPlan = ?", getId()).execute())
            exerciseArrayList.add(((WorkOutPlanExercises) we).exercise);
        return exerciseArrayList;
    }

    public void removeExercise(Exercise e){
        for(Model m: new Select().from(WorkOutPlanExercises.class).execute()) {
            WorkOutPlanExercises ex = (WorkOutPlanExercises) m;
            Log.v("WorkOutPlan <-> Exercise", getId().toString() + " " + ex.exercise.getId().toString());
        }
        new Delete().from(WorkOutPlanExercises.class).where("exercise = ? and workOutPlan = ?", e.getId(), this.getId()).execute();
    }
}
