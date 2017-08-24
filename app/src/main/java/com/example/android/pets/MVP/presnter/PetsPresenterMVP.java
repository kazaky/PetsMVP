package com.example.android.pets.MVP.presnter;

import com.example.android.pets.MVP.model.repositories.PetsRepository;
import com.example.android.pets.MVP.view.PetsViewMVP;

/**
 * Created by AHMED HAMDI ELSHAHAWI on 7/23/2017.
 * HIT ME @TenFeetShuffler
 */

public class PetsPresenterMVP {
    private PetsViewMVP petsViewMVP;
    private PetsRepository petsRepository;

    public PetsPresenterMVP(PetsViewMVP petsViewMVP, PetsRepository petsRepository) {
        this.petsViewMVP = petsViewMVP;
        this.petsRepository = petsRepository;
    }

    public void loadPets() {}
}