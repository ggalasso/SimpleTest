package com.ggalasso.BggCollectionManager.controller;

import android.util.Log;

import com.ggalasso.BggCollectionManager.model.GameId;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;

@Root(name="items")
public class GameIdManager {

    private static GameIdManager ourInstance = null;

    @ElementList(entry = "item", inline = true, required = false)
    private ArrayList<GameId> gameIds;

    private GameIdManager() {
        Log.i("INFO", "Instantiated GameId Manager");
    }

    public static GameIdManager getInstance() {
        if (ourInstance == null) {
            synchronized (GameIdManager.class) {
                if (ourInstance == null) {
                    ourInstance = new GameIdManager();
                }
            }
        }
        return ourInstance;
    }

    public ArrayList<GameId> getGameIds() {
        return gameIds;
    }

    public String getIdListString() {
        String idList = "";
        for (GameId game : getGameIds()) {
            if (idList == "") {
                idList = game.getObjectid();
            } else {
                idList += "," + game.getObjectid();
            }
        }
        return idList;
    }
}
