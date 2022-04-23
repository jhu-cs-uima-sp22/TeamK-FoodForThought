package com.example.foodforthoughtapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.foodforthoughtapp.model.pantry.PantryHours;
import com.example.foodforthoughtapp.model.pantry.PantryInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PantryDetail extends AppCompatActivity {
    List resourceList = new ArrayList();
    ResourceAdapter ra;
    String pantryKey;

    private ListView resourceListView;
    private CardView myCard;
    private DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();
    private PantryInfo pantry;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pantry_detail_page);
        findViewById(R.id.mainLayout).setVisibility(View.INVISIBLE);
        Bundle extras = getIntent().getExtras();
        pantryKey = extras.getString("Food Pantry");

        //have to get the arrayList of resources in the specific pantry
        dbref.child("pantries").child(pantryKey).get()
                .addOnCompleteListener(task -> {
                    pantry = task.getResult().getValue(PantryInfo.class);
                    populateView();
                    findViewById(R.id.mainLayout).setVisibility(View.VISIBLE);
                });
    }

    private void populateView() {
        //back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(pantry.getName());

        populateHours();
        resourceList = pantry.getResources();

        //connect the resource list with the card view
        // View myview = inflater.inflate(R.layout.pantry_detail_page, container, false);
        resourceListView = (ListView) findViewById(R.id.conResourcesNeeded);
        myCard = (CardView) findViewById(R.id.card_view);

        ra = new ResourceAdapter(this, R.layout.resource_layout, resourceList);

        //setAdapter to the arrayList that we need to use
        //connect listview to the array adapter
        resourceListView.setAdapter(ra);
        registerForContextMenu(resourceListView);
        ra.notifyDataSetChanged();
    }

    private void populateHours() {
        Map<String, PantryHours> hours = pantry.hours;
        LinearLayout days = findViewById(R.id.linearLayout);
        if (hours.containsKey("Monday")) {
            // get array for selecting times based on the hours
            PantryHours curHours = hours.get("Monday");
            ((TextView) findViewById(R.id.monHoursView)).setText(curHours.toString());
        } else {
            ((TextView) findViewById(R.id.monHoursView)).setText("Unavailable");
        }
        if (hours.containsKey("Tuesday")) {
            // get array for selecting times based on the hours
            PantryHours curHours = hours.get("Tuesday");
            ((TextView) findViewById(R.id.tuesHoursView)).setText(curHours.toString());
        } else {
            ((TextView) findViewById(R.id.tuesHoursView)).setText("Unavailable");
        }
        if (hours.containsKey("Wednesday")) {
            // get array for selecting times based on the hours
            PantryHours curHours = hours.get("Wednesday");
            ((TextView) findViewById(R.id.wedHoursView)).setText(curHours.toString());
        } else {
            ((TextView) findViewById(R.id.wedHoursView)).setText("Unavailable");
        }
        if (hours.containsKey("Thursday")) {
            // get array for selecting times based on the hours
            PantryHours curHours = hours.get("Thursday");
            ((TextView) findViewById(R.id.thursHoursView)).setText(curHours.toString());
        } else {
            ((TextView) findViewById(R.id.thursHoursView)).setText("Unavailable");
        }
        if (hours.containsKey("Friday")) {
            // get array for selecting times based on the hours
            PantryHours curHours = hours.get("Friday");
            ((TextView) findViewById(R.id.friHoursView)).setText(curHours.toString());
        } else {
            ((TextView) findViewById(R.id.friHoursView)).setText("Unavailable");
        }
        if (hours.containsKey("Saturday")) {
            // get array for selecting times based on the hours
            PantryHours curHours = hours.get("Saturday");
            ((TextView) findViewById(R.id.satHoursView)).setText(curHours.toString());
        } else {
            ((TextView) findViewById(R.id.satHoursView)).setText("Unavailable");
        }
        if (hours.containsKey("Sunday")) {
            // get array for selecting times based on the hours
            PantryHours curHours = hours.get("Sunday");
            ((TextView) findViewById(R.id.sunHoursView)).setText(curHours.toString());
        } else {
            ((TextView) findViewById(R.id.sunHoursView)).setText("Unavailable");
        }
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    /** Called when the user presses contribute button*/
    public void contributeOnClick(View view) {
        Intent intent = new Intent(this, ContributeActivity.class);
        intent.putExtra("Food Pantry", pantryKey);
        startActivity(intent);
    }
}
