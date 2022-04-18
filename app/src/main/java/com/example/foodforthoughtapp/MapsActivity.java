package com.example.foodforthoughtapp;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.foodforthoughtapp.model.pantry.PantryInfo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener,
GoogleMap.OnMarkerClickListener{

    private GoogleMap mMap;
    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;
    private EditText searchLoc;
    private LatLng cityCoor = new LatLng(39.29, -76.61);
    private String cityName = "Baltimore";
    private FragmentTransaction transaction;

    SearchView searchView;

    DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the layout file as the content view.
        setContentView(R.layout.activity_maps);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Menu menu = toolbar.getMenu();
        //menu.findItem(R.id.action_search).setVisible(false);


        // Get a handle to the fragment and register the callback.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        /*
        searchLoc = (EditText)findViewById(R.id.plain_text_input);

        Button searchButton = (Button) findViewById(R.id.refresh_map_button);

        searchButton.setOnClickListener(view -> {
            cityName = searchLoc.getText().toString();
            cityCoor = getLocationFromAddress(MapsActivity.this, cityName);
            mMap.clear();
            mMap.moveCamera(CameraUpdateFactory.newLatLng(cityCoor));
            handleSearchOnClick(cityName);
        });
        */
        /*
        Menu menu = toolbar.getMenu();
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                cityName = searchView.getQuery().toString();
                cityCoor = MapsActivity.getLocationFromAddress(MapsActivity.this, cityName);
                mMap.clear();
                mMap.moveCamera(CameraUpdateFactory.newLatLng(cityCoor));
                handleSearchOnClick(cityName);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
         */
        if (!cityName.isEmpty()) {
            cityCoor = getLocationFromAddress(this, cityName);
        }

        Fragment settingsFrag = new SettingsFrag();

        dl = (DrawerLayout)findViewById(R.id.my_drawer_layout);
        abdt = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);
        abdt.setDrawerIndicatorEnabled(true);
        dl.addDrawerListener(abdt);
        abdt.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView nav_view = (NavigationView) findViewById(R.id.nav_view);
        nav_view.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.map_view) {

            } else if (id == R.id.opportunities) {

            } else if (id == R.id.contributions) {

            } else if (id == R.id.settings) {
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.map,settingsFrag);
                transaction.addToBackStack(null);
                transaction.commit();

            } else if (id == R.id.nav_logout) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MapsActivity.this, WelcomeActivity.class));
                finish();
            }
            return true;
        });

        //"Food Pantry"
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Initalize the map
        mMap = googleMap;

        //Mark all the food pantries in the array with markers, and create info windows
        //addMarkersAndInfoWindows(List<Address addresses>);

        //Move the camera
        LatLng baltimore = new LatLng(39.29, -76.61);
        //mMap.addMarker(new MarkerOptions().position(baltimore).title("Marker in Baltimore"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(baltimore));

        //Start new activity on infoWindowClick
        googleMap.setOnInfoWindowClickListener(this);

    }

    public static LatLng getLocationFromAddress(Context context, String strAddress) {
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p1;

    }

    @Override
    public void onInfoWindowClick(@NonNull Marker marker) {
        Intent intent = new Intent(this, PantryDetail.class);
        intent.putExtra("Food Pantry", (String) marker.getTag());
        startActivity(intent);
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        marker.showInfoWindow();
        return true;
    }


    private void addMarkersAndInfoWindows(HashMap<PantryInfo, LatLng> pantryCoordinates, HashMap<PantryInfo, String> pantryKeys) {
        for(PantryInfo pantry : pantryCoordinates.keySet()){
            Marker mapMarker = mMap.addMarker(new MarkerOptions().position(pantryCoordinates.get(pantry)).title(pantry.name)
                    .snippet(pantry.location.street + ", " + pantry.location.city));
            mapMarker.setTag(pantryKeys.get(pantry));
        }
    }


    private HashMap<PantryInfo, LatLng> getPantriesCoordinates(Set<PantryInfo> pantries) {
        HashMap<PantryInfo, LatLng> pantryCoordinates = new HashMap<>();

        for (PantryInfo pantry : pantries) {
            pantryCoordinates.put(pantry, getLocationFromAddress(MapsActivity.this, pantry.location.street +
                    ", " + pantry.location.city));
        }

        return pantryCoordinates;

    }

    private void handleSearchOnClick(String city) {
        HashMap<PantryInfo, String> pantries = new HashMap<>();

        dbref.child("pantries").get().addOnCompleteListener(task -> {
            DataSnapshot res = task.getResult();
            for (DataSnapshot child : res.getChildren()) {
                PantryInfo pantry = child.getValue(PantryInfo.class);
                if (pantry.location.city.equalsIgnoreCase(city)) {
                    pantries.put(pantry, child.getKey());
                }
            }
            Log.d("DEBUG", pantries.toString());
            HashMap<PantryInfo, LatLng> pantryCoordinates = getPantriesCoordinates(pantries.keySet());
            Log.d("DEBUG", pantryCoordinates.toString());
            addMarkersAndInfoWindows(pantryCoordinates, pantries);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Enter your city");

        return super.onCreateOptionsMenu(menu);
    }
}