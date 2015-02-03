package de.tu_dresden.inf.es.workedout.workedout.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by vincent on 02.02.15.
 */
@Table(name="WorkOutPlanExercises")
public class WorkOutPlanExercises extends Model {
    @Column(name="exercise")
    public Exercise exercise;

    @Column(name="workOutPlan")
    public WorkOutPlan workOutPlan;

    public WorkOutPlanExercises(){super();}
    public WorkOutPlanExercises(WorkOutPlan workOutPlan,Exercise exercise){
        super();
        this.workOutPlan = workOutPlan;
        this.exercise = exercise;
    }
}
