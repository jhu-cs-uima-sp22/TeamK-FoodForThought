package com.example.foodforthoughtapp;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;

import com.example.foodforthoughtapp.model.pantry.PantryInfo;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class MapsFrag extends Fragment implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener,
GoogleMap.OnMarkerClickListener{

    private GoogleMap mMap;
    private LatLng cityCoor = new LatLng(39.29, -76.61);
    private String cityName = "Baltimore";

    SearchView searchView;

    SupportMapFragment mapFragment;
    private MainActivity myact;
    Context cntx;

    DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view =inflater.inflate(R.layout.frag_maps, container, false);

        searchView = view.findViewById(R.id.idSearchView);

        cntx = getActivity().getApplicationContext();

        myact = (MainActivity) getActivity();
        myact.getSupportActionBar().setTitle("Map");



        // Get a handle to the fragment and register the callback.
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        /*
        searchLoc = (EditText)findViewById(R.id.plain_text_input);

        Button searchButton = (Button) findViewById(R.id.refresh_map_button);

        searchButton.setOnClickListener(view -> {
            cityName = searchLoc.getText().toString();
            cityCoor = getLocationFromAddress(MapsFrag.this, cityName);
            mMap.clear();
            mMap.moveCamera(CameraUpdateFactory.newLatLng(cityCoor));
            handleSearchOnClick(cityName);
        });
        */

        /*
        Menu menu = toolbar.getMenu();
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
        searchItem.setVisible(true);
         */

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                cityName = searchView.getQuery().toString();
                cityCoor = MapsFrag.getLocationFromAddress(cntx, cityName);
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

        if (!cityName.isEmpty()) {
            cityCoor = getLocationFromAddress(cntx, cityName);
        }
        //"Food Pantry"
        return view;
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
        Intent intent = new Intent(cntx, PantryDetail.class);
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
            pantryCoordinates.put(pantry, getLocationFromAddress(cntx, pantry.location.street +
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

    // Called at the start of the visible lifetime.
    @Override
    public void onStart(){
        super.onStart();
        Log.d ("Other Fragment2", "onStart");
        // Apply any required UI change now that the Fragment is visible.
    }

    // Called at the start of the active lifetime.
    @Override
    public void onResume(){
        super.onResume();
        Log.d ("Other Fragment", "onResume");
        // Resume any paused UI updates, threads, or processes required
        // by the Fragment but suspended when it became inactive.
    }

    // Called at the end of the active lifetime.
    @Override
    public void onPause(){
        Log.d ("Other Fragment", "onPause");
        // Suspend UI updates, threads, or CPU intensive processes
        // that don't need to be updated when the Activity isn't
        // the active foreground activity.
        // Persist all edits or state changes
        // as after this call the process is likely to be killed.
        super.onPause();
    }

    // Called to save UI state changes at the
    // end of the active lifecycle.
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Log.d ("Other Fragment", "onSaveInstanceState");
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate, onCreateView, and
        // onCreateView if the parent Activity is killed and restarted.
        super.onSaveInstanceState(savedInstanceState);
    }

    // Called at the end of the visible lifetime.
    @Override
    public void onStop(){
        Log.d ("Other Fragment", "onStop");
        // Suspend remaining UI updates, threads, or processing
        // that aren't required when the Fragment isn't visible.
        super.onStop();
    }

    // Called when the Fragment's View has been detached.
    @Override
    public void onDestroyView() {
        Log.d ("Other Fragment", "onDestroyView");
        // Clean up resources related to the View.
        super.onDestroyView();
    }

    // Called at the end of the full lifetime.
    @Override
    public void onDestroy(){
        Log.d ("Other Fragment", "onDestroy");
        // Clean up any resources including ending threads,
        // closing database connections etc.
        super.onDestroy();
    }

    // Called when the Fragment has been detached from its parent Activity.
    @Override
    public void onDetach() {
        Log.d ("Other Fragment", "onDetach");
        super.onDetach();
    }


}