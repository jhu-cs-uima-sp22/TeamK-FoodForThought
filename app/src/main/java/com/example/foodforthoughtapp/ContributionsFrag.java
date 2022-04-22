package com.example.foodforthoughtapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodforthoughtapp.model.contributions.Contribution;
import com.example.foodforthoughtapp.model.contributions.ResourceContribution;
import com.example.foodforthoughtapp.model.contributions.VolunteerContribution;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ContributionsFrag extends Fragment {

    private final String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    private List<Contribution> contributions;
    private ComplexRecyclerViewAdapter list;
    private Context context;
    private MainActivity main;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.frag_contributions, container, false);
        main = (MainActivity) getActivity();
        context = main.getApplicationContext();
        main.getSupportActionBar().setTitle("My Contributions");
        contributions = new ArrayList<>();
        initUI();
        readData();
        return view;
    }

    private void readData() {
        Log.d("ContributionHistory", "Retrieving contributions history for user " + user);
        db.child("contributions").child(user).child("resourceHistory").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                // throw exception and return
                Log.d("ContributionHistory", task.getException().toString());
                return;
            }
            DataSnapshot result = task.getResult();
            Log.d("ContributionHistory", "Successfully read " + result.getChildrenCount()
                    + " resource contributions");
            for (DataSnapshot child : result.getChildren()) {
                ResourceContribution cur = child.getValue(ResourceContribution.class);
                cur.type = "RESOURCE";
                contributions.add(cur);
            }
            db.child("contributions").child(user).child("volunteerHistory").get().addOnCompleteListener(task1 -> {
                if (!task1.isSuccessful()) {
                    // throw exception and return
                    Log.d("ContributionHistory", task1.getException().toString());
                    return;
                }
                DataSnapshot result1 = task1.getResult();
                Log.d("ContributionHistory", "Successfully read " + result1.getChildrenCount()
                        + " volunteer contributions");
                for (DataSnapshot child : result1.getChildren()) {
                    VolunteerContribution cur = child.getValue(VolunteerContribution.class);
                    cur.type = "VOLUNTEER";
                    contributions.add(cur);
                }
                Collections.sort(contributions, (c, other) -> {
                    // sort by date
                    String date1 = c.type.equals("RESOURCE") ? ((ResourceContribution) c).date : ((VolunteerContribution) c).date;
                    String date2 = other.type.equals("RESOURCE") ? ((ResourceContribution) other).date : ((VolunteerContribution) other).date;
                    Date cDate = null;
                    Date otherDate = null;
                    try {
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                        cDate = dateFormat.parse(date1);
                        otherDate = dateFormat.parse(date2);
                    } catch (ParseException ex) {
                        Log.d("ContributionHistory", ex.toString());
                    }
                    return otherDate.compareTo(cDate);
                });
                list.notifyDataSetChanged();
            });
        });
    }

    private void initUI() {
        RecyclerView recyclerView = view.findViewById(R.id.contributionsList);
        list = new ComplexRecyclerViewAdapter(contributions);
        recyclerView.setAdapter(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }
}