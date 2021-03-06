package de.tu_dresden.inf.es.workedout.workedout.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Exercises")
public class Exercise extends Model {
    @Column(name = "name")
    public String name;

    @Column(name = "youtube")
    public String youtube;

    public Exercise() {
        super();
    }

    public Exercise(String name, String youtube) {
        super();
        this.name = name;
        this.youtube = youtube;
    }
}
