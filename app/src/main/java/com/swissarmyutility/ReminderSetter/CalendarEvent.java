package com.swissarmyutility.ReminderSetter;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.app.swissarmyutility.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kapil.gupta on 28-07-2014.
 */
public class CalendarEvent {

    long save_end_time;
    long millis = 0;
    String str_event_title, str_millis, event_Title_db, event_Desc_db, event_loc_db, str_month_splitted, str_complete_time__from_database, str_complete_end_time__from_database, split_end_date1, split_month_end, split_end_date2, split_end_year, split_end_time,
            hour_end, minute_end, hour_minute_end, split_start_date_db1, split_start_date_db2, str_split_year, str_split_time,
            str_hour, str_minute, str_hour_minute;
    SimpleDateFormat formatter, formatter_db;
    Date date_for_millis, event_Start_db, event_end_db, date_for_start_time, date_for_end_time;
    long long_start_date_database = 0, long_end_date_database = 0;
    static Cursor cursor, cursor_for_attendees;
    int event_id;
    String[] array_strt_date, array_end_date, split_end_time_array, array_split_time;
    DateFormat df, df_end_date;
    ContentResolver contentResolver;
    //Initialize Split_String class
    Split_String split_string = new Split_String();
    Context context;
    View view;
    Bundle bundle;
    Uri calendar_uri, attendees_uri;

    public void setFragmentView(View view, Bundle bundle, Context ct) {
        this.view = view;
        this.bundle = bundle;
        this.context = ct;
    }

