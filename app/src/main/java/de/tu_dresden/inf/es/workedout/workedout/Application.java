package de.tu_dresden.inf.es.workedout.workedout;

import com.activeandroid.ActiveAndroid;

import de.tu_dresden.inf.es.workedout.workedout.models.BodyPart;

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);

        if(BodyPart.all(BodyPart.class).isEmpty()) {
            new BodyPart(false, "Shoulders-Breast", 0, 0, 100, 100).save();
            new BodyPart(false, "Arms", 0, 0, 100, 100).save();
            new BodyPart(false, "Abdominal", 0, 0, 100, 100).save();
            new BodyPart(false, "UpperLegs-Crotch", 0, 0, 100, 100).save();
            new BodyPart(false, "LowerLegs", 0, 0, 100, 100).save();

            new BodyPart(true, "Neck-UpperBack-Shoulders", 0, 0, 100, 100).save();
            new BodyPart(true, "Arms", 0, 0, 100, 100).save();
            new BodyPart(true, "LowerBack", 0, 0, 100, 100).save();
            new BodyPart(true, "Buttocks", 0, 0, 100, 100).save();
            new BodyPart(true, "UpperLegs", 0, 0, 100, 100).save();
            new BodyPart(true, "LowerLegs", 0, 0, 100, 100).save();
        }
    }

    @Override
    public void onTerminate() {
        ActiveAndroid.dispose();
    }
}
