package com.ggalasso.BggCollectionManager.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

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

    public Link(String value, String id, String type) {
        this.value = value;
        this.id = id;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
