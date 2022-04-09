package com.example.foodforthoughtapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.foodforthoughtapp.model.pantry.PantryInfo;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ContributeActivity extends AppCompatActivity {
    List conResourceList = new ArrayList();
    ContributeAdapter ca;

    private ListView resourceConListView;
    private CardView contributeCard;

    DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();

    protected void onCreate(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contribute_page);
        Bundle extras = getIntent().getExtras();
        String pantryKey = extras.getString("Food Pantry");

        //have to get the arrayList of resources in the specific pantry
        PantryInfo pantry = dbref.child("pantries").child(pantryKey).get().getResult().getValue(PantryInfo.class);
        conResourceList = pantry.getResources();

        //connect the resource list with the card view
        View myview = inflater.inflate(R.layout.contribute_page, container, false);
        resourceConListView = (ListView) myview.findViewById(R.id.conResourcesNeeded);
        contributeCard = (CardView) myview.findViewById(R.id.contribute_card_view);

        ca = new ContributeAdapter(this, R.layout.resource_contribute_layout, conResourceList);

        //setAdapter to the arrayList that we need to use
        //connect listview to the array adapter
        resourceConListView .setAdapter(ca);
        registerForContextMenu(resourceConListView);
        ca.notifyDataSetChanged();


        //setting the monday/tues/thus/fri start spinners
        Spinner mstartSpinner = (Spinner) findViewById(R.id.strtSpinnerM);
        setMtthfSpinners(mstartSpinner);
        Spinner tstartSpinner = (Spinner) findViewById(R.id.strtSpinnerT);
        setMtthfSpinners(tstartSpinner);
        Spinner thstartSpinner = (Spinner) findViewById(R.id.strtSpinnerTh);
        setMtthfSpinners(thstartSpinner);
        Spinner fstartSpinner = (Spinner) findViewById(R.id.strtSpinnerF);
        setMtthfSpinners(fstartSpinner);

        //setting the monday/tues/thus/fri end spinners
        Spinner mendSpinner = (Spinner) findViewById(R.id.endSpinnerM);
        setMtthfSpinners(mendSpinner);
        Spinner tendSpinner = (Spinner) findViewById(R.id.endSpinnerT);
        setMtthfSpinners(tendSpinner);
        Spinner thendSpinner = (Spinner) findViewById(R.id.endSpinnerTh);
        setMtthfSpinners(thendSpinner);
        Spinner fendSpinner = (Spinner) findViewById(R.id.endSpinnerF);
        setMtthfSpinners(fendSpinner);

        //setting the wednesday start and end spinners
        Spinner wstrtSpinner = (Spinner) findViewById(R.id.strtSpinnerW);
        setWSpinners(wstrtSpinner);
        Spinner wendSpinner = (Spinner) findViewById(R.id.endSpinnerW);
        setWSpinners(wendSpinner);

        //setting the sat/sun start and end spinners
        Spinner satStrtSpinner = (Spinner) findViewById(R.id.strtSpinnerSa);
        setSasuSpinners(satStrtSpinner);
        Spinner sunStrtSpinner = (Spinner) findViewById(R.id.strtSpinnerSu);
        setSasuSpinners(sunStrtSpinner);
        Spinner satEndSpinner = (Spinner) findViewById(R.id.endSpinnerSa);
        setSasuSpinners(satEndSpinner);
        Spinner sunEndSpinner = (Spinner) findViewById(R.id.endSpinnerSu);
        setSasuSpinners(sunEndSpinner);


    }

    public void setMtthfSpinners(Spinner spinner) {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.mtthf_time_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    public void setWSpinners(Spinner spinner) {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.w_time_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    public void setSasuSpinners(Spinner spinner) {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.ss_time_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
    }

    /** Called when the user presses submit button*/
    //public void submitOnClick(View view) {
        //Intent intent = new Intent(this, submitActivity.class);
        //startActivity(intent);
    //}
}
