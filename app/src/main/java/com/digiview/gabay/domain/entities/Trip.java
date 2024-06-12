package com.digiview.gabay.domain.entities;

public class Trip {
    public Trip() {}

    public Trip(String trip_id, String trip_name, Double trip_budget, String trip_date) {
        this.trip_id = trip_id;
        this.trip_name = trip_name;
        this.trip_budget = trip_budget;
        this.trip_date = trip_date;
    }
    public String trip_id;
    public String trip_name;
    public Double trip_budget;
    public String trip_date;
}
