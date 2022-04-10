package com.example.foodforthoughtapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.foodforthoughtapp.model.pantry.Resource;

import java.util.List;

public class ContributeAdapter extends ArrayAdapter<Resource> {

    int resource;

    public ContributeAdapter(Context ctx, int res, List<Resource> items)
    {
        super(ctx, res, items);
        resource = res;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout itemView;
        Resource res = getItem(position);

        if (convertView == null) {
            itemView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
            vi.inflate(resource, itemView, true);
        } else {
            itemView = (LinearLayout) convertView;
        }

        CheckBox resourceCheck = (CheckBox) itemView.findViewById(R.id.resCheckBox);
        EditText resourceCount = (EditText) itemView.findViewById(R.id.editCount);

        //we have to set the values of the resource to the cardview
        //need the name and the count
        String resName = res.getResourceName();
        String resCount = String.valueOf(res.getCount());


        resourceCheck.setText(resName);
        resourceCount.setText(resCount);

        return itemView;
    }
}
