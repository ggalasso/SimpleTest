package com.ggalasso.simpletest;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by truthd on 8/6/2015.
 */

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
