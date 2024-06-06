package com.digiview.gabay.domain.entities;

public class Trip {
    public Trip() {}

    public Trip(String trip_id, String trip_name, Double budget, String start_date, String end_date) {
        this.trip_id = trip_id;
        this.trip_name = trip_name;
        this.budget = budget;
        this.start_date = start_date;
        this.end_date = end_date;
    }
    String trip_id;
    String trip_name;
    Double budget;
    String start_date;
    String end_date;
}
