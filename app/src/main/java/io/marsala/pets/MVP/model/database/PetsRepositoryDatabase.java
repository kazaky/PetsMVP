package io.marsala.pets.MVP.model.database;

import android.support.annotation.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import io.marsala.pets.MVP.model.models.Pet;
import io.marsala.pets.MVP.model.repositories.PetsRepository;
import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.exceptions.RealmException;

import static io.marsala.pets.MVP.model.models.Constants.PET_ID;
import static io.marsala.pets.MVP.model.models.Constants.PET_NAME;

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
        List<Pet> results = null;

        try {
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
            else if (id != -1) {
                Pet existentPet = realm.where(Pet.class)
                        .equalTo(PET_ID, id).findFirst();
                results = Arrays.asList(existentPet);
            }
        }

        // If error happened, Return an empty list
        catch (RealmException e) {
            e.printStackTrace();
            results = Collections.emptyList();
        }

        return results;
    }


    /**
     * @param id     set to 0 if you it's a new pet!
     * @param name
     * @param breed
     * @param gender
     * @param weight
     */
    @Override
    public void addOrUpdatePet(@Nullable final long id,
                               final String name,
                               final String breed,
                               final String gender,
                               final String weight) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                if (id == 0) {
                    // Add new pet
                    Pet newPet = realm.createObject(Pet.class, new Date().getTime()); // Primary key
                    newPet.setName(name);
                    newPet.setBreed(breed);
                    newPet.setGender(gender);
                    newPet.setWeight(weight);
                }

                // Update existent pets
                else {
                    Pet existentPet = realm.where(Pet.class)
                            .equalTo(PET_ID, id).findFirst();
                    existentPet.setName(name);
                    existentPet.setBreed(breed);
                    existentPet.setGender(gender);
                    existentPet.setWeight(weight);
                }

            }
        });
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


}