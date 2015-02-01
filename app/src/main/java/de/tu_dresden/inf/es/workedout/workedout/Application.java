package de.tu_dresden.inf.es.workedout.workedout;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;

import de.tu_dresden.inf.es.workedout.workedout.models.BodyPart;

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
        this.initializeBodyParts();


    }

    @Override
    public void onTerminate() {
        ActiveAndroid.dispose();
    }

    private void initializeBodyParts(){
        if(!BodyPart.all(BodyPart.class).isEmpty()) {
            new Delete().from(BodyPart.class).execute();
        }

        //Front
        new BodyPart(false, "Shoulders-Breast", 26, 17, 66, 28).save();
        new BodyPart(false, "Arms", 20, 28, 27, 53).save();
        new BodyPart(false, "Abdominal", 39, 32, 56, 41).save();
        new BodyPart(false, "UpperLegs-Crotch", 36, 44, 64, 63).save();
        new BodyPart(false, "LowerLegs", 36, 74, 70, 88).save();

        // Back
        new BodyPart(true, "Neck-UpperBack-Shoulders", 48, 12, 66, 29).save();
        new BodyPart(true, "Arms", 30, 28, 41, 58).save();
        new BodyPart(true, "LowerBack", 45, 29, 62, 37).save();
        new BodyPart(true, "Buttocks", 44, 38, 66,48).save();
        new BodyPart(true, "UpperLegs", 46, 54, 67, 66).save();
        new BodyPart(true, "LowerLegs", 64, 92, 63, 92).save();


    }
}
