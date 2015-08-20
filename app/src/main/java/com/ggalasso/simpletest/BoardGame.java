package com.ggalasso.simpletest;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;


@Root(name="items", strict=false)
public class BoardGame {

    @ElementList(entry="item", inline=true)
    private List<item> items;

//    public maintag() {};

    public List<item> getItems() {
        return items;
    }

}
