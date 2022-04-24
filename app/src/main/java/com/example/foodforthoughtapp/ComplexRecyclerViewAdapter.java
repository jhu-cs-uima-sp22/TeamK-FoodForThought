package com.example.foodforthoughtapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodforthoughtapp.model.contributions.Contribution;
import com.example.foodforthoughtapp.model.contributions.ResourceContribution;
import com.example.foodforthoughtapp.model.contributions.VolunteerContribution;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ComplexRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // view type encodings
    private static final int DONATION = 0;
    private static final int VOLUNTEERING = 1;
    private final String donatePrefix;
    private final String volunteerPrefix;
    private final List<Contribution> history;
    private final DatabaseReference db = FirebaseDatabase.getInstance().getReference();

    public ComplexRecyclerViewAdapter(List<Contribution> history, boolean showFutureOnly) {
        this.history = history;
        donatePrefix = showFutureOnly ? "Donating" : "Donated";
        volunteerPrefix = showFutureOnly ? "Volunteering" : "Volunteered";
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == DONATION) {
            View v = inflater.inflate(R.layout.contribution_donation_view, parent, false);
            return new DonationViewHolder(v);
        }
        View v = inflater.inflate(R.layout.contribution_volunteer_view, parent, false);
        return new VolunteerViewHolder(v);
    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == DONATION) {
            ResourceContribution data = (ResourceContribution) history.get(position);
            String pantryID = data.pantryID;
            db.child("pantries").child(pantryID).child("name").get().addOnCompleteListener(task -> {
                String pantryName = task.getResult().getValue(String.class);
                ((DonationViewHolder) holder).pantryName.setText(pantryName);
                ((DonationViewHolder) holder).resourcesList.setText(donatePrefix + data.toString());
            });
        } else if (holder.getItemViewType() == VOLUNTEERING) {
            VolunteerContribution data = (VolunteerContribution) history.get(position);
            String pantryID = data.pantryID;
            db.child("pantries").child(pantryID).child("name").get().addOnCompleteListener(task -> {
                String pantryName = task.getResult().getValue(String.class);
                ((VolunteerViewHolder) holder).pantryName.setText(pantryName);
                ((VolunteerViewHolder) holder).hoursDescription.setText(volunteerPrefix + data.toString());
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        Contribution contribution = history.get(position);
        return contribution.type.equals("RESOURCE") ? 0 : 1;
    }

    @Override
    public int getItemCount() {
        return history.size();
    }

    protected class DonationViewHolder extends RecyclerView.ViewHolder {
        protected final TextView pantryName;
        protected final TextView resourcesList;

        public DonationViewHolder(@NonNull View itemView) {
            super(itemView);
            pantryName = itemView.findViewById(R.id.pantryNameDonationCard);
            resourcesList = itemView.findViewById(R.id.resourcesList);
        }
    }

    protected class VolunteerViewHolder extends RecyclerView.ViewHolder {
        protected final TextView pantryName;
        protected final TextView hoursDescription;

        public VolunteerViewHolder(@NonNull View itemView) {
            super(itemView);
            pantryName = itemView.findViewById(R.id.pantryNameVolunteerCard);
            hoursDescription = itemView.findViewById(R.id.hoursDescription);
        }
    }
}
