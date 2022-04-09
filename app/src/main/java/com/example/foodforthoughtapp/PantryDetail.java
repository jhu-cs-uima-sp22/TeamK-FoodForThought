package com.example.foodforthoughtapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.foodforthoughtapp.model.pantry.PantryInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class PantryDetail extends AppCompatActivity {
    List resourceList = new ArrayList();
    ResourceAdapter ra;
    String pantryKey;

    private ListView resourceListView;
    private CardView myCard;

    DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();


    protected void onCreate(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantry_detail_page);
        Bundle extras = getIntent().getExtras();
        pantryKey = extras.getString("Food Pantry");

        //have to get the arrayList of resources in the specific pantry
        PantryInfo pantry = dbref.child("pantries").child(pantryKey).get().getResult().getValue(PantryInfo.class);
        resourceList = pantry.getResources();


        //connect the resource list with the card view
        View myview = inflater.inflate(R.layout.pantry_detail_page, container, false);
        resourceListView = (ListView) myview.findViewById(R.id.conResourcesNeeded);
        myCard = (CardView) myview.findViewById(R.id.card_view);

        ra = new ResourceAdapter(this, R.layout.resource_layout, resourceList);

        //setAdapter to the arrayList that we need to use
        //connect listview to the array adapter
        resourceListView.setAdapter(ra);
        registerForContextMenu(resourceListView);
        ra.notifyDataSetChanged();

    }

    /** Called when the user presses contribute button*/
    public void contributeOnClick(View view) {
        Intent intent = new Intent(this, ContributeActivity.class);
        intent.putExtra("Food Pantry", pantryKey);
        startActivity(intent);
    }
}
