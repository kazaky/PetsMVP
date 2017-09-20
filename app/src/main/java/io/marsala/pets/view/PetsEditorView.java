package io.marsala.pets.view;

import io.marsala.pets.model.models.Pet;

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


    void displayStorageError();

    void displayDeletePrompt();

    void displayPetDeleted();

    void displayDeletionError();
}
