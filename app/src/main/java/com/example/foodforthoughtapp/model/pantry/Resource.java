package com.example.foodforthoughtapp.model.pantry;

public class Resource {
    private String resourceName;
    private int count;

    Resource(String res, int c) {
        this.resourceName = res;
        this.count = c;
    }

    public String getResourceName() { return resourceName; }


    public int getCount() { return count; }


}
