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

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.List;

import io.marsala.pets.model.database.PetsRepositoryDatabase;
import io.marsala.pets.model.models.Pet;
import io.marsala.pets.presnter.PetsCatalogPresenter;
import io.marsala.pets.R;
import io.realm.Realm;

/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity
        implements PetsCatalogView {


    /**
     * Adapter for the ListView
     */
    PetsCatalogAdapter catalogAdapter;
    private PetsCatalogPresenter presenter;
    private Realm realm;
    View emptyView;
    private RecyclerView petListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        realm = Realm.getDefaultInstance();

        presenter = new PetsCatalogPresenter(this, new PetsRepositoryDatabase(realm));

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        // Find the ListView which will be populated with the pet data
        petListView = (RecyclerView) findViewById(R.id.list);
        emptyView = findViewById(R.id.empty_view);

    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.loadPets();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                presenter.deleteAllPets();
                presenter.loadPets();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void displayPets(List<Pet> petList) {
        emptyView.setVisibility(View.GONE);
        petListView.setVisibility(View.VISIBLE);

        catalogAdapter = new PetsCatalogAdapter(this, petList);
        petListView.setAdapter(catalogAdapter);
    }

    @Override
    public void displayNoPets() {
        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        petListView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void displayError(Exception e) {
        Toast.makeText(this, "Error accessing data "+e, Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onStop() {
        super.onStop();
        realm.close();
    }
}
