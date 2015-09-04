package com.ggalasso.simpletest;

import java.util.ArrayList;

/**
 * Attempting to test final in java.
 */
public class TestFinal {

    private final ArrayList<String> myList;
    private int myInt;
    private String name;


    public TestFinal() {
        this.myList = new ArrayList<String>();
        this.name = "Giovanni";
        this.myInt = 25;
    }

    public TestFinal(Integer myInt, String name) {
        this.myInt = myInt;
        this.name = name;
        this.myList = new ArrayList<String>();
    }

}
