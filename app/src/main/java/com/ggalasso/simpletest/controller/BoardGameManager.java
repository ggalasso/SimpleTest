package com.ggalasso.simpletest.controller;

import android.util.Log;

import com.ggalasso.simpletest.model.BoardGame;
import com.ggalasso.simpletest.model.Link;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    public ArrayList<Link> getCategoryLinks() {
        ArrayList<Link> categoryLinks = new ArrayList<>();
        for (BoardGame bg : getBoardGames()) {
            categoryLinks.addAll(bg.getCategoryLinks());
        }
        return categoryLinks;
    }

    public ArrayList<Link> getMechanicLinks() {
        ArrayList<Link> mechanicLinks = new ArrayList<>();
        for (BoardGame bg : getBoardGames()) {
            mechanicLinks.addAll(bg.getMechanicLinks());
        }
        return mechanicLinks;
    }

    public Map<String, String> getUniqueCategories() {
        Map<String, String> categoryMap = new HashMap<>();
        for (Link link : getCategoryLinks()) {
            categoryMap.put(link.getId(), link.getValue());
        }
        return categoryMap;
    }

    public Map<String, String> getUniqueMechanics() {
        Map<String, String> mechanicMap = new HashMap<>();
        for (Link link : getMechanicLinks()) {
            mechanicMap.put(link.getId(), link.getValue());
        }
        return mechanicMap;
    }

    public Map<String, ArrayList<String>> getAllBoardGameCategories() {
        Map<String, ArrayList<String>> bg_categories = new HashMap<>();

        for (BoardGame bg: getBoardGames()) {
            String id = bg.getId();
            ArrayList<Link> categories = bg.getCategoryLinks();
            ArrayList<String> categoryIds = new ArrayList<>();
            for(Link link: categories) {
                categoryIds.add(link.getId());
            }
            bg_categories.put(id, categoryIds);
        }

        return bg_categories;
    }
    public Map<String, ArrayList<String>> getAllBoardGameMechanics() {
        Map<String, ArrayList<String>> bg_mechanics = new HashMap<>();

        for (BoardGame bg: getBoardGames()) {
            String id = bg.getId();
            ArrayList<Link> mechanics = bg.getMechanicLinks();
            ArrayList<String> mechanicsIds = new ArrayList<>();
            for(Link link: mechanics) {
                mechanicsIds.add(link.getId());
            }
            bg_mechanics.put(id, mechanicsIds);
        }

        return bg_mechanics;
    }

}
