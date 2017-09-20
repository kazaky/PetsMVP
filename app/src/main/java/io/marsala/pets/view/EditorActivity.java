/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.marsala.pets.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTouch;
import io.marsala.pets.R;
import io.marsala.pets.model.database.PetsRepositoryDatabase;
import io.marsala.pets.model.models.Pet;
import io.marsala.pets.presnter.PetsEditorPresenter;
import io.realm.Realm;

import static io.marsala.pets.model.models.Constants.GENDER_FEMALE;
import static io.marsala.pets.model.models.Constants.GENDER_MALE;
import static io.marsala.pets.model.models.Constants.GENDER_UNKNOWN;
import static io.marsala.pets.model.models.Constants.PET_ID;

/**
 * Allows user to create a new pet or edit an existing one.
 */
public class EditorActivity extends AppCompatActivity implements PetsEditorView {

   @BindView(R.id.edit_pet_name)EditText mNameEditText;
   @BindView(R.id.edit_pet_breed)EditText mBreedEditText;
   @BindView(R.id.edit_pet_weight)EditText mWeightEditText;
   @BindView(R.id.spinner_gender)Spinner mGenderSpinner;

    /**
     * Boolean flag that keeps track of whether the pet has been edited (true) or not (false)
     */
    private boolean mPetHasChanged = false;

    /**
     * OnTouchListener that listens for any user touches on a View, implying that they are modifying
     * the view, and we change the mPetHasChanged boolean to true.
     */
    // Setup OnTouchListeners on all the input fields, so we can determine if the user
    // has touched or modified them. This will let us know if there are unsaved changes
    // or not, if the user tries to leave the editor without saving.
    @OnTouch({ R.id.edit_pet_name, R.id.edit_pet_breed, R.id.edit_pet_weight,R.id.spinner_gender })
    public boolean changeEditState(){
        mPetHasChanged = true;
        return false;
    }

    private PetsEditorPresenter presenter;
    private Realm realm;
    private String mGender = GENDER_UNKNOWN;
    long idCurrentPet = 0;
    private boolean isSaved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        ButterKnife.bind(this);

        realm = Realm.getDefaultInstance();
        presenter = new PetsEditorPresenter(this, new PetsRepositoryDatabase(realm));

        // Examine the intent that was used to launch this activity,
        // in order to figure out if we're creating a new pet or editing an existing one.
        Intent intent = getIntent();
        idCurrentPet = intent.getLongExtra(PET_ID, 0);

        setupSpinner();

        presenter.startNewOrEdit(idCurrentPet);

    }

    /**
     * Setup the dropdown spinner that allows the user to select the gender of the pet.
     */
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mGenderSpinner.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.gender_male))) {
                        mGender = GENDER_MALE;
                    } else if (selection.equals(getString(R.string.gender_female))) {
                        mGender = GENDER_FEMALE;
                    } else {
                        mGender = GENDER_UNKNOWN;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGender = GENDER_UNKNOWN;
            }
        });
    }

    /**
     * Get user input from editor and save pet into database.
     */
    private boolean savePet() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String nameString = mNameEditText.getText().toString().trim();
        String breedString = mBreedEditText.getText().toString().trim();
        String weightString = mWeightEditText.getText().toString().trim();

        // Check if this is supposed to be a new pet
        // and check if all the fields in the editor are blank
        if (idCurrentPet == 0 &&
                TextUtils.isEmpty(nameString) && TextUtils.isEmpty(breedString) &&
                TextUtils.isEmpty(weightString) && mGender == GENDER_UNKNOWN) {
            // Since no fields were modified, we can return early without creating a new pet.
            // No need to create ContentValues and no need to do any ContentProvider operations.
            return true;
        }

        // If the weight is not provided by the user, don't try to parse the string into an
        // integer value. Use 0 by default.
        int weight = 0;
        if (!TextUtils.isEmpty(weightString)) {
            weight = Integer.parseInt(weightString);
        }


        try {
            isSaved = presenter.savePetEditor(idCurrentPet, nameString, breedString, mGender, String.valueOf(weight));
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, e.getMessage(),
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        return isSaved;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    /**
     * This method is called after invalidateOptionsMenu(), so that the
     * menu can be updated (some menu items can be hidden or made visible).
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new pet, hide the "Delete" menu item.
        if (idCurrentPet == 0) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save pet to database
                if (savePet() == true) {
                    finish(); // Exit activity
                }
                break;

            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Pop up confirmation dialog for deletion
                presenter.promptDeletion();
                break;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method is called when the back button is pressed.
     */
    @Override
    public void onBackPressed() {
        // If the pet hasn't changed, continue with handling back button press
        if (!mPetHasChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }


    /**
     * Show a dialog that warns the user there are unsaved changes that will be lost
     * if they continue leaving the editor.
     *
     * @param discardButtonClickListener is the click listener for what to do when
     *                                   the user confirms they want to discard their changes
     */
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    /**
     * Prompt the user to confirm that they want to delete this pet.
     */
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                presenter.deleteCurrentPet(idCurrentPet);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    @Override
    public void displayExistentPet(Pet pet) {

        // Otherwise this is an existing pet, so change app bar to say "Edit Pet"
        setTitle(getString(R.string.editor_activity_title_edit_pet));

        // Display the current values in the editor
        String name = pet.getName();
        String breed = pet.getBreed();
        String gender = pet.getGender();
        String weight = pet.getWeight();

        // Update the views on the screen with the values from the database
        mNameEditText.setText(name);
        mBreedEditText.setText(breed);
        mWeightEditText.setText(weight);

        // Gender is a dropdown spinner, so map the constant value from the database
        // into one of the dropdown options (0 is Unknown, 1 is Male, 2 is Female).
        // Then call setSelection() so that option is displayed on screen as the current selection.
        switch (gender) {
            case GENDER_MALE:
                mGenderSpinner.setSelection(1);
                break;
            case GENDER_FEMALE:
                mGenderSpinner.setSelection(2);
                break;
            default:
                mGenderSpinner.setSelection(0);
                break;
        }
    }

    @Override
    public void displayStorageError() {
        Toast.makeText(this, "Error editing this pet in database", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displayDeletePrompt() {
        showDeleteConfirmationDialog();


    }  @Override
    public void displayPetDeleted() {
        Toast.makeText(this, getString(R.string.editor_delete_pet_successful), Toast.LENGTH_SHORT).show();

        // Close the activity
        finish();
    }

    @Override
    public void displayDeletionError() {
        // If no rows were deleted, then there was an error with the delete.
        Toast.makeText(this, getString(R.string.editor_delete_pet_failed),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void displayEmptyFields() {
        // creating a new pet.
        // This is a new pet, so change the app bar to say "Add a Pet"
        setTitle(getString(R.string.editor_activity_title_new_pet));

        // Invalidate the options menu, so the "Delete" menu option can be hidden.
        // (It doesn't make sense to delete a pet that hasn't been created yet.)
        invalidateOptionsMenu();

        // If the loader is invalidated, clear out all the data from the input fields.
        mNameEditText.setText("");
        mBreedEditText.setText("");
        mWeightEditText.setText("");
        mGenderSpinner.setSelection(0); // Select "Unknown" gender
    }

}