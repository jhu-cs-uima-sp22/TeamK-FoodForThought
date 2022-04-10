package com.example.foodforthoughtapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import com.example.foodforthoughtapp.model.pantry.PantryInfo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.pantry_detail_page);
        Bundle extras = getIntent().getExtras();
        pantryKey = extras.getString("Food Pantry");

        //have to get the arrayList of resources in the specific pantry
        // PantryInfo pantry = dbref.child("pantries").child(pantryKey).get().getResult().getValue(PantryInfo.class);
        dbref.child("pantries").child(pantryKey).get()
                .addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        PantryInfo pantry = task.getResult().getValue(PantryInfo.class);
                        populateView(pantry);
                    }
                });
//
    }

    private void populateView(PantryInfo pantry) {
        resourceList = pantry.getResources();

        //setting the toolbar with the pantry name
        String pantryName = pantry.getName();
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.nav_center);
        TextView toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setText(pantryName);
        //back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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


    /** Called when the user presses contribute button*/
    public void contributeOnClick(View view) {
        Intent intent = new Intent(this, ContributeActivity.class);
        intent.putExtra("Food Pantry", pantryKey);
        startActivity(intent);
    }
}
