package com.example.foodforthoughtapp;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.foodforthoughtapp.model.pantry.PantryHours;
import com.example.foodforthoughtapp.model.pantry.PantryInfo;
import com.example.foodforthoughtapp.model.pantry.VolDateTime;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class VolunteerDateAdapter extends ArrayAdapter<VolDateTime> {

    int resource;
    PantryInfo pantry;

    Spinner startTime;
    Spinner endTime;

    String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

    private final View.OnClickListener dateOnClickListener = view -> {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePicker1 = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker1, int year, int month, int day) {
                ((TextView) view).setText((month + 1) + "/" + day + "/" + year);
                Calendar temp = Calendar.getInstance();
                temp.set(year, month, day);
                updateTimePickers(days[temp.get(Calendar.DAY_OF_WEEK) - 1]);
            }
        }, year, month, day);

        cal.add(Calendar.DAY_OF_MONTH, 1);
        datePicker1.getDatePicker().setMinDate(cal.getTimeInMillis());
        cal.add(Calendar.DAY_OF_MONTH, 6);
        datePicker1.getDatePicker().setMaxDate(cal.getTimeInMillis());
        datePicker1.show();
    };

    @SuppressLint("NewApi")
    private void updateTimePickers(String dayOfWeek) {
        Log.d("VolDateAdapter", "TimePickerUpdate: " + dayOfWeek);
        PantryHours hours = pantry.hours.get(dayOfWeek);
        try {
            LocalTime start = LocalTime.parse(hours.startTime);
            LocalTime end = LocalTime.parse(hours.endTime);
            int intervals = (int) start.until(end, ChronoUnit.MINUTES) / 30;
            List<CharSequence> times = new ArrayList<>(Collections.singletonList("None"));
            for (int i = 0 ; i < intervals + 1; i++) {
                times.add(start.plusMinutes(i * 30).toString());
            }
            setSpinner(startTime, times);
            setSpinner(endTime, times);
        } catch (Exception e) {
            Log.d("VolunteerDateAdapter", e.toString());
        }
    }
    public void setSpinner(Spinner spinner, List<CharSequence> times) {
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, times);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public VolunteerDateAdapter(Context ctx, int res, List<VolDateTime> volTimes, PantryInfo pantry) {
        super(ctx, res, volTimes);
        resource = res;
        this.pantry = pantry;
        Log.d("VolunteerDateAdapter", pantry.toString());
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout itemView;
        VolDateTime res = getItem(position);

        if (convertView == null) {
            itemView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(inflater);
            vi.inflate(resource, itemView, true);
        } else {
            itemView = (LinearLayout) convertView;
        }
        startTime = itemView.findViewById(R.id.start_time);
        endTime = itemView.findViewById(R.id.end_time);

        TextView datePicker = itemView.findViewById(R.id.date_picker);
        datePicker.setOnClickListener(dateOnClickListener);

        ImageButton delete = itemView.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Fix removal not working for some reason
                ((ContributeActivity) getContext()).conVolunteerList.remove(position);
                notifyDataSetChanged();
            }
        });

        return itemView;
    }

    @Nullable
    @Override
    public VolDateTime getItem(int position) {
        return super.getItem(position);
    }
}
