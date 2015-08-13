package com.ggalasso.simpletest;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by truthd on 8/6/2015.
 */

@Root(name="maintag")
public class maintag {

    @ElementList(entry="item", inline=true)
    private List<item> items;

//    public maintag() {};

    public List<item> getItems() {
        return items;
    }

}
