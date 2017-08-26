package io.marsala.pets.MVP.presnter;

import io.marsala.pets.MVP.model.repositories.PetsRepository;
import io.marsala.pets.MVP.view.PetsView;
import io.marsala.pets.MVP.model.models.Pet;

import java.util.List;

/**
 * Created by AHMED HAMDI ELSHAHAWI on 7/23/2017.
 * HIT ME @TenFeetShuffler
 */

public class PetsPresenter {
    private PetsView petsView;
    private PetsRepository petsRepository;

    public PetsPresenter(PetsView petsView, PetsRepository petsRepository) {
        this.petsView = petsView;
        this.petsRepository = petsRepository;
    }

    /**
     * Loads pets from database repository  to view
     */
    public void loadPets() {
        // Load pets from database
        List<Pet> petsList = petsRepository.getPets();

        // Display pets into view
        if (petsList.size() > 0) {
            petsView.displayPets(petsList);
        }

        // Display no pets into view
        else if (petsList.isEmpty()){
            petsView.displayNoPets();
        }


    }

}