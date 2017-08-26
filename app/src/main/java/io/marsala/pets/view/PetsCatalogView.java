package io.marsala.pets.view;

import io.marsala.pets.model.models.Pet;

import java.util.List;

/**
 * Created by AHMED HAMDI ELSHAHAWI on 7/23/2017.
 * HIT ME @TenFeetShuffler
 */

public interface PetsCatalogView {

    /**
     * method called by the presenter on the view
     *
     * @param petsList is passed by the presenter as an instance of the database
     */
    void displayPets(List<Pet> petsList);

    void displayNoPets();

}
