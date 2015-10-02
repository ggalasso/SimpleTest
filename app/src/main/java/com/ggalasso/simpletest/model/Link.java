package com.ggalasso.simpletest.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import java.util.ArrayList;

/**
 * Created by Chris on 9/3/2015.
 */

@Root
public class Link {

    //@Path("name[@type='alternate']")
    @Attribute
    private String value;
    @Attribute
    private String id;
    @Attribute
    private String type;

    public Link() {
    }

//    public Link(String value, String id, String type) {
    public Link(String value) {
        this.value = value;
        this.id = "1";
        this.type = "Type";
    }

}
