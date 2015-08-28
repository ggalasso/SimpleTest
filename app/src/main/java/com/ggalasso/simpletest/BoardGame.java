package com.ggalasso.simpletest;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;


@Root
public class BoardGame {

    @Element(required=false)
    private String name;
    @Element(name="yearpublished", required=false)
    private String yearPub;
//    @Attribute(required=false)
//    private String objectid;
    @Element(required=false)
    private String description;

    private double rating;

    public String getName() {
        return name;
    }

    public String getYeapublished() {
        return yearPub;
    }

    public String getDescription() {
        return description;
    }

    public double getRating() {
        return rating;
    }


}
