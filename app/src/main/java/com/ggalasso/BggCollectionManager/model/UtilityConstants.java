package com.ggalasso.BggCollectionManager.model;

/**
 * Created by ggalasso on 2/21/16.
 */
public enum UtilityConstants {
    TRIM_COMMA(1),
    TRIM_COMMA_AND_SPACE(2);

    private final int id;
    UtilityConstants(int id) {
        this.id = id;
    }
    public int getValue() { return id; }
}
