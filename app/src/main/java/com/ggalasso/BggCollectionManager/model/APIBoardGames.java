package com.ggalasso.BggCollectionManager.model;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;

/**
 * Created by Edward on 7/25/2016.
 */
@Root(name="items")
public class APIBoardGames {
    @ElementList(entry="item", inline=true)
    private ArrayList<BoardGame> BoardGames;

    public ArrayList<BoardGame> getBoardGames() {
        return BoardGames;
    }
}
