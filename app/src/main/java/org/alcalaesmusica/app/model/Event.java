package org.alcalaesmusica.app.model;


import android.content.Context;
import android.support.annotation.NonNull;

import org.alcalaesmusica.app.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by julio on 23/05/17.
 */

public class Event extends RealmObject implements Comparable {

    public static final int TIME_HOUR_MIDNIGHT_SAFE_THRESHOLD = 5; // After 5:00 is "next day"

    public static final String TIME = "time";
    public static final String DAY = "day";
    public static final String FAVOURITE = "favourite";
    public static final String ID = "id";
    public static final String STARRED = "starred";
    public static final String BAND_ID = "band";
    public static final String TIME_HOUR_MIDNIGHT_SAFE = "timeHourMidnightSafe";

    public static DateFormat dateFormatApi = new SimpleDateFormat("yyyy-MM-dd");
    public static DateFormat dateFormatShareText = new SimpleDateFormat("EEE d MMMM");

    public static DateFormat timeFormatApi = new SimpleDateFormat("HH:mm:ss");
    public static DateFormat timeFormatUser = new SimpleDateFormat("HH:mm");


    @PrimaryKey
    private Integer id;
    private String day;
    private String time;
    private int band;
    private int duration;
    private boolean starred;
    private Band bandEntity;
    private Venue venue;

    private int timeHourMidnightSafe;

    public String getDayShareFormat() {
        try {
            Date dateDay = dateFormatApi.parse(day);
            return dateFormatShareText.format(dateDay);
        } catch (ParseException e) {
            e.printStackTrace();

            // Better than nothing :S
            return day.substring(8);
        }
    }

    public String getTimeFormatted() {
        try {
            Date timeDate = timeFormatApi.parse(time);
            return timeFormatUser.format(timeDate);
        } catch (ParseException e) {
            e.printStackTrace();

            // Better than nothing :S
            return time.substring(0, 5);
        }
    }

    public String getDurationFormatted(Context context) {
        return getDuration() + " " + context.getString(R.string.minutes);
    }


    public void configureTimeMidnightSafe() {

        try {
            Date timeDate = timeFormatApi.parse(time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timeDate.getTime());
            int timeHour = calendar.get(Calendar.HOUR_OF_DAY);
            if (timeHour < TIME_HOUR_MIDNIGHT_SAFE_THRESHOLD) {
                calendar.add(Calendar.HOUR_OF_DAY, 24);
            }
            setTimeHourMidnightSafe((int) calendar.getTimeInMillis());

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public long getDayId() {

        try {
            Date date = dateFormatApi.parse(day);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(date.getTime());
            int dayInt = calendar.get(Calendar.DAY_OF_MONTH);
            return (long) dayInt;
        } catch (ParseException e) {
            e.printStackTrace();
            throw new IllegalStateException("wrong date");
        }
    }

    @Override
    public int compareTo(@NonNull Object o) {
        return getTimeHourMidnightSafe() - ((Event) o).getTimeHourMidnightSafe();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getBand() {
        return band;
    }

    public void setBand(int band) {
        this.band = band;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Band getBandEntity() {
        return bandEntity;
    }

    public void setBandEntity(Band bandEntity) {
        this.bandEntity = bandEntity;
    }


    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public boolean isStarred() {
        return starred;
    }

    public void setStarred(boolean starred) {
        this.starred = starred;
    }

    public int getTimeHourMidnightSafe() {
        return timeHourMidnightSafe;
    }

    public void setTimeHourMidnightSafe(int timeHourMidnightSafe) {
        this.timeHourMidnightSafe = timeHourMidnightSafe;
    }

}
