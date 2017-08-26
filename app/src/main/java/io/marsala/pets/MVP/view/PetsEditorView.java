package io.marsala.pets.MVP.view;

import java.util.List;

import io.marsala.pets.MVP.model.models.Pet;

/**
 * Created by AHMED HAMDI ELSHAHAWI on 8/26/2017.
 * HIT ME @TenFeetShuffler
 */

public interface PetsEditorView {

    void displayEmptyFields();

    /**
     * method called by the presenter on the view
     * @param petsList
     */
    void displayExistentPet(Pet petsList);

}
