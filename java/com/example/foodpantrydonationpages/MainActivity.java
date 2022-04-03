package com.example.foodpantrydonationpages;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView resourceListView;
    List resourceList = new ArrayList();
    ArrayAdapter resourceAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("got here");

        resourceListView = (ListView) findViewById(R.id.resourcesNeeded);
        resourceList.add("Item 1                                                    count");
        resourceList.add("Item 2                                                    count");
        resourceList.add("Item 3                                                    count");
        resourceList.add("Item 4                                                    count");
        resourceList.add("Item 5                                                    count");
        resourceList.add("Item 6                                                    count");
        resourceList.add("Item 7                                                    count");

        resourceAdapter = new ArrayAdapter(MainActivity.this,
                android.R.layout.simple_expandable_list_item_1, resourceList);
        resourceListView.setAdapter(resourceAdapter);

    }
}