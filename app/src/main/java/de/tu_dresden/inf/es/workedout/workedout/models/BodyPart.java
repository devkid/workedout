package de.tu_dresden.inf.es.workedout.workedout.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;


@Table(name = "BodyParts")
public class BodyPart extends Model {
    @Column(name = "front_or_back")
    public Boolean front_or_back;

    @Column(name = "name")
    public String name;

    @Column(name = "leftUpperX")
    public Integer leftUpperX;

    @Column(name = "leftUpperY")
    public Integer leftUpperY;

    @Column(name = "rightLowerX")
    public Integer rightLowerX;

    @Column(name = "rightLowerY")
    public Integer rightLowerY;

    public BodyPart() {
        super();
    }

    public BodyPart(Boolean front_or_back, String name,
                    Integer leftUpperX, Integer leftUpperY,
                    Integer rightLowerX, Integer rightLowerY) {
        this.front_or_back = front_or_back;
        this.name = name;
        this.leftUpperX = leftUpperX;
        this.leftUpperY = leftUpperY;
        this.rightLowerX = rightLowerX;
        this.rightLowerY = rightLowerY;
    }
    public boolean checkIfTouched(int x,int y,long front_or_back){
        if ((this.front_or_back && front_or_back==1 )||(!this.front_or_back && front_or_back==0)) {
            return (x >= leftUpperX && x <= rightLowerX && y >= leftUpperY && y <= rightLowerY);
        }
        else return false;
    }
}
