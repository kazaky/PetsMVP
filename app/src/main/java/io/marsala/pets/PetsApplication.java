package io.marsala.pets;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by AHMED HAMDI ELSHAHAWI on 8/26/2017.
 * HIT ME @TenFeetShuffler
 */

public class PetsApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // The Realm file will be located in Context.getFilesDir() with name "raven.realm"
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("pets.realm")
                .build();

        // Make this realm the default
        Realm.setDefaultConfiguration(config);


    }
}
