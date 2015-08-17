package eric.mitapp.mitgpa;

import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by ericmanzi on 5/18/15.
 */
public class Application extends android.app.Application {

    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(Course.class);

        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "e17a3IXYb6KUO8StafzVc5wmt1HO3SkgDO08HjGT", "6hAZDoHvWEjtraob9bS56Ss0WiQBCsO8h9wFVnT6");

    }
}