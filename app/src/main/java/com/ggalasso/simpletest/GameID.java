package com.ggalasso.simpletest;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * Created by truthd on 8/27/2015.
 */
@Root
public class GameID {
    @Attribute(required=false)
    private String objectid;

    public String getObjectid() {
        return objectid;
    }

    public void setObjectid(String objectid) {
        this.objectid = objectid;
    }

}
