package de.tu_dresden.inf.es.workedout.workedout.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Devices")
public class Device extends Model {
    @Column(name = "name")
    public String name;

    @Column(name = "text_id")
    public String text_id;

    public Device() {
        super();
    }

    public Device(String name, String text_id) {
        super();
        this.name = name;
        this.text_id = text_id;
    }
}
