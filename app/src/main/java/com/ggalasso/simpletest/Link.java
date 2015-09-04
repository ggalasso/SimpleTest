package com.ggalasso.simpletest;

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

}
