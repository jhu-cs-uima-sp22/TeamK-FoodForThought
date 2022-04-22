package com.example.foodforthoughtapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.foodforthoughtapp.model.pantry.PantryHours;
import com.example.foodforthoughtapp.model.pantry.PantryInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ContributeActivity extends AppCompatActivity {
    List conResourceList = new ArrayList();
    ContributeAdapter ca;

    private ListView resourceConListView;
    private CardView contributeCard;

    DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contribute_page);
        //back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        String pantryKey = extras.getString("Food Pantry");

        //have to get the arrayList of resources in the specific pantry
        dbref.child("pantries").child(pantryKey).get().addOnCompleteListener(task -> {
            PantryInfo pantry = task.getResult().getValue(PantryInfo.class);
            populateView(pantry);
        });

        Button submitButton = (Button) findViewById(R.id.submitButton);
        submitButton.setOnClickListener(view -> {
            // TODO: update user's contributions in Firebase
            submitContribution();
            Intent intent = new Intent(this, SubmitActivity.class);
            startActivity(intent);
            this.finish();
        });
    }

    // submits a user's contribution to the database
    private void submitContribution() {
        // TODO
    }

    private void populateView(PantryInfo pantry) {
        populateHours(pantry);
        setTitle(pantry.getName());
        conResourceList = pantry.getResources();

        //connect the resource list with the card view
        // View myview = inflater.inflate(R.layout.contribute_page, container, false);
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
        if (hours.containsKey("Monday")) {
            Spinner mstartSpinner = (Spinner) findViewById(R.id.strtSpinnerM);
            Spinner mendSpinner = (Spinner) findViewById(R.id.endSpinnerM);
            // get array for selecting times based on the hours
            List<CharSequence> times = getHoursArray(hours.get("Monday"));
            setSpinner(mstartSpinner, times);
            setSpinner(mendSpinner, times);
        }
        if (hours.containsKey("Tuesday")) {
            Spinner tstartSpinner = (Spinner) findViewById(R.id.strtSpinnerT);
            Spinner tendSpinner = (Spinner) findViewById(R.id.endSpinnerT);
            // get array for selecting times based on the hours
            List<CharSequence> times = getHoursArray(hours.get("Monday"));
            setSpinner(tstartSpinner, times);
            setSpinner(tendSpinner, times);
        }
        if (hours.containsKey("Wednesday")) {
            Spinner wstartSpinner = (Spinner) findViewById(R.id.strtSpinnerW);
            Spinner wendSpinner = (Spinner) findViewById(R.id.endSpinnerW);
            // get array for selecting times based on the hours
            List<CharSequence> times = getHoursArray(hours.get("Monday"));
            setSpinner(wstartSpinner, times);
            setSpinner(wendSpinner, times);
        }
        if (hours.containsKey("Thursday")) {
            Spinner thstartSpinner = (Spinner) findViewById(R.id.strtSpinnerTh);
            Spinner thendSpinner = (Spinner) findViewById(R.id.endSpinnerTh);
            // get array for selecting times based on the hours
            List<CharSequence> times = getHoursArray(hours.get("Monday"));
            setSpinner(thstartSpinner, times);
            setSpinner(thendSpinner, times);
        }
        if (hours.containsKey("Friday")) {
            Spinner fstartSpinner = (Spinner) findViewById(R.id.strtSpinnerF);
            Spinner fendSpinner = (Spinner) findViewById(R.id.endSpinnerF);
            // get array for selecting times based on the hours
            List<CharSequence> times = getHoursArray(hours.get("Monday"));
            setSpinner(fstartSpinner, times);
            setSpinner(fendSpinner, times);
        }
        if (hours.containsKey("Saturday")) {
            Spinner sastartSpinner = (Spinner) findViewById(R.id.strtSpinnerSa);
            Spinner saendSpinner = (Spinner) findViewById(R.id.endSpinnerSa);
            // get array for selecting times based on the hours
            List<CharSequence> times = getHoursArray(hours.get("Monday"));
            setSpinner(sastartSpinner, times);
            setSpinner(saendSpinner, times);
        }
        if (hours.containsKey("Sunday")) {
            Spinner sustartSpinner = (Spinner) findViewById(R.id.strtSpinnerSu);
            Spinner suendSpinner = (Spinner) findViewById(R.id.endSpinnerSu);
            // get array for selecting times based on the hours
            List<CharSequence> times = getHoursArray(hours.get("Monday"));
            setSpinner(sustartSpinner, times);
            setSpinner(suendSpinner, times);
        }
    }

    @SuppressLint("NewApi")
    private List<CharSequence> getHoursArray(PantryHours monday) {
        LocalTime start, end;
        try {
            start = LocalTime.parse(monday.startTime);
            end = LocalTime.parse(monday.endTime);
            int intervals = (int) start.until(end, ChronoUnit.MINUTES) / 30;
            List<CharSequence> times = new ArrayList<>();
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
