package com.ggalasso.simpletest;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;


@Root(strict=false)
public class BoardGame {

    @Element
    private String name;
    @Element(name="yearpublished")
    private String yearPub;
    @Attribute
    private String objectid;
    @Element(required=false)
    private String description;

    private double rating;

    public String getName() {
        return name;
    }

    public String getYeapublished() {
        return yearPub;
    }

    public String getObjectid() {
        return objectid;
    }

    public String getDescription() {
        return description;
    }

    public double getRating() {
        return rating;
    }


    public void setObjectid(String objectid) {
        this.objectid = objectid;
    }


}
