package com.example.foodforthoughtapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.foodforthoughtapp.model.contributions.ResourceContribution;
import com.example.foodforthoughtapp.model.contributions.VolunteerContribution;
import com.example.foodforthoughtapp.model.pantry.PantryHours;
import com.example.foodforthoughtapp.model.pantry.PantryInfo;
import com.example.foodforthoughtapp.model.pantry.Resource;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContributeActivity extends AppCompatActivity {
    List conResourceList = new ArrayList();
    ContributeAdapter ca;

    private ListView resourceConListView;
    private CardView contributeCard;
    private PantryInfo pantry;
    private String pantryID;
    private DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
    private static final String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contribute_page);
        findViewById(R.id.mainLayout).setVisibility(View.INVISIBLE);
        //back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        pantryID = extras.getString("Food Pantry");

        //have to get the arrayList of resources in the specific pantry
        dbref.child("pantries").child(pantryID).get().addOnCompleteListener(task -> {
            PantryInfo pantry = task.getResult().getValue(PantryInfo.class);
            this.pantry = pantry;
            populateView(pantry);
            findViewById(R.id.mainLayout).setVisibility(View.VISIBLE);
        });

        Button submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(view -> {
            submitContribution();
            Intent intent = new Intent(this, SubmitActivity.class);
            startActivity(intent);
            this.finish();
        });
    }

    // submits a user's contribution to the database
    private void submitContribution() {
        List<VolunteerContribution> volunteering = getVolunteerHours();
        if (volunteering != null) {
            for (VolunteerContribution contribution : volunteering) {
                dbref.child("contributions").child(userID).child("volunteerHistory").push().setValue(contribution);
            }
            Log.d("ContributeActivity", "Submitted "
                    + volunteering.size() + " volunteer opportunities for user "
                    + userID);
        }
//        ResourceContribution donations = getDonation();
        Pair<ResourceContribution, Map<Resource, Integer>> donations = getDonation();
        if (donations != null) {
            // TODO: change the count of resources needed in the pantries object for each resources donated
            dbref.child("contributions").child(userID).child("resourceHistory").push().setValue(donations);
            Log.d("ContributeActivity", "Submitted donation contribution for user "
                    + userID);
            submitContributionToPantry(donations.first, donations.second);
        }
    }

    private void submitContributionToPantry(ResourceContribution contribution, Map<Resource, Integer> oldCounts) {
        for (Resource res : contribution.resources) {
            int oldCount = oldCounts.get(res);
            int newCount = oldCount - res.count;
            dbref.child("pantries").child(pantryID).child("resources").get().addOnCompleteListener(task -> {

            });
        }
    }

    private List<VolunteerContribution> getVolunteerHours() {
        List<VolunteerContribution> volunteering = new ArrayList<>();
        for (String day : pantry.hours.keySet()) {
            Pair<Spinner, Spinner> times = getTimeSpinners(day);
            String start = times.first.getSelectedItem().toString();
            String end = times.second.getSelectedItem().toString();
            if (!start.equals("None") && !end.equals("None")) {
                PantryHours hours = new PantryHours(start, end);
                VolunteerContribution cur = new VolunteerContribution("05/05/2022", pantryID, hours);
                volunteering.add(cur);
            }
        }
        return !volunteering.isEmpty() ? volunteering : null;
    }

    private Pair<Spinner, Spinner> getTimeSpinners(String day) {
        switch (day) {
            case "Monday":
                return new Pair<>(findViewById(R.id.strtSpinnerM), findViewById(R.id.endSpinnerM));
            case "Tuesday":
                return new Pair<>(findViewById(R.id.strtSpinnerT), findViewById(R.id.endSpinnerT));
            case "Wednesday":
                return new Pair<>(findViewById(R.id.strtSpinnerW), findViewById(R.id.endSpinnerW));
            case "Thursday":
                return new Pair<>(findViewById(R.id.strtSpinnerTh), findViewById(R.id.endSpinnerTh));
            case "Friday":
                return new Pair<>(findViewById(R.id.strtSpinnerF), findViewById(R.id.endSpinnerF));
            case "Saturday":
                return new Pair<>(findViewById(R.id.strtSpinnerSa), findViewById(R.id.endSpinnerSa));
            case "Sunday":
                return new Pair<>(findViewById(R.id.strtSpinnerSu), findViewById(R.id.endSpinnerSu));
            default:
                return null;
        }
    }

    private Pair<ResourceContribution, Map<Resource, Integer>> getDonation() {
        List<Resource> resources = new ArrayList<>();
        Map<Resource, Integer> oldCounts = new HashMap<>();
        for (int i = 0; i < resourceConListView.getChildCount(); i++) {
            View cur = resourceConListView.getChildAt(i);
            CheckBox resourceCheck = (CheckBox) cur.findViewById(R.id.resCheckBox);
            EditText resourceCount = (EditText) cur.findViewById(R.id.editCount);
            if (resourceCheck.isChecked()) {
                if (!resourceCount.getText().toString().isEmpty()) {
                    Resource res = new Resource(resourceCheck.getText().toString(),
                            Integer.parseInt(resourceCount.getText().toString()));
                    resources.add(res);
                    int prevResourceCount = Integer.parseInt(((EditText) cur.findViewById(R.id.editCount)).getHint().toString());
                    oldCounts.put(res, prevResourceCount);
                }
            }
        }
        if (resources.isEmpty()) {
            return null;
        }
        // TODO: hardcode date for now
        String date = "01/04/2020";
        return new Pair<>(new ResourceContribution(date, pantryID, resources), oldCounts);
    }

    private void populateView(PantryInfo pantry) {
        setTitle(pantry.getName());
        populateHours(pantry);
        conResourceList = pantry.getResources();

        //connect the resource list with the card view
        resourceConListView = (ListView) findViewById(R.id.conResourcesNeeded);
        contributeCard = (CardView) findViewById(R.id.contribute_card_view);

        ca = new ContributeAdapter(this, R.layout.resource_contribute_layout, conResourceList);

        //setAdapter to the arrayList that we need to use
        //connect listview to the array adapter
        resourceConListView .setAdapter(ca);
        registerForContextMenu(resourceConListView);
        ca.notifyDataSetChanged();
    }

    private void populateHours(PantryInfo pantry) {
        Map<String, PantryHours> hours = pantry.hours;
        LinearLayout days = findViewById(R.id.linearLayout);
        if (hours.containsKey("Monday")) {
            Spinner mstartSpinner = (Spinner) findViewById(R.id.strtSpinnerM);
            Spinner mendSpinner = (Spinner) findViewById(R.id.endSpinnerM);
            // get array for selecting times based on the hours
            PantryHours curHours = hours.get("Monday");
            ((TextView) findViewById(R.id.mTimeTextView)).setText(curHours.toString());
            List<CharSequence> times = getHoursArray(curHours);
            setSpinner(mstartSpinner, times);
            setSpinner(mendSpinner, times);
        } else {
            days.removeView(findViewById(R.id.mondayLayout));
        }
        if (hours.containsKey("Tuesday")) {
            Spinner tstartSpinner = (Spinner) findViewById(R.id.strtSpinnerT);
            Spinner tendSpinner = (Spinner) findViewById(R.id.endSpinnerT);
            // get array for selecting times based on the hours
            PantryHours curHours = hours.get("Tuesday");
            ((TextView) findViewById(R.id.tuesTimeTextView)).setText(curHours.toString());
            List<CharSequence> times = getHoursArray(curHours);
            setSpinner(tstartSpinner, times);
            setSpinner(tendSpinner, times);
        } else {
            days.removeView(findViewById(R.id.tuesdayLayout));
        }
        if (hours.containsKey("Wednesday")) {
            Spinner wstartSpinner = (Spinner) findViewById(R.id.strtSpinnerW);
            Spinner wendSpinner = (Spinner) findViewById(R.id.endSpinnerW);
            // get array for selecting times based on the hours
            PantryHours curHours = hours.get("Wednesday");
            ((TextView) findViewById(R.id.weTimeTextView)).setText(curHours.toString());
            List<CharSequence> times = getHoursArray(curHours);
            setSpinner(wstartSpinner, times);
            setSpinner(wendSpinner, times);
        } else {
            days.removeView(findViewById(R.id.wednesdayLayout));
        }
        if (hours.containsKey("Thursday")) {
            Spinner thstartSpinner = (Spinner) findViewById(R.id.strtSpinnerTh);
            Spinner thendSpinner = (Spinner) findViewById(R.id.endSpinnerTh);
            // get array for selecting times based on the hours
            PantryHours curHours = hours.get("Thursday");
            ((TextView) findViewById(R.id.thursTimeTextView)).setText(curHours.toString());
            List<CharSequence> times = getHoursArray(curHours);
            setSpinner(thstartSpinner, times);
            setSpinner(thendSpinner, times);
        } else {
            days.removeView(findViewById(R.id.thursdayLayout));
        }
        if (hours.containsKey("Friday")) {
            Spinner fstartSpinner = (Spinner) findViewById(R.id.strtSpinnerF);
            Spinner fendSpinner = (Spinner) findViewById(R.id.endSpinnerF);
            // get array for selecting times based on the hours
            PantryHours curHours = hours.get("Friday");
            ((TextView) findViewById(R.id.frTimeTextView)).setText(curHours.toString());
            List<CharSequence> times = getHoursArray(curHours);
            setSpinner(fstartSpinner, times);
            setSpinner(fendSpinner, times);
        } else {
            days.removeView(findViewById(R.id.fridayLayout));
        }
        if (hours.containsKey("Saturday")) {
            Spinner sastartSpinner = (Spinner) findViewById(R.id.strtSpinnerSa);
            Spinner saendSpinner = (Spinner) findViewById(R.id.endSpinnerSa);
            // get array for selecting times based on the hours
            PantryHours curHours = hours.get("Saturday");
            ((TextView) findViewById(R.id.satTimeTextView)).setText(curHours.toString());
            List<CharSequence> times = getHoursArray(curHours);
            setSpinner(sastartSpinner, times);
            setSpinner(saendSpinner, times);
        } else {
            days.removeView(findViewById(R.id.saturdayLayout));
        }
        if (hours.containsKey("Sunday")) {
            Spinner sustartSpinner = (Spinner) findViewById(R.id.strtSpinnerSu);
            Spinner suendSpinner = (Spinner) findViewById(R.id.endSpinnerSu);
            // get array for selecting times based on the hours
            PantryHours curHours = hours.get("Sunday");
            ((TextView) findViewById(R.id.sunTimeTextView)).setText(curHours.toString());
            List<CharSequence> times = getHoursArray(curHours);
            setSpinner(sustartSpinner, times);
            setSpinner(suendSpinner, times);
        } else {
            days.removeView(findViewById(R.id.sundayLayout));
        }
    }

    @SuppressLint("NewApi")
    private List<CharSequence> getHoursArray(PantryHours monday) {
        LocalTime start, end;
        try {
            start = LocalTime.parse(monday.startTime);
            end = LocalTime.parse(monday.endTime);
            int intervals = (int) start.until(end, ChronoUnit.MINUTES) / 30;
            List<CharSequence> times = new ArrayList<>(Collections.singletonList("None"));
            for (int i = 0 ; i < intervals + 1; i++) {
                times.add(start.plusMinutes(i * 30).toString());
            }
            return times;
        } catch (Exception e) {
            Log.d("ContributeActivity", e.toString());
        }
        return null;
    }

    public void setSpinner(Spinner spinner, List<CharSequence> times) {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, times);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), PantryDetail.class);
        startActivityForResult(myIntent, 0);
        return true;
    }
}
