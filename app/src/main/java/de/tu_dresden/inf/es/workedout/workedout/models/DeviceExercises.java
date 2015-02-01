package de.tu_dresden.inf.es.workedout.workedout.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "DeviceExercises")
public class DeviceExercises extends Model {
    @Column(name = "device")
    public Device device;

    @Column(name = "exercise")
    public Exercise exercise;

    public DeviceExercises() {
        super();
    }

    public DeviceExercises(Device device, Exercise exercise) {
        super();
        this.device = device;
        this.exercise = exercise;
    }
}
