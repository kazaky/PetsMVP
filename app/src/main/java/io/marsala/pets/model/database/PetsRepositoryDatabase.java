package io.marsala.pets.model.database;

import android.support.annotation.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import io.marsala.pets.model.models.Pet;
import io.marsala.pets.model.repositories.PetsRepository;
import io.reactivex.Single;
import io.realm.Realm;
import io.realm.RealmResults;

import static io.marsala.pets.model.models.Constants.PET_ID;
import static io.marsala.pets.model.models.Constants.PET_NAME;

/**
 * Created by AHMED HAMDI ELSHAHAWI on 8/26/2017.
 * HIT ME @TenFeetShuffler
 */

public class PetsRepositoryDatabase implements PetsRepository {

    private Realm realm;

    public PetsRepositoryDatabase(Realm realm) {
        this.realm = realm;
    }

    /**
     * @param searchKeyword set to null if you want all the pets
     * @param id            set to -1 if you want all the pets
     * @return a list of all pets OR list of specific pets OR list of one pet
     */
    @Override
    public List<Pet> getPets(String searchKeyword, long id) {
        List<Pet> results = Collections.emptyList();

        // Get all pets
        if (searchKeyword == null && id == -1) {
            results = realm.where(Pet.class).findAll();
        }

        // Return matched keyword pets only
        else if (searchKeyword != null && id == -1) {
            RealmResults<Pet> resultsInNames = realm.where(Pet.class)
                    .contains(PET_NAME, searchKeyword)
                    .findAll();
            results.addAll(resultsInNames);
        }

        // Return a list of one specific pet by his id
        else {
            Pet existentPet = realm.where(Pet.class)
                    .equalTo(PET_ID, id).findFirst();
            results = Arrays.asList(existentPet);
        }
        return results;
    }

    @Override
    public Single<List<Pet>> getPetsReactively(final String searchKeyword, final long id) {
        return Single.fromCallable(new Callable<List<Pet>>() {
            @Override
            public List<Pet> call() throws Exception {
                return getPets(searchKeyword, id);
            }
        });
    }


    @Override
    public boolean addNewPet(final String name,
                             final String breed,
                             final String gender,
                             final String weight) {
        // TODO use executeTransactionAsync to have an onSuccess, onError
        realm.executeTransaction(new Realm.Transaction() {

            @Override
            public void execute(Realm realm) {
                Pet newPet = realm.createObject(Pet.class, new Date().getTime()); // Primary key
                newPet.setName(name);
                newPet.setBreed(breed);
                newPet.setGender(gender);
                newPet.setWeight(weight);
            }
        });

        return true;
    }

    @Override
    public boolean updateExistentPet(@Nullable final long id,
                                     final String name,
                                     final String breed,
                                     final String gender,
                                     final String weight) {
        realm.executeTransaction(new Realm.Transaction() {

            @Override
            public void execute(Realm realm) {
                Pet existentPet = realm.where(Pet.class)
                        .equalTo(PET_ID, id).findFirst();
                existentPet.setName(name);
                existentPet.setBreed(breed);
                existentPet.setGender(gender);
                existentPet.setWeight(weight);


            }
        });

        return true;
    }

    @Override
    public void deleteAll() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // Delete all pets
                RealmResults<Pet> allPets = realm.where(Pet.class).findAll();
                allPets.deleteAllFromRealm();
            }
        });
    }

    @Override
    public boolean deleteOnePet(final long id) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // Delete one pet with id
                Pet existentPet = realm.where(Pet.class)
                        .equalTo(PET_ID, id).findFirst();

                existentPet.deleteFromRealm();

            }
        });
        return true;
    }

}