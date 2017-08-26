package io.marsala.pets.MVP.model.repositories;

import io.marsala.pets.MVP.model.models.Pet;

import java.util.List;

/**
 * Created by AHMED HAMDI ELSHAHAWI on 7/23/2017.
 * HIT ME @TenFeetShuffler
 */

public interface PetsRepository {


    List<Pet> getPets();

}
