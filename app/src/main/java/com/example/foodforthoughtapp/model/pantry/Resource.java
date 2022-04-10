package com.example.foodforthoughtapp.model.pantry;

public class Resource {
    public String resourceName;
    public int count;

    public Resource() {}

    public Resource(String res, int c) {
        this.resourceName = res;
        this.count = c;
    }

    public String getResourceName() { return resourceName; }


    public int getCount() { return count; }


}
