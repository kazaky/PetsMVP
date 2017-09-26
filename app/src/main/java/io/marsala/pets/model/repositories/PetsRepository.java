package io.marsala.pets.model.repositories;

import android.support.annotation.Nullable;

import java.util.List;

import io.marsala.pets.model.models.Pet;
import io.reactivex.Single;

/**
 * Created by AHMED HAMDI ELSHAHAWI on 7/23/2017.
 * HIT ME @TenFeetShuffler
 */

public interface PetsRepository {

    /**
     * @param searchKeyword set to null if you want all the pets
     * @param id            set to -1 if you want all the pets
     * @return a list of all pets OR list of specific pets OR list of one pet
     */
    List<Pet> getPets(String searchKeyword, long id);

    Single<List<Pet>> getPetsReactively(String searchKeyword, long id);


    boolean addNewPet(String name,
                      String breed,
                      String gender,
                      String weight);

    boolean updateExistentPet(@Nullable long id,
                              String name,
                              String breed,
                              String gender,
                              String weight);

    void deleteAll();

    boolean deleteOnePet(long id);
}
