package io.marsala.pets;

import io.marsala.pets.MVP.model.repositories.PetsRepository;
import io.marsala.pets.MVP.presnter.PetsPresenter;
import io.marsala.pets.MVP.view.PetsView;
import io.marsala.pets.MVP.model.models.Pet;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by AHMED HAMDI ELSHAHAWI on 7/23/2017.
 * HIT ME @TenFeetShuffler
 */
public class PetsPresenterTest {


    // Demo test
    @Test
    public void shouldPass() {
        Assert.assertEquals(1, 1);
    }

    @Test
    public void shouldPassPetsDatabaseToView() {
        /**
         * GIVEN
         * Initial conditions,
         * Create instances
         */
        PetsView fakePetsView = new FakePetsView();
        PetsRepository fakePetsRepository = new FakePetsRepository(true);

        /**
         * WHEN
         * Actually the actions we want to trigger,
         * The thing we gonna be testing
         */
        // View is passed to presenter
        PetsPresenter presenter = new PetsPresenter(fakePetsView, fakePetsRepository);
        presenter.loadPets();

        /**
         * THEN
         * Did it work, Or it didn't work
         */
        // We must cast to (FakePetsView) as it does have the passed variable
        Assert.assertEquals(true, ((FakePetsView) fakePetsView).displayPetsWithPetsLoaded);
    }


    @Test
    public void shouldHandleNoPetsFound() {
        // GIVEN
        PetsView fakePetsView = new FakePetsView();
        PetsRepository fakePetsRepository = new FakePetsRepository(false);

        // WHEN
        PetsPresenter petsPresenter = new PetsPresenter(fakePetsView, fakePetsRepository);
        petsPresenter.loadPets();

        Assert.assertEquals(true, ((FakePetsView) fakePetsView).displayPetsWithNoPetsLoaded);

    }


    private class FakePetsView implements PetsView {
        // No need to initialize primitives
        // Default for boolean primitive is true
        boolean displayPetsWithPetsLoaded;
        boolean displayPetsWithNoPetsLoaded;

        @Override
        public void displayPets(List<Pet> petList) {
            // Watch out when writing tests for edgy cases like that!!
            if (petList.size() == 3) displayPetsWithPetsLoaded = true;
        }

        @Override
        public void displayNoPets() {
            displayPetsWithNoPetsLoaded = true;
        }
    }

    private class FakePetsRepository implements PetsRepository {

        private boolean returnSomePets;

        public FakePetsRepository(boolean returnSomePets) {
            this.returnSomePets = returnSomePets;
        }

        @Override
        public List<Pet> getPets() {

            /**
             * We want to control this mock to do different things at different times
             */

            // Sometimes it will return data
            if (returnSomePets == true) {
                return Arrays.asList(new Pet(), new Pet(), new Pet()); // Dump data filling
            }

            // Sometimes it will return no data
            else {
                return Collections.emptyList(); // Empty array list
            }

        }
    }

}