package com.ggalasso.simpletest;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by truthd on 8/6/2015.
 */

@Root
public class item {

    @Element
    private String name;
    @Element
    private String website;

//    public Item (String name, String website) {
//        this.name = name;
//        this.website = website;
//    }
//
//    public Item() {
//        super();
//    }


    public String getName() {
        return name;
    }
    //
//    public void setName(String name) {
//        this.name = name;
//    }
//
    public String getWebsite() {
        return website;
    }
//
//    public void setWebsite(String website) {
//        this.website = website;
//    }
}
