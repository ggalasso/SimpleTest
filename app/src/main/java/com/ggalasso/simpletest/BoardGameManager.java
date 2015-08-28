package com.ggalasso.simpletest;

import android.util.Log;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;

@Root(name="items")
public class BoardGameManager {

    private static BoardGameManager ourInstance = null;
    @ElementList(entry="item", inline=true)
    private ArrayList<BoardGame> BoardGames;

    private BoardGameManager() { Log.i("INFO", "Instantiated BoardGameManager"); }

    public static BoardGameManager getInstance() {
        if (ourInstance == null) {
            ourInstance = new BoardGameManager();
        }
        return ourInstance;
    }

    public ArrayList<BoardGame> getBoardGames() {
        return BoardGames;
    }

    public String getIdListString() {
        String idList = "";
        for (BoardGame game : getBoardGames()) {
            if (idList == "") {
                idList = game.getId();
            } else {
                idList += "," + game.getId();
            }
        }
        return idList;
    }

}