    public void addCalendarEvent() {
        //convert edit text field into string
        contentResolver = context.getContentResolver();
        EditText ed_event_title = (EditText) view.findViewById(R.id.ed_event_title);
        str_event_title = ed_event_title.getText().toString();

        Intent calendar_intent = new Intent(Intent.ACTION_EDIT);
        calendar_intent.setType("vnd.android.cursor.item/event");
        // Addition of date and time string
        String toParse = bundle.getString("date_value") + " " + bundle.getString("time_value");
        try {
            formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm"); // I assume d-M, you may refer to M-d for month-day instead.
            date_for_millis = formatter.parse(toParse); // You will need try/catch around this
            millis = date_for_millis.getTime(); //converting date and time into milliseconds
            str_millis = Long.toString(millis);//converting date and time milliseconds into string
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        save_end_time = millis + 1800 * 1000; // setting up end time for calendar event
        calendar_intent.putExtra("id", 1);
        calendar_intent.putExtra("title", str_event_title);
        calendar_intent.putExtra("description", "This is a simple test for calendar api");
        calendar_intent.putExtra("beginTime", millis);
        calendar_intent.putExtra("endTime", save_end_time);
        calendar_intent.putExtra(CalendarContract.Events.EVENT_LOCATION, "home");
        calendar_uri = CalendarContract.Events.CONTENT_URI;
        attendees_uri = CalendarContract.Attendees.CONTENT_URI;
        // retrieving the values from calendar database through Content Provider
        cursor = contentResolver.query(calendar_uri,
                (new String[]{CalendarContract.Events._ID, CalendarContract.Events.TITLE,
                        CalendarContract.Events.DESCRIPTION, CalendarContract.Events.DTSTART,
                        CalendarContract.Events.DTEND, CalendarContract.Events.EVENT_LOCATION}), null, null, null
        );
        if (cursor != null) {
            while (cursor.moveToNext()) {
                event_id = cursor.getInt(cursor.getColumnIndex(CalendarContract.Events._ID));
                cursor_for_attendees = contentResolver.query(attendees_uri, new String[]{CalendarContract.Attendees.ATTENDEE_NAME, CalendarContract.Attendees.ATTENDEE_EMAIL},
                        CalendarContract.Attendees.EVENT_ID + "=" + event_id, null, null);
                if (cursor_for_attendees != null) {
                    while (cursor_for_attendees.moveToNext()) {
                        event_Title_db = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.TITLE));
                        event_Desc_db = cursor.getString(cursor.getColumnIndexOrThrow(CalendarContract.Events.DESCRIPTION));
                        event_Start_db = new Date(cursor.getLong(cursor.getColumnIndex(CalendarContract.Events.DTSTART)));
                        event_end_db = new Date(cursor.getLong(cursor.getColumnIndex(CalendarContract.Events.DTEND)));
                        event_loc_db = cursor.getString(cursor.getColumnIndex(CalendarContract.Events.EVENT_LOCATION));
                    }
                    cursor_for_attendees.close();
                }
            }
            cursor.close();
        }
        String str_saved_millis, str_event_start_date_from_database = null, str_event_end_date_from_database = null,
                str_start_final_date_database, str_end_final_date_database;
        str_saved_millis = Long.toString(save_end_time);
        if (cursor_for_attendees != null) {
            str_event_start_date_from_database = event_Start_db.toString();
            str_event_end_date_from_database = event_end_db.toString();
        }
        try {
            if (cursor_for_attendees != null) {
                array_end_date = split_string.split(str_event_end_date_from_database, ' '); //splitting end date-time value which is coming from calendar database
                split_end_date1 = array_end_date[1];
                df_end_date = new SimpleDateFormat("MMM");
                Date d_end = df_end_date.parse(split_end_date1);
                df_end_date = new SimpleDateFormat("MM");
                split_month_end = df_end_date.format(d_end);
                split_end_date2 = array_end_date[2];
                split_end_year = array_end_date[5];
                split_end_time = array_end_date[3];
                split_end_time_array = split_string.split(split_end_time, ':');
                hour_end = split_end_time_array[0];
                minute_end = split_end_time_array[1];
                hour_minute_end = hour_end + ":" + minute_end;

                str_complete_end_time__from_database = split_end_date2 + "-" + split_month_end + "-" + split_end_year + " " + hour_minute_end;
                array_strt_date = split_string.split(str_event_start_date_from_database, ' '); //splitting start date-time value which is coming from calendar database
                split_start_date_db1 = array_strt_date[1];
                df = new SimpleDateFormat("MMM");
                Date d = df.parse(split_start_date_db1);
                df = new SimpleDateFormat("MM");
                str_month_splitted = df.format(d);
                split_start_date_db2 = array_strt_date[2];
                str_split_year = array_strt_date[5];
                str_split_time = array_strt_date[3];
                array_split_time = split_string.split(str_split_time, ':');
                str_hour = array_split_time[0];
                str_minute = array_split_time[1];
                str_hour_minute = str_hour + ":" + str_minute;
                // splitted,added and converted according to gettable end date time format
                str_complete_time__from_database = split_start_date_db2 + "-" + str_month_splitted + "-" + str_split_year + " " + str_hour_minute;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            formatter_db = new SimpleDateFormat("dd-MM-yyyy hh:mm");
            if (cursor_for_attendees != null) {

                date_for_start_time = formatter_db.parse(str_complete_time__from_database);
                date_for_end_time = formatter_db.parse(str_complete_end_time__from_database);
                long_start_date_database = date_for_start_time.getTime();
                long_end_date_database = date_for_end_time.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        str_start_final_date_database = Long.toString(long_start_date_database);
        str_end_final_date_database = Long.toString(long_end_date_database);
        //* Checking duplicacy of event
        if ((str_millis != null)) {
            if (str_event_title.equals(event_Title_db) || str_millis.equals(str_start_final_date_database) ||
                    str_saved_millis.equals(str_end_final_date_database)) {
                showMessage("Alert", "Event Exist please try different event");
            } else {
                try {
                    context.startActivity(calendar_intent);

                } catch (Exception e) {
                    Toast.makeText(context, "Sorry, no compatible calendar is found!", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            showMessage("Alert", "Please fill all fields");
        }
//*
    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
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
}
