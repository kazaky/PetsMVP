package com.example.android.pets.MVP.view;

import com.example.android.pets.database.Pet;

import java.util.List;

/**
 * Created by AHMED HAMDI ELSHAHAWI on 7/23/2017.
 * HIT ME @TenFeetShuffler
 */

public interface PetsView {

    /**
     * method called by the presenter on the view
     *
     * @param petsList is passed by the presenter as an instance of the database
     */
    void displayPets(List<Pet> petsList);

    void displayNoPets();

}
