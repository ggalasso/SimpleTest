package com.ggalasso.simpletest.controller;

import android.util.Log;

import com.ggalasso.simpletest.model.BoardGame;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;

@Root(name="items")
public class BoardGameManager {

    private static BoardGameManager ourInstance = null;
    @ElementList(entry="item", inline=true)
    private ArrayList<BoardGame> BoardGames;

    private BoardGameManager() { Log.i("BGCM-BGM", "Instantiated BoardGameManager"); }

    public static BoardGameManager getInstance() {
        if (ourInstance == null) {
            synchronized (BoardGameManager.class) {
                if (ourInstance == null) {
                    ourInstance = new BoardGameManager();
                }
            }
        }
        return ourInstance;
    }

    public BoardGame getBoardGameById(String id){
        for (BoardGame game: getBoardGames()){
            if (game.getId().equals(id)){
                return game;
            }
        }
        return null;
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
