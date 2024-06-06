package com.digiview.gabay.domain.entities;

public class Category {
    public Category() {}

    public Category(String category_id, String category_name, Integer category_icon) {
        this.category_id = category_id;
        this.category_name = category_name;
        this.category_icon = category_icon;
    }
    String category_id;
    String category_name;
    Integer category_icon;
}
