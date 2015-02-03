package de.tu_dresden.inf.es.workedout.workedout.models;


import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

import de.tu_dresden.inf.es.workedout.workedout.WorkOutPlanActivity;

/**
 * Created by vincent on 29.01.15.
 */
@Table(name = "WorkOutPlans")
public class WorkOutPlan extends Model {
    @Column(name = "name")
    public String name;
    @Column( name ="saveflag")
    public boolean saveflag;


    public WorkOutPlan(){
        super();
    }
    public WorkOutPlan(String name){
        super();
        this.name=name;
        this.saveflag=false;

    }
    public void addExercise(Exercise e){
        WorkOutPlanExercises w=new WorkOutPlanExercises(this ,e);
        w.save();
    }
    public ArrayList<Exercise>getExercises(){
       ArrayList<Exercise>exerciseArrayList= new Select().from(Exercise.class).innerJoin(WorkOutPlanExercises.class).on("WorkOutPlanExercises.exercise = Exercises.id").where("WorkOutPlanExercises.workOutPlan = ?", this.getId()).execute();
       if (exerciseArrayList==null) Log.v("bla", "bla");
        return exerciseArrayList;
    }
    public ArrayList<Exercise>removeExercise(Exercise e){
        return new Delete().from(WorkOutPlanExercises.class).where("exercise = ? and workOutPlan=?", e.getId(), this.getId()).execute();
    }

    public void trueSave(){
        this.saveflag=true;
        super.save();

    }
}
