package com.ggalasso.simpletest;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;


@Root(name="items", strict=false)
public class BoardGameManager {

    @ElementList(entry="item", inline=true)
    private ArrayList<BoardGame> BoardGames;

    public List<BoardGame> getBoardGames() {
        return BoardGames;
    }

}
