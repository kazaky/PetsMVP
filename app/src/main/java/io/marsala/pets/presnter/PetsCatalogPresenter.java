package io.marsala.pets.presnter;

import android.text.TextUtils;

import java.util.List;

import io.marsala.pets.model.models.Pet;
import io.marsala.pets.model.repositories.PetsRepository;
import io.marsala.pets.view.PetsCatalogView;
import io.marsala.pets.view.PetsEditorView;

/**
 * Created by AHMED HAMDI ELSHAHAWI on 7/23/2017.
 * HIT ME @TenFeetShuffler
 */

public class PetsCatalogPresenter {
    private PetsCatalogView petsCatalogView;
    private PetsRepository petsRepository;

    public PetsCatalogPresenter(PetsCatalogView petsCatalogView, PetsRepository petsRepository) {
        this.petsCatalogView = petsCatalogView;
        this.petsRepository = petsRepository;
    }

    public PetsCatalogPresenter(PetsRepository petsRepository) {
        this.petsRepository = petsRepository;
    }

    /**
     * Loads pets from database repository to view
     */
    public void loadPets() {
        // Load pets from database
        List<Pet> petsList;
        try {
            petsList = petsRepository.getPets(null, -1);

            // Display no pets into view
            if (petsList.isEmpty()) {
                petsCatalogView.displayNoPets();
            }

            // Display pets into view
            else {
                petsCatalogView.displayPets(petsList);
            }

        } catch (Exception e) {

            petsCatalogView.displayError(e);
        }

    }

    public void deleteAllPets() {
        petsRepository.deleteAll();
    }

}