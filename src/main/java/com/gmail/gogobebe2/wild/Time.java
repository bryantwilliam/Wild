package com.gmail.gogobebe2.wild;

import java.util.concurrent.TimeUnit;

public class Time {
    long millis;

    public Time() {
        this(System.currentTimeMillis());
    }

    public Time(long millis) {
        this.millis = millis;
    }

    public String getFormattedTime() {
        return getDays() + " days, " + getHours() + " hours, " + getMinutes() + " minutes and " + getSeconds() + " seconds.";
    }

    public long getDays() {
        return TimeUnit.MILLISECONDS.toDays(millis);
    }

    public long getHours() {
        return TimeUnit.MILLISECONDS.toHours(millis);
    }

    public long getMinutes() {
        return TimeUnit.MILLISECONDS.toMinutes(millis);
    }

    public long getSeconds() {
        return TimeUnit.MILLISECONDS.toSeconds(millis);
    }

    public long getMiliseconds() {
        return this.millis;
    }

    public void setMiliseconds(long millis) {
        this.millis = millis;
    }
}
