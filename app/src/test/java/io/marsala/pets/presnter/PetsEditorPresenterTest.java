package io.marsala.pets.presnter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.Arrays;
import java.util.List;

import io.marsala.pets.model.models.Pet;
import io.marsala.pets.model.repositories.PetsRepository;
import io.marsala.pets.view.PetsEditorView;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by AHMED HAMDI ELSHAHAWI on 7/23/2017.
 * HIT ME @TenFeetShuffler
 */

public class PetsEditorPresenterTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    PetsEditorView editorView;
    @Mock
    PetsRepository petsRepository;

    Long ANY_ID = Long.valueOf(58464646);
    private final List<Pet> ONE_PET = Arrays.asList(new Pet(323222, "Spotchy", "Kalb Balady", "Male", "20"));

    private PetsEditorPresenter editorPresenter;

    @Before
    public void setUp() {
        editorPresenter = new PetsEditorPresenter(editorView, petsRepository);
    }

    @Test
    public void shouldHandleDatabaseError() {
        // Tell the mock to blow up, When there is an error
        when(petsRepository.getPets(null, ANY_ID)).thenThrow(new RuntimeException("boom"));

        editorPresenter.editExistentPet(ANY_ID);

        verify(editorView).displayStorageError();
    }

    @Test
    public void shouldCleanFieldsOnAddingNewPet() {
        editorPresenter.startNewPet();

        verify(editorView).displayEmptyFields();
    }




}