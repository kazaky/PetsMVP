package com.example.android.pets;

import com.example.android.pets.MVP.model.repositories.PetsRepository;
import com.example.android.pets.MVP.presnter.PetsPresenterMVP;
import com.example.android.pets.MVP.view.PetsViewMVP;
import com.example.android.pets.database.Pet;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Created by AHMED HAMDI ELSHAHAWI on 7/23/2017.
 * HIT ME @TenFeetShuffler
 */
public class PetsPresenterMVPTest {


    // Demo test
/*
    @Test
    public void shouldPass() {
        Assert.assertEquals(1, 1);
    }
*/

    @Test
    public void passPetsDBToView() {
        // Given
        PetsViewMVP view = new MockView();
        PetsRepository repository = new MockRepository();

        // When
        PetsPresenterMVP presenter = new PetsPresenterMVP(view, repository);
        presenter.loadPets();

        // Then
        Assert.assertEquals(true, ((MockView) view).passed);

    }

    private class MockView implements PetsViewMVP {
        boolean passed;

        @Override
        public void displayPets(List<Pet> petList) {
            passed = true;
        }
    }

    private class MockRepository implements PetsRepository {

        @Override
        public List<Pet> getPets() {

            // Dump data filling
            return Arrays.asList(new Pet(), new Pet(), new Pet());
        }
    }

}