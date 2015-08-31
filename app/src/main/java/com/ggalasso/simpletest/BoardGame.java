package com.ggalasso.simpletest;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;


@Root
public class BoardGame {
    //Id of the Game, this is required otherwise we shouldn't be able to find the game.
    @Attribute
    private String id;
    @Path("name[1]")
    @Attribute(name="value", required=false)
    private String name;
    @Path("yearpublished")
    @Attribute(name="value", required=false)
    private String yearPub;
    @Element(required=false)
    private String description;
    @Element(name="thumbnail", required=false)
    private String thumbnail;
    @Element(name="image", required=false)
    private String image;
    @Path("statistics/ratings/average")
    @Attribute(name="value", required=false)
    private double rating;
    @Path("minplayers")
    @Attribute(name="value", required=false)
    private int minPlayers;
    @Path("maxplayers")
    @Attribute(name="value", required=false)
    private int maxPlayers;
    @Path("playingtime")
    @Attribute(name="value", required=false)
    private int playTime;
    @Path("minplaytime")
    @Attribute(name="value", required=false)
    private int minTime;
    @Path("maxplaytime")
    @Attribute(name="value", required=false)
    private int maxTime;
    @Path("minage")
    @Attribute(name="value", required=false)
    private int minAge;

    public String getName() {
        return name;
    }

    public String getYeapublished() {
        return yearPub;
    }

    public String getDescription() {
        return description;
    }

    public double getRating() {
        return rating;
    }

    public String getId() {
        return id;
    }

}
