package com.example.foodforthoughtapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.foodforthoughtapp.model.contributions.ResourceContribution;
import com.example.foodforthoughtapp.model.contributions.VolunteerContribution;
import com.example.foodforthoughtapp.model.pantry.PantryInfo;
import com.example.foodforthoughtapp.model.pantry.Resource;
import com.example.foodforthoughtapp.model.pantry.VolDateTime;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pranavpandey.android.dynamic.toasts.DynamicToast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ContributeActivity extends AppCompatActivity {
    private List<Resource> conResourceList = new ArrayList<>();
    private ContributeAdapter ca;
    private ListView resourceConListView;
    private CardView contributeCard;

    protected List<VolDateTime> conVolunteerList = new ArrayList<>();
    private VolunteerDateAdapter va;
    private ListView conDateHoursView;
    private CardView datePickerCard;

    private PantryInfo pantry;
    private String pantryID;
    private DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
    private static final String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_contribute_page);
        //back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        pantryID = extras.getString("Food Pantry");

        //have to get the arrayList of resources in the specific pantry
        dbref.child("pantries").child(pantryID).get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                Log.d("ContributeActivity", task.getException().toString());
            }
            PantryInfo pantry = task.getResult().getValue(PantryInfo.class);
            this.pantry = pantry;
            populateView(pantry);

            initVolHours(pantry);
            ImageButton addDay = (ImageButton) findViewById(R.id.add_day);
            addDay.setOnClickListener(view -> {
                if(va.getCount() == 0 || va.getItem(va.getCount() - 1).isComplete())
                    addDay(pantry);
                else {
                    Toast toast = DynamicToast.makeError(getApplicationContext(), getString(R.string.complete_hours_first), Toast.LENGTH_SHORT);
                    toast.show();
                }
            });
            //findViewById(R.id.mainLayout2).setVisibility(View.VISIBLE);
        });

        Button submitButton = (Button) findViewById(R.id.submitButtonNew);
        submitButton.setOnClickListener(view -> {
            if(submitContribution()) {
                Intent intent = new Intent(this, SubmitActivity.class);
                startActivity(intent);
                this.finish();
            } else {
                Toast toast = DynamicToast.makeError(getApplicationContext(), getString(R.string.empty_fields), Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    private void addDay(PantryInfo pantry) {
        conVolunteerList.add(new VolDateTime("", "", ""));
        va.notifyDataSetChanged();
    }

    private void initVolHours(PantryInfo pantry) {
        Log.d("ContributeActivity", "Initializing volunteer card view");

        //connect the resource list with the card view
        conDateHoursView = (ListView) findViewById(R.id.conDatesHours);
        datePickerCard = (CardView) findViewById(R.id.contribute_date_picker_view);
        va = new VolunteerDateAdapter(this, R.layout.volunteer_date_picker, conVolunteerList, pantry);

        //setAdapter to the arrayList that we need to use
        //connect listview to the array adapter
        conDateHoursView.setAdapter(va);
        // registerForContextMenu(resourceConListView);
        va.notifyDataSetChanged();
    }

    private void populateView(PantryInfo pantry) {
        Log.d("ContributeActivity", "Populating view with pantry information");
        setTitle(pantry.getName());
        //populateHours(pantry);
        conResourceList = pantry.getResources();

        //connect the resource list with the card view
        resourceConListView = (ListView) findViewById(R.id.conResourcesNeededNew);
        contributeCard = (CardView) findViewById(R.id.contribute_card_view);

        ca = new ContributeAdapter(this, R.layout.resource_contribute_layout, conResourceList);

        //setAdapter to the arrayList that we need to use
        //connect listview to the array adapter
        resourceConListView.setAdapter(ca);
        // registerForContextMenu(resourceConListView);
        ca.notifyDataSetChanged();
    }

    // submits a user's contribution to the database
    private boolean submitContribution() {
        List<VolunteerContribution> volunteering = getVolunteerHours();
        if (volunteering == null) {
            // user did not fill out all volunteer slots
            return false;
        }
        for (VolunteerContribution contribution : volunteering) {
            dbref.child("contributions").child(userID).child("volunteerHistory").push().setValue(contribution);
        }
        Log.d("ContributeActivity", "Submitted "
                + volunteering.size() + " volunteer opportunities for user "
                + userID);


        Pair<ResourceContribution, Set<String>> donations = getDonation();
        if (donations != null) {
            dbref.child("contributions").child(userID).child("resourceHistory").push().setValue(donations.first);
            Log.d("ContributeActivity", "Submitted donation contribution for user "
                    + userID);
            submitContributionToPantry(donations.first, donations.second);
        }
        return !volunteering.isEmpty() || donations != null;
    }

    private Pair<ResourceContribution, Set<String>> getDonation() {
        List<Resource> resources = new ArrayList<>();
        Set<String> donated = new HashSet<>();
        for (int i = 0; i < resourceConListView.getChildCount(); i++) {
            View cur = resourceConListView.getChildAt(i);
            CheckBox resourceCheck = (CheckBox) cur.findViewById(R.id.resCheckBox);
            EditText resourceCount = (EditText) cur.findViewById(R.id.editCount);
            if (resourceCheck.isChecked()) {
                if (!resourceCount.getText().toString().isEmpty()) {
                    Resource res = new Resource(resourceCheck.getText().toString(),
                            Integer.parseInt(resourceCount.getText().toString()));
                    resources.add(res);
                    donated.add(res.resourceName);
                } else {
                    Toast toast = DynamicToast.makeError(getApplicationContext(), getString(R.string.invalid_resource_count), Toast.LENGTH_SHORT);
                    toast.show();
                    return null;
                }
            }
        }
        if (resources.isEmpty()) {
            return null;
        }
//        try {
//            // TODO: set date to current date for now
//            String date = new SimpleDateFormat("MM/dd/yyyy").parse(new Date().toString())
//                    .toString();
//            return new Pair<>(new ResourceContribution
//                    (date, pantryID, resources), donated);
//        } catch (ParseException ex) {
//            ex.printStackTrace();
//        }
        String date = "04/27/2022";
        return new Pair<>(new ResourceContribution
                (date, pantryID, resources), donated);
    }

    private void submitContributionToPantry(ResourceContribution contribution, Set<String> changed) {
        List<Resource> updatedResources = new ArrayList<>();
        dbref.child("pantries").child(pantryID).child("resources").get().addOnCompleteListener(task -> {
            for (DataSnapshot resource : task.getResult().getChildren()) {
                Resource cur = resource.getValue(Resource.class);
                if (!changed.contains(cur.resourceName)) {
                    updatedResources.add(cur);
                } else {
                    int remove = 0;
                    for (Resource r : contribution.resources) {
                        if (r.resourceName.equals(cur.resourceName)) {
                            remove = r.count;
                            break;
                        }
                    }
                    cur.count -= remove;
                    if (cur.count > 0) {
                        updatedResources.add(cur);
                    }
                }
            }
            // update the Resources list in the database
            Log.d("ContributeActivity", "Updating Pantry " + pantryID +
                    "'s resources in database");
            dbref.child("pantries").child(pantryID).child("resources").setValue(updatedResources);
        });
    }

    private List<VolunteerContribution> getVolunteerHours() {
        List<VolunteerContribution> volunteering = new ArrayList<>();
        for (int i = 0; i < va.getCount(); i++) {
            VolDateTime cur = va.getItem(i);
            if (!cur.time.isValid()) {
                Toast toast = DynamicToast.makeError(getApplicationContext(), getString(R.string.invalid_hours), Toast.LENGTH_SHORT);
                toast.show();
                return null;
            }
            volunteering.add(new VolunteerContribution(cur.date, pantryID, cur.time));
        }
        return volunteering;
    }
}