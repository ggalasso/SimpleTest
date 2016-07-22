package com.ggalasso.BggCollectionManager.controller;

import android.content.Context;
import android.util.Log;

import com.ggalasso.BggCollectionManager.api.ImageService;
import com.ggalasso.BggCollectionManager.db.BoardGameTable;
import com.ggalasso.BggCollectionManager.model.BoardGame;
import com.ggalasso.BggCollectionManager.model.Link;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.File;
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
                    Log.d("BGCM-BGM", "!! Instantiated Board Game Manager for the first time.");
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

    public void setBoardGames(Context ctx) {
        BoardGameTable bgt = new BoardGameTable(ctx);
        BoardGames = bgt.fetchAllBoardGames();
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

    public void getAndSaveAllImages(){
        ImageService is = new ImageService();
        is.getImgStorageDir();
    }

    public void syncBoardGameCollection(Context ctx) {
        ArrayList<BoardGame> apiGames = getBoardGames();
        BoardGameTable bgt = new BoardGameTable(ctx);
        Integer rowCount = bgt.fetchBoardGameCount();

        if (rowCount > 0) {
            ArrayList<BoardGame> dbGames = bgt.fetchAllBoardGames();
            Map<String,BoardGame> bgMap = markAPIvsDB(apiGames, dbGames);
            syncShallowIteratorComparison(bgMap);
        } else {
            syncDeep(apiGames, bgt);
        }
    }

    private void syncDeep(ArrayList<BoardGame> boardGames, BoardGameTable bgt) {
        ImageService is = new ImageService();
        bgt.deleteAllRowsFromTable();
        is.deleteImageDirectory();
        for(BoardGame game : boardGames) {
            saveImage(is, game);
            bgt.insert(game);
        }
    }

    private void saveImage(ImageService is, BoardGame game) {
        if (is.getAndStoreImage(game.getThumbnailURL())) {
            game.setThumbnailPath(is.getImgStorageDir() + File.separator + game.getThumbnailURLFileName());
        } else {
            game.setThumbnailPath("nofilepath");
            Log.d("BGCM-BGM","No file path for: " + game.getPrimaryName());
        }
    }

    protected Map<String,BoardGame> markAPIvsDB(ArrayList<BoardGame> apiGames, ArrayList<BoardGame> dbGames) {
        Map<String,BoardGame> gameMap = new HashMap<String,BoardGame>();
        for (BoardGame game : dbGames){
            game.setSyncValue("DBOnly");
            gameMap.put(game.getId(),game);
        }
        for (BoardGame game : apiGames){
            String id = game.getId();
            if (gameMap.containsKey(id)){
                gameMap.remove(id);
            } else {
                game.setSyncValue("APIOnly");
                gameMap.put(id, game);
            }
        }
        return gameMap;
    }

    private void syncShallowIteratorComparison(Map<String,BoardGame> bgMap) {
        Integer countDeleted = 0, countInserted = 0;

        for (Map.Entry<String,BoardGame> game : bgMap.entrySet()){
            BoardGame bg = game.getValue();
            String syncVal = bg.getSyncValue();
            Log.d("BGCM-BGT", "Id: " + bg.getId() + " Val: " + syncVal);
            if (syncVal.equals("DBOnly")){
                //delete(bg);
                countDeleted++;
            } else {
                //insert(bg);
                countInserted++;
            }
        }

        Log.d("BGCM-BGT","Deleted: " + countDeleted + " Inserted New: " + countInserted);
    }

    protected ArrayList<BoardGame> findArraylistValuesNotInSecondArraylist(ArrayList<BoardGame> arrayListLeft, ArrayList<BoardGame> arrayListRight){
        ArrayList<BoardGame> resultList = new ArrayList<>();
        for (BoardGame lgame: arrayListLeft) {
            if(idInArrayList(arrayListRight, lgame.getId()) == false){
                resultList.add(lgame);
            }
        }
        return resultList;
    }

    //Check the listToCheck to see if it contains the same id for the idToCheck
    private boolean idInArrayList(ArrayList<BoardGame> listToCheck, String idToCheck) {
        for (BoardGame bg : listToCheck) {
            String gameId = bg.getId();
            if (gameId.equals(idToCheck)) {
                return true;
            }
        }
        return false;
    }

}
