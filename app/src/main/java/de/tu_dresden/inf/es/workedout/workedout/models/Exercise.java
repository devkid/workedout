package de.tu_dresden.inf.es.workedout.workedout.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Exercises")
public class Exercise extends Model {
    @Column(name = "name")
    String name;

    @Column(name = "bodyParts")
    BodyPart[] bodyParts;

    @Column(name = "youtube")
    String youtube;

    public Exercise() {
        super();
    }

    public Exercise(String name, BodyPart[] bodyParts, String youtube) {
        this.name = name;
        this.bodyParts = bodyParts;
        this.youtube = youtube;
    }
}
