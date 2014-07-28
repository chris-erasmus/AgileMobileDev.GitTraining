package com.swissarmyutility.ReminderSetter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.app.swissarmyutility.R;
import com.swissarmyutility.globalnavigation.AppFragment;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Naresh.Kaushik on 16-07-2014.
 */

/**
 * Created by kapil.gupta on 23-07-2014.
 */
public class RemiderSetterFragment extends AppFragment {
    Button btn_add_event;
    EditText ed_date,ed_time,ed_event_title;
    Calendar myCalendar;
    DatePickerDialog.OnDateSetListener date;
    TimePickerDialog mTimePicker;
    int hour,minute;
    String str_date,str_time,str_event_title;
    long save_end_time;
    long millis=0,long_start_date_database=0,long_end_date_database=0;
    static Cursor cursor,cursor2;
    String str_complete_time__from_database,str_complete_end_time__from_database;
    int event_id;
    ContentResolver contentResolver;
    String event_Title,event_Desc,event_loc,str_month_splitted;
    Date event_Start;
    Date event_end;
    Date date_for_millis;
    SimpleDateFormat formatter,formatter2;
    DateFormat df,df_end_date;
    String[] array_strt_date,array_end_date;
    String str_millis;

    @SuppressLint("Override")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v= inflater.inflate(R.layout.reminder_setter,null);
        btn_add_event=(Button)v.findViewById(R.id.btn_add_event);
        ed_date=(EditText)v.findViewById(R.id.ed_date);
        ed_time=(EditText)v.findViewById(R.id.ed_time);
        ed_event_title=(EditText)v.findViewById(R.id.ed_event_title);
        contentResolver = getActivity().getContentResolver();




// add event button
        btn_add_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Perform event action on click
                addCalendarEvent();
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
//*
        //* date picker dialog open on edit text listener
        ed_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
//*
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
                        ed_time.setText( selectedHour + ":" + selectedMinute  );
                        str_time=selectedHour + ":" + selectedMinute ;
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
        str_date=sdf.format(myCalendar.getTime());
    }
    //*

    //*method is calling when add event button triggered
    public void addCalendarEvent() {
        //convert edit text field into string
        str_event_title = ed_event_title.getText().toString();

        Intent l_intent = new Intent(Intent.ACTION_EDIT);
        l_intent.setType("vnd.android.cursor.item/event");
        // Addition of date and time string
        String toParse = str_date + " " + str_time;
        try {

            formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm"); // I assume d-M, you may refer to M-d for month-day instead.
            date_for_millis = formatter.parse(toParse); // You will need try/catch around this
            millis = date_for_millis.getTime(); //converting date and time into milliseconds
            str_millis = Long.toString(millis);//converting date and time milliseconds into string

        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        save_end_time = millis + 1800 * 1000; // setting up end time for calendar event
        l_intent.putExtra("id", 1);
        l_intent.putExtra("title", str_event_title);
        l_intent.putExtra("description", "This is a simple test for calendar api");
        l_intent.putExtra("beginTime", millis);
        l_intent.putExtra("endTime", save_end_time);
        l_intent.putExtra(CalendarContract.Events.EVENT_LOCATION, "home");

        Uri caluri = CalendarContract.Events.CONTENT_URI;
        Uri atteuri = CalendarContract.Attendees.CONTENT_URI;

        // retrieving the values from calendar database through Content Provider
        cursor = contentResolver.query(caluri,
                (new String[]{CalendarContract.Events._ID, CalendarContract.Events.TITLE,
                        CalendarContract.Events.DESCRIPTION, CalendarContract.Events.DTSTART,
                        CalendarContract.Events.DTEND, CalendarContract.Events.EVENT_LOCATION}), null, null, null
        );
        if (cursor != null) {
            while (cursor.moveToNext()) {
                event_id = cursor.getInt(cursor.getColumnIndex(CalendarContract.Events._ID));
                cursor2 = contentResolver.query(atteuri, new String[]{CalendarContract.Attendees.ATTENDEE_NAME, CalendarContract.Attendees.ATTENDEE_EMAIL},
                        CalendarContract.Attendees.EVENT_ID + "=" + event_id, null, null);
                if (cursor2 != null) {
                    while (cursor2.moveToNext()) {
                        event_Title = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.TITLE));
                        event_Desc = cursor.getString(cursor.getColumnIndexOrThrow(CalendarContract.Events.DESCRIPTION));
                        event_Start = new Date(cursor.getLong(cursor.getColumnIndex(CalendarContract.Events.DTSTART)));
                        event_end = new Date(cursor.getLong(cursor.getColumnIndex(CalendarContract.Events.DTEND)));
                        event_loc = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.EVENT_LOCATION));
                    }
                    cursor2.close();
                }

            }
            cursor.close();
        }
        String str_saved_millis, str_event_start_date_from_database = null, str_event_end_date_from_database = null,
                str_start_final_date_database, str_end_final_date_database;
        str_saved_millis = Long.toString(save_end_time);
        if(cursor2!=null) {
            str_event_start_date_from_database = event_Start.toString();
            str_event_end_date_from_database = event_end.toString();
        }
        try {

            if (cursor2 != null) {

                array_end_date = split(str_event_end_date_from_database, ' '); //splitting end date-time value which is coming from calendar database

                String strsplitenddate1 = array_end_date[1];
                df_end_date = new SimpleDateFormat("MMM");
                Date d_end = df_end_date.parse(strsplitenddate1);
                df_end_date = new SimpleDateFormat("MM");
                String strend__month_splitted = df_end_date.format(d_end);
                String strendsplit2 = array_end_date[2];
                String strend_split_year = array_end_date[5];
                String strend_split_time = array_end_date[3];
                String[] arrayend_split_time = split(strend_split_time, ':');
                String strend_hour = arrayend_split_time[0];
                String strend_minute = arrayend_split_time[1];
                String strend_hour_minute = strend_hour + ":" + strend_minute;
                // splitted,added and converted according to gettable end date time format
                str_complete_end_time__from_database = strendsplit2 + "-" + strend__month_splitted + "-" + strend_split_year + " " + strend_hour_minute;
                array_strt_date = split(str_event_start_date_from_database, ' '); //splitting start date-time value which is coming from calendar database
                String strsplit1 = array_strt_date[1];
                df = new SimpleDateFormat("MMM");
                Date d = df.parse(strsplit1);
                df = new SimpleDateFormat("MM");
                str_month_splitted = df.format(d);
                String strsplit2 = array_strt_date[2];
                String str_split_year = array_strt_date[5];
                String str_split_time = array_strt_date[3];
                String[] array_split_time = split(str_split_time, ':');
                String str_hour = array_split_time[0];
                String str_minute = array_split_time[1];
                String str_hour_minute = str_hour + ":" + str_minute;
                // splitted,added and converted according to gettable end date time format
                str_complete_time__from_database = strsplit2 + "-" + str_month_splitted + "-" + str_split_year + " " + str_hour_minute;
            }
        }catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            formatter2 = new SimpleDateFormat("dd-MM-yyyy hh:mm");
            if(cursor2!=null) {

                Date date1 = formatter2.parse(str_complete_time__from_database);
                Date date2 = formatter2.parse(str_complete_end_time__from_database);
                long_start_date_database = date1.getTime();
                long_end_date_database = date2.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        str_start_final_date_database = Long.toString(long_start_date_database);
        str_end_final_date_database = Long.toString(long_end_date_database);
        //* Cheking duplicacy of event
        if ((str_millis != null)){
            if (str_event_title.equals(event_Title) || str_millis.equals(str_start_final_date_database) ||
                    str_saved_millis.equals(str_end_final_date_database)) {
                showMessage("Alert", "Event Exist please try different event");
            } else {
                try {
                    startActivity(l_intent);
                    getActivity().finish();
                } catch (Exception e) {
                    Toast.makeText(getActivity(), "Sorry, no compatible calendar is found!", Toast.LENGTH_LONG).show();
                }
            }
        }
        else{
            showMessage("Alert", "Please fill all fields");
        }
//*
    }

//*

    //*this method is showing pop up box
    private void showMessage(String title, String message) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        final AlertDialog alertDialog = alertBuilder.create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                alertDialog.dismiss();
            }
        });

        alertDialog.show();

    }

    //*

    //* this method splitting the String
    public String[] split(String s, char suffix) {
        int dem = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == suffix) {
                dem++;
            }
            // System.out.println("DEM VARIABLE:::::" + dem);
        }
        String[] temp = new String[dem + 1];
        for (int i = 0; i < temp.length; i++) {
            temp[i] = "";
        }
        dem = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == suffix) {
                dem++;
            } else {
                if (dem < temp.length) {
                    temp[dem] += s.charAt(i);
                }
            }
        }
        return temp;
    }
}