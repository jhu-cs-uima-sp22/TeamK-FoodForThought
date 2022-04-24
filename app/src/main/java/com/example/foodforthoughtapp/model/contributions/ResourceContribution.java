package com.example.foodforthoughtapp.model.contributions;

import com.example.foodforthoughtapp.model.pantry.Resource;

import java.util.List;
import java.util.Map;

public class ResourceContribution extends Contribution {
    public String date;
    public String pantryID;
    public List<Resource> resources;

    public ResourceContribution() {}

    public ResourceContribution(String date, String pantryID, List<Resource> resources) {
        this.date = date;
        this.pantryID = pantryID;
        this.resources = resources;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder(" on " + date + ":\n");
        for (int i = 0; i < resources.size(); i++) {
            Resource res = resources.get(i);
            str.append("- " + res.resourceName + ": " + res.count);
            if (i < resources.size() - 1) {
                str.append("\n");
            }
        }
        return str.toString();
    }
}
