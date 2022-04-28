package com.example.foodforthoughtapp;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

        DatePickerDialog datePicker1 = new DatePickerDialog(getContext(), (datePicker11, year1, month1, day1) -> {
            ((TextView) view).setText(String.format("%02d/%02d/%04d", (month1 + 1), day1, year1));
            Calendar temp = Calendar.getInstance();
            temp.set(year1, month1, day1);
            updateTimePickers(days[temp.get(Calendar.DAY_OF_WEEK) - 1]);
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
        List<CharSequence> times = new ArrayList<>(Collections.singletonList("None"));
        PantryHours hours = pantry.hours.get(dayOfWeek);
        try {
            LocalTime start = LocalTime.parse(hours.startTime);
            LocalTime end = LocalTime.parse(hours.endTime);
            int intervals = (int) start.until(end, ChronoUnit.MINUTES) / 30;
            for (int i = 0 ; i < intervals + 1; i++) {
                times.add(start.plusMinutes(i * 30).toString());
            }
        } catch (Exception e) {
            Log.d("VolunteerDateAdapter", e.toString());
        }
        setSpinner(startTime, times);
        setSpinner(endTime, times);
    }
    private void setSpinner(Spinner spinner, List<CharSequence> times) {
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

    private void updateEntry(VolDateTime position, String date, String start, String end) {
        position.setDate(date);
        position.setTime(start, end);
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
        delete.setOnClickListener(v -> {
            // TODO: Fix removal not working for some reason
            ((ContributeActivity) getContext()).conVolunteerList.remove(res);
            notifyDataSetChanged();
        });

        startTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                updateEntry(res, datePicker.getText().toString(), startTime.getSelectedItem().toString(), endTime.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });

        endTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                updateEntry(res, datePicker.getText().toString(), startTime.getSelectedItem().toString(), endTime.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });

        return itemView;
    }

    @Nullable
    @Override
    public VolDateTime getItem(int position) {
        return super.getItem(position);
    }
}
