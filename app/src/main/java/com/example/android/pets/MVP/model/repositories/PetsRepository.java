package com.example.android.pets.MVP.model.repositories;

import com.example.android.pets.database.Pet;

import java.util.List;

/**
 * Created by AHMED HAMDI ELSHAHAWI on 7/23/2017.
 * HIT ME @TenFeetShuffler
 */

public interface PetsRepository {


    List<Pet> getPets();

}
