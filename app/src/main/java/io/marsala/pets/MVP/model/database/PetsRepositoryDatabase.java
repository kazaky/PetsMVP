package io.marsala.pets.MVP.model.database;

import android.content.Context;

import io.marsala.pets.MVP.model.repositories.PetsRepository;
import io.marsala.pets.MVP.model.models.Pet;
import io.realm.Realm;

import java.util.List;

/**
 * Created by AHMED HAMDI ELSHAHAWI on 8/26/2017.
 * HIT ME @TenFeetShuffler
 */

public class PetsRepositoryDatabase implements PetsRepository {

    private Realm realm;

    public PetsRepositoryDatabase(Realm realm) {
        this.realm = realm;
    }

    @Override
    public List<Pet> getPets() {
        return null;
    }
}
