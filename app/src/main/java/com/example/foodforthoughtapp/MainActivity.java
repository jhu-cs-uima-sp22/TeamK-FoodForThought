package com.example.foodforthoughtapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout dl;
    private ActionBarDrawerToggle abdt;

    private FragmentTransaction transaction;
    private Fragment settings;
    private Fragment map;

    DatabaseReference dbref = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.out.println("Created Main Activity");

        // Set the layout file as the content view.
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        System.out.println("Set up toolbars and support bar");

        //Menu menu = toolbar.getMenu();
        //menu.findItem(R.id.action_search).setVisible(false);

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
        /*
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                cityName = searchView.getQuery().toString();
                cityCoor = MapsFrag.getLocationFromAddress(MapsFrag.this, cityName);
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

        settings = new SettingsFrag();
        map = new MapsFrag();

        System.out.println("Set up required fragments");

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, settings).commit();

        System.out.println("Added fragment to the screen initially");

        dl = (DrawerLayout)findViewById(R.id.my_drawer_layout);
        abdt = new ActionBarDrawerToggle(this, dl, R.string.Open, R.string.Close);
        abdt.setDrawerIndicatorEnabled(true);
        dl.addDrawerListener(abdt);
        abdt.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        System.out.println("Created drawer");

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        System.out.println("Created navigation view");
        /*
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
                startActivity(new Intent(MapsFrag.this, WelcomeActivity.class));
                finish();
            }
            return true;
        });
         */

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return abdt.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Enter your city");

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.map_view) {
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, map);
            transaction.addToBackStack(null);
            transaction.commit();
        } else if (id == R.id.opportunities) {

        } else if (id == R.id.contributions) {

        } else if (id == R.id.settings) {
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container,settings);
            transaction.addToBackStack(null);
            transaction.commit();

        } else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this, WelcomeActivity.class));
            finish();
        }
        return true;
    }
}
