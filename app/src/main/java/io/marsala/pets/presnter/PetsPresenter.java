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

public class PetsPresenter {
    private PetsEditorView petsEditorView;
    private PetsCatalogView petsCatalogView;
    private PetsRepository petsRepository;

    public PetsPresenter(PetsCatalogView petsCatalogView, PetsRepository petsRepository) {
        this.petsCatalogView = petsCatalogView;
        this.petsRepository = petsRepository;
    }

    public PetsPresenter(PetsEditorView petsEditorView, PetsRepository petsRepository) {
        this.petsEditorView = petsEditorView;
        this.petsRepository = petsRepository;
    }

    /**
     * Loads pets from database repository to view
     */
    public void loadPets() {
        // Load pets from database
        List<Pet> petsList = petsRepository.getPets(null, -1);

        // Display no pets into view
        if (petsList.isEmpty()) {
            petsCatalogView.displayNoPets();
        }

        // Display pets into view
        else {
            petsCatalogView.displayPets(petsList);
        }


    }

    public void deleteAllPets() {
        petsRepository.deleteAll();
    }

    public void startNewOrEdit(long idCurrentPet) {

        // Start a new pet
        if (idCurrentPet == 0) {
            petsEditorView.displayEmptyFields();
        }

        // Edit an existent pet
        else {
            List<Pet> onePetList = petsRepository.getPets(null, idCurrentPet);
            petsEditorView.displayExistentPet(onePetList.get(0));
        }

    }

    public boolean savePetEditor(long id, String name, String breed, String gender, String weight) {
        boolean inputsAreValid = true;
        boolean isPetSaved = false;

        // check that the name value is not null.
        if (TextUtils.isEmpty(name)) {
            inputsAreValid = false;
            throw new IllegalArgumentException("Pet requires a name");
        }

        // check that the gender value is valid.
        if (gender == null) {
            inputsAreValid = false;
            throw new IllegalArgumentException("Pet requires valid gender");
        }

        // Check that the weight is greater than or equal to 0 kg
        if (TextUtils.isEmpty(weight) || weight.equals("0")) {
            inputsAreValid = false;
            throw new IllegalArgumentException("Pet requires valid weight");
        }


        if (inputsAreValid)
            isPetSaved = petsRepository.addOrUpdatePet(id, name, breed, gender, weight);

        return isPetSaved;
    }

    public boolean deleteOne(long id) {
        boolean result = petsRepository.deleteOnePet(id);
        return result;
    }
}