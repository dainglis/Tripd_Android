package com.dainglis.trip_planner.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "events",
        foreignKeys = @ForeignKey(
                entity = Trip.class,
                parentColumns = "tripId",
                childColumns = "tripId"),
        indices = {@Index("tripId")})
public class Event {
    @PrimaryKey(autoGenerate = true)
    public int eventId;

    @ColumnInfo(name = "tripId")
    public int tripId;

    @ColumnInfo(name = "eventTitle")
    public String title;

    @ColumnInfo(name = "eventDate")
    public String date;

    Event() {}

    @Ignore
    Event(String title, int tripId) {
        this.title = title;
        this.tripId = tripId;
    }
}
