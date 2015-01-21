package de.tu_dresden.inf.es.workedout.workedout;

import com.activeandroid.ActiveAndroid;

import de.tu_dresden.inf.es.workedout.workedout.models.BodyPart;

public class Application extends android.app.Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);

        if(BodyPart.all(BodyPart.class).isEmpty()) {
            BodyPart neck = new BodyPart(false, "Neck", 0, 0, 100, 100);
            neck.save();
        }
    }

    @Override
    public void onTerminate() {
        ActiveAndroid.dispose();
    }
}
