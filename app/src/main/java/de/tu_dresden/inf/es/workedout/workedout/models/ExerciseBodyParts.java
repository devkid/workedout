package de.tu_dresden.inf.es.workedout.workedout.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "ExerciseBodyParts")
public class ExerciseBodyParts extends Model {
    @Column(name = "exercise")
    public Exercise exercise;

    @Column(name = "bodyPart")
    public BodyPart bodyPart;

    public ExerciseBodyParts() {
        super();
    }

    public ExerciseBodyParts(Exercise exercise, BodyPart bodyPart) {
        super();
        this.exercise = exercise;
        this.bodyPart = bodyPart;
    }
}
