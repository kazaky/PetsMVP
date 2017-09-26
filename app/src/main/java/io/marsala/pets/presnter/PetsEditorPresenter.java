package io.marsala.pets.presnter;

import android.text.TextUtils;

import java.util.List;

import io.marsala.pets.model.models.Pet;
import io.marsala.pets.model.repositories.PetsRepository;
import io.marsala.pets.view.PetsEditorView;
import io.reactivex.functions.Consumer;

/**
 * Created by AHMED HAMDI ELSHAHAWI on 7/23/2017.
 * HIT ME @TenFeetShuffler
 */

public class PetsEditorPresenter {
    private PetsEditorView petsEditorView;
    private PetsRepository petsRepository;


    public PetsEditorPresenter(PetsEditorView petsEditorView, PetsRepository petsRepository) {
        this.petsEditorView = petsEditorView;
        this.petsRepository = petsRepository;
    }


    public void startNewPet() {
        petsEditorView.displayEmptyFields();
    }

    public void editExistentPet(long idCurrentPet) {
        try {
            List<Pet> onePetList = petsRepository.getPets(null, idCurrentPet);
            petsEditorView.displayExistentPet(onePetList.get(0));
        } catch (Exception e) {
            petsEditorView.displayStorageError();
        }
    }
    public void editExistentPetReactively(long idCurrentPet) {
            petsRepository.getPetsReactively(null, idCurrentPet)
                    .subscribe(new Consumer<List<Pet>>() {
                        @Override
                        public void accept(List<Pet> onePetList) throws Exception {
                            petsEditorView.displayExistentPet(onePetList.get(0));

                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            petsEditorView.displayStorageError();
                        }
                    });



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

        if (inputsAreValid) {
            if (id == 0)
                isPetSaved = petsRepository.addNewPet(name, breed, gender, weight);

            else
                isPetSaved = petsRepository.updateExistentPet(id, name, breed, gender, weight);

        }
        return isPetSaved;
    }

    public void deleteCurrentPet(long id) {

        // Perform the deletion of the pet in the database.
        boolean result = petsRepository.deleteOnePet(id);

        if (result) {
            petsEditorView.displayPetDeleted();

            // Otherwise, the delete was successful and we can display a toast.
        } else petsEditorView.displayDeletionError();


    }


    public void startNewOrEdit(long idCurrentPet) {

        // Start a new pet
        if (idCurrentPet == 0)
            startNewPet();

            // Edit an existent pet
        else editExistentPetReactively(idCurrentPet);
    }

    public void promptDeletion() {
        petsEditorView.displayDeletePrompt();
    }
}

