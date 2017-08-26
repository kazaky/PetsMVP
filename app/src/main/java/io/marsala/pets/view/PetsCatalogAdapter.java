package io.marsala.pets.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import io.marsala.pets.model.models.Pet;
import io.marsala.pets.R;

import static io.marsala.pets.model.models.Constants.PET_ID;

/**
 * Created by AHMED HAMDI ELSHAHAWI on 6/25/2017.
 * HIT ME @TenFeetShuffler
 */

public class PetsCatalogAdapter extends RecyclerView.Adapter<PetsCatalogAdapter.PetViewHolder> {

    String TAG = getClass().getSimpleName();
    Context context;
    private List<Pet> petsList;

    public PetsCatalogAdapter(Context context, List<Pet> petsList) {
        this.context = context;
        this.petsList = petsList;
    }

    public class PetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        TextView summary;

        public PetViewHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.name);
            summary = (TextView) v.findViewById(R.id.summary);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // Create new intent to go to {@link EditorActivity}
            Intent intent = new Intent(context, EditorActivity.class);

            Long id = petsList.get(getAdapterPosition()).getId();
            Log.e(TAG, "onClick: " + id);
            // Set the URI on the data field of the intent
            intent.putExtra(PET_ID, id);

            // Launch the {@link EditorActivity} to display the data for the current pet.
            context.startActivity(intent);

        }
    }

    @Override
    public PetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        PetViewHolder petViewHolder = new PetViewHolder(v);
        return petViewHolder;
    }

    @Override
    public void onBindViewHolder(PetViewHolder holder, int position) {

        // Read the pet attributes from the Cursor for the current pet
        String petName = petsList.get(position).getName();
        String petBreed = petsList.get(position).getBreed();

        // If the pet breed is empty string or null, then use some default text
        // that says "Unknown breed", so the TextView isn't blank.
        if (TextUtils.isEmpty(petBreed)) {
            petBreed = context.getString(R.string.unknown_breed);
        }

        // Update the TextViews with the attributes for the current pet
        holder.name.setText(petName);
        holder.summary.setText(petBreed);
    }

    @Override
    public int getItemCount() {
        return petsList.size();
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


}
