package com.example.foodforthoughtapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.foodforthoughtapp.model.contributions.Contribution;
import com.example.foodforthoughtapp.model.contributions.ResourceContribution;
import com.example.foodforthoughtapp.model.contributions.VolunteerContribution;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContributionHistory extends AppCompatActivity {

    private final String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    private List<Contribution> contributions;
    private ComplexRecyclerViewAdapter list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contribution_history);
        contributions = new ArrayList<>();
        initUI();
        readData();
    }

    private void readData() {
        // TODO: sort list by date
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
                list.notifyDataSetChanged();
            });
        });
    }

    private void initUI() {
        RecyclerView recyclerView = findViewById(R.id.contributionsList);
        list = new ComplexRecyclerViewAdapter(contributions);
        recyclerView.setAdapter(list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}