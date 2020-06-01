package com.upgrad.FoodOrderingApp.service.common;

public enum ItemType {
    VEG("VEG"),

    NON_VEG("NON_VEG");
    private String value;

    ItemType(String value) {
        this.value = value;
    }
}
