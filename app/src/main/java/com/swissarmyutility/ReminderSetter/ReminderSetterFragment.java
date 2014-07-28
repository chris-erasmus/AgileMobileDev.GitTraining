package com.swissarmyutility.ReminderSetter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.swissarmyutility.globalnavigation.AppFragment;
import com.app.swissarmyutility.R;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by kapil.gupta on 23-07-2014.
 */
public class ReminderSetterFragment extends AppFragment {
    Button btn_add_event;
    EditText ed_date, ed_time, ed_event_title;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    TimePickerDialog mTimePicker;
    int hour, minute;
    String str_date, str_time;
    //Initialize Calendar_Event class
    CalendarEvent calendarEvent = new CalendarEvent();
    Context context;

    @SuppressLint("Override")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.reminder_setter, null);
        btn_add_event = (Button) v.findViewById(R.id.btn_add_event);
        ed_date = (EditText) v.findViewById(R.id.ed_date);
        ed_time = (EditText) v.findViewById(R.id.ed_time);
        ed_event_title = (EditText) v.findViewById(R.id.ed_event_title);
        context = getActivity();
// add event button
        btn_add_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform event action on click
                Bundle bundle = new Bundle();
                bundle.putString("date_value", str_date);
                bundle.putString("time_value", str_time);
                calendarEvent.setFragmentView(getView(), bundle, context);
                calendarEvent.addCalendarEvent();
            }
        });
        myCalendar = Calendar.getInstance();
//* Date Picker dialog
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        //* date picker dialog open on edit text listener
        ed_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)
                ).show();

            }
        });
        Calendar mcurrentTime = Calendar.getInstance();
        hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        minute = mcurrentTime.get(Calendar.MINUTE);
        //* Time picker dialog open on edit text listener
        ed_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTimePicker = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        ed_time.setText(selectedHour + ":" + selectedMinute);
                        str_time = selectedHour + ":" + selectedMinute;
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        setTitle("Reminder");
        super.onActivityCreated(savedInstanceState);
    }

    private void updateLabel() {
        String myFormat = "dd-MM-yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        ed_date.setText(sdf.format(myCalendar.getTime()));
        str_date = sdf.format(myCalendar.getTime());
    }

}