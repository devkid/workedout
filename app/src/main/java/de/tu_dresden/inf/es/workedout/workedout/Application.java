package de.tu_dresden.inf.es.workedout.workedout;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Model;
import com.activeandroid.query.Delete;

import de.tu_dresden.inf.es.workedout.workedout.models.BodyPart;
import de.tu_dresden.inf.es.workedout.workedout.models.Device;
import de.tu_dresden.inf.es.workedout.workedout.models.DeviceExercises;
import de.tu_dresden.inf.es.workedout.workedout.models.Exercise;
import de.tu_dresden.inf.es.workedout.workedout.models.ExerciseBodyParts;

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

    private <T extends Model> T saveAndGet(T obj) {
        obj.save();
        return obj;
    }

    private void addExercise(Exercise e, BodyPart b) {
        e.save();
        new ExerciseBodyParts(e, b).save();
    }

    private void addExercise(Exercise e, BodyPart[] b) {
        e.save();
        for(BodyPart bp: b)
            new ExerciseBodyParts(e, bp).save();
    }

    private void addExercises(Exercise[] e, BodyPart b) {
        for(Exercise ex: e) {
            ex.save();
            new ExerciseBodyParts(ex, b).save();
        }
    }

    private void addExercises(Exercise[] e, BodyPart[] b) {
        for(BodyPart bp: b) {
            for (Exercise ex : e) {
                ex.save();
                new ExerciseBodyParts(ex, bp).save();
            }
        }
    }

    private void addDevice(Device d, Exercise[] e) {
        d.save();
        for (Exercise ex : e) {
            new DeviceExercises(d, ex).save();
        }
    }

    private void initializeBodyParts(){
        if(!BodyPart.all(BodyPart.class).isEmpty()) {
            new Delete().from(BodyPart.class).execute();
        }

        /*
            Body Parts
         */

        // Front
        BodyPart shoulderBreast = saveAndGet(new BodyPart(false, "Shoulders-Breast", 26, 17, 66, 28));
        BodyPart armsFront = saveAndGet(new BodyPart(false, "Arms", 20, 28, 27, 53));
        BodyPart abdominal = saveAndGet(new BodyPart(false, "Abdominal", 39, 32, 56, 41));
        BodyPart upperLegsCrotch = saveAndGet(new BodyPart(false, "UpperLegs-Crotch", 36, 44, 64, 63));
        BodyPart lowerLegsFront = saveAndGet(new BodyPart(false, "LowerLegs", 36, 74, 70, 88));

        // Back
        BodyPart neckUpperBackShoulders = saveAndGet(new BodyPart(true, "Neck-UpperBack-Shoulders", 48, 12, 66, 29));
        BodyPart armsBack = saveAndGet(new BodyPart(true, "Arms", 30, 28, 41, 58));
        BodyPart lowerBack = saveAndGet(new BodyPart(true, "LowerBack", 45, 29, 62, 37));
        BodyPart buttocks = saveAndGet(new BodyPart(true, "Buttocks", 44, 38, 66,48));
        BodyPart upperLegs = saveAndGet(new BodyPart(true, "UpperLegs", 46, 54, 67, 66));
        BodyPart lowerLegsBack = saveAndGet(new BodyPart(true, "LowerLegs", 64, 92, 63, 92));


        /*
            Exercises
         */

        Exercise engesBankdruecken = new Exercise("Enges Bankdrücken", null);
        Exercise bankdruecken = new Exercise("Bankdrücken", null);
        Exercise beinpresse = new Exercise("Beinpresse", null);
        Exercise wadenwippen = new Exercise("Wadenwippen", null);

        addExercises(new Exercise[] {
                bankdruecken,
            new Exercise("Schrägbankdrücken", null),
        }, shoulderBreast);

        addExercises(new Exercise[] {
                new Exercise("Kurzhantel-Curls", null)
        }, armsFront);

        addExercises(new Exercise[]{
                new Exercise("Trizepsdrücken", null),
                engesBankdruecken
        }, armsBack);

        addExercise(beinpresse, new BodyPart[]{
                upperLegsCrotch,
                upperLegs
        });

        addExercise(wadenwippen, new BodyPart[] {
            lowerLegsFront,
            lowerLegsBack
        });

        addExercises(new Exercise[] {
            new Exercise("Shrugs", null),
            new Exercise("Schulterdrücken", null),
            new Exercise("Reverse Flys", null),
            new Exercise("Rudern", null),
            new Exercise("Reverse Butterfly", null)
        }, neckUpperBackShoulders);


        /*
            Devices
         */

        addDevice(new Device("Leg press", "leg_press"), new Exercise[] {
            beinpresse,
            wadenwippen
        });
        addDevice(new Device("Flat bench", "flat_bench"), new Exercise[] {
            bankdruecken,
            engesBankdruecken
        });
    }
}
