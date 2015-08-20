package com.ggalasso.simpletest;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;


@Root(strict=false)
public class item {

    @Element
    private String name;
    @Element(name="yearpublished")
    private String yearPub;

    private double rating;

    public String getName() {
        return name;
    }

    public String getYeapublished() {
        return yearPub;
    }

}
