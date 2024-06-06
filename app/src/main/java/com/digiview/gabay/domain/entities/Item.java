package com.digiview.gabay.domain.entities;

public class Item {
    public Item() {}
    public Item(String item_id, String trip_id, String category_id, String item_name, Double price) {
        this.item_id = item_id;
        this.trip_id = trip_id;
        this.category_id = category_id;
        this.item_name = item_name;
        this.price = price;
    }

    String item_id;
    String trip_id;
    String category_id;
    String item_name;
    Double price;
}
