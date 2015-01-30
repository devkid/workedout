package de.tu_dresden.inf.es.workedout.workedout.models;

import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Date;

/**
 * Created by vincent on 29.01.15.
 */
@Table(name = "WorkOutPlan")
public class WorkeOutPlan {
    @Column(name = "name")
    String name;
    @Column(name="exercises")
    Exercise[] execercises;

    public WorkeOutPlan(String name, Exercise[] exercises){
        this.execercises=exercises;
        this.name=name;
    }

}
