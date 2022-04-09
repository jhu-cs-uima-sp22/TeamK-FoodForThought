package com.example.foodforthoughtapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.ArrayList;
import java.util.List;

public class PantryDetail extends AppCompatActivity {
    List resourceList = new ArrayList();
    ResourceAdapter ra;

    private ListView resourceListView;
    private CardView myCard;


    protected void onCreate(LayoutInflater inflater, ViewGroup container,
                            Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pantry_detail_page);
        Bundle extras = getIntent().getExtras();
        String pantryKey = extras.getString("Food Pantry");

        //connect the resource list with the card view
        View myview = inflater.inflate(R.layout.pantry_detail_page, container, false);
        resourceListView = (ListView) myview.findViewById(R.id.resourcesNeeded);
        myCard = (CardView) myview.findViewById(R.id.card_view);

        //ra = new ResourceAdapter(this, R.layout.resource_layout, myBooks);

        //setAdapter to the arrayList that we need to use
        //connect listview to the array adapter
        //resourceListView.setAdapter(array adapter we're using);
        registerForContextMenu(resourceListView);

        //array adapter we're using.notifyDataSetChanged();


    }
}
