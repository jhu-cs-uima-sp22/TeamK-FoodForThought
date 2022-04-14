package com.example.foodforthoughtapp.model.contributions;

import com.example.foodforthoughtapp.model.pantry.Resource;

import java.util.Map;

public class ResourceContribution {
    public String pantryID;
    public Map<Resource, Integer> resources;

    public ResourceContribution(String pantryID, Map<Resource, Integer> resources) {
        this.pantryID = pantryID;
        this.resources = resources;
    }
}
