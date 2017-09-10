package io.marsala.pets;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import io.marsala.pets.model.models.Pet;
import io.marsala.pets.model.repositories.PetsRepository;
import io.marsala.pets.presnter.PetsPresenter;
import io.marsala.pets.view.PetsCatalogView;
import io.marsala.pets.view.PetsEditorView;

import static java.util.Collections.EMPTY_LIST;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by AHMED HAMDI ELSHAHAWI on 7/23/2017.
 * HIT ME @TenFeetShuffler
 */

@RunWith(MockitoJUnitRunner.class)
public class PetsPresenterTest {

    @Mock
    PetsCatalogView catalogView;
    @Mock
    PetsEditorView editorView;
    @Mock
    PetsRepository petsRepository;

    private final List<Pet> MANY_PETS = Arrays.asList(new Pet(), new Pet(), new Pet());

    PetsPresenter petsPresenter;

    @Before
    public void setUp() {
        petsPresenter = new PetsPresenter(catalogView, petsRepository);
    }

    @Test
    public void shouldPassPetsDatabaseToView() {
        // GIVEN
        when(petsRepository.getPets(null, -1)).thenReturn(MANY_PETS);

        // WHEN
        petsPresenter.loadPets();

        // THEN
        verify(catalogView).displayPets(MANY_PETS);
    }


    @Test
    public void shouldHandleNoPetsFound() {
        when(petsRepository.getPets(null, -1)).thenReturn(EMPTY_LIST);

        petsPresenter.loadPets();

        verify(catalogView).displayNoPets();
    }

}