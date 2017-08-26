package io.marsala.pets.MVP.model.repositories;

import android.support.annotation.Nullable;

import java.util.List;

import io.marsala.pets.MVP.model.models.Pet;

/**
 * Created by AHMED HAMDI ELSHAHAWI on 7/23/2017.
 * HIT ME @TenFeetShuffler
 */

public interface PetsRepository {

    List<Pet> getPets(String searchKeyword, long id);

    boolean addOrUpdatePet(@Nullable long id,
                           String name,
                           String breed,
                           String gender,
                           String weight
    );

    void deleteAll();

    boolean deleteOnePet(long id);
}
