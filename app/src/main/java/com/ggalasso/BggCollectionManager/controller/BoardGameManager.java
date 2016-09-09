package com.ggalasso.BggCollectionManager.controller;

import android.content.Context;
import android.util.Log;

import com.ggalasso.BggCollectionManager.api.ImageService;
import com.ggalasso.BggCollectionManager.api.XMLApi;
import com.ggalasso.BggCollectionManager.db.BoardGameTable;
import com.ggalasso.BggCollectionManager.db.CategoryInGameTable;
import com.ggalasso.BggCollectionManager.db.CategoryTable;
import com.ggalasso.BggCollectionManager.db.MechanicInGameTable;
import com.ggalasso.BggCollectionManager.db.MechanicTable;
import com.ggalasso.BggCollectionManager.model.APIBoardGames;
import com.ggalasso.BggCollectionManager.model.BoardGame;
import com.ggalasso.BggCollectionManager.model.Link;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Root(name = "items")
public class BoardGameManager {

    private static final Object lock = new Object();
    private static volatile BoardGameManager ourInstance = null;
    @ElementList(entry = "item", inline = true)
    private ArrayList<BoardGame> BoardGames;
    private Context ctx;

    //private BoardGameManager() { Log.i("BGCM-BGM", "Instantiated BoardGameManager"); }

    public static BoardGameManager getInstance() {
        BoardGameManager bgm = ourInstance;
        if (bgm == null) {
            synchronized (lock) {
                bgm = ourInstance;
                if (bgm == null) {
                    bgm = new BoardGameManager();
                    ourInstance = bgm;
                }
            }
        }
        return bgm;
    }

    public BoardGame getBoardGameById(String id) {
        for (BoardGame game : getBoardGames()) {
            if (game.getId().equals(id)) {
                return game;
            }
        }
        return null;
    }

    public Context getCtx() {
        return ctx;
    }

    public void setCtx(Context ctx) {
        this.ctx = ctx;
    }

    public ArrayList<BoardGame> getBoardGames() {
        return BoardGames;
    }

    public void setBoardGames(ArrayList<BoardGame> bgList) {
        this.BoardGames = bgList;
    }

    public void setBoardGamesFromDB(Context ctx) {
        BoardGameTable bgt = new BoardGameTable(ctx);
        setBoardGames(bgt.fetchAllBoardGames());
    }

    public void setBoardGamesFromAPI(String idList) {
        String download2 = "https://boardgamegeek.com/xmlapi2/thing?id=" + idList + "&stats=1";
        XMLApi xapi = new XMLApi(APIBoardGames.class, download2);
        APIBoardGames abgs = (APIBoardGames) xapi.getAPIManager();
        setBoardGames(abgs.getBoardGames());
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

        for (BoardGame bg : getBoardGames()) {
            String id = bg.getId();
            ArrayList<Link> categories = bg.getCategoryLinks();
            ArrayList<String> categoryIds = new ArrayList<>();
            for (Link link : categories) {
                categoryIds.add(link.getId());
            }
            bg_categories.put(id, categoryIds);
        }

        return bg_categories;
    }

    public Map<String, ArrayList<String>> getAllBoardGameMechanics() {
        Map<String, ArrayList<String>> bg_mechanics = new HashMap<>();

        for (BoardGame bg : getBoardGames()) {
            String id = bg.getId();
            ArrayList<Link> mechanics = bg.getMechanicLinks();
            ArrayList<String> mechanicsIds = new ArrayList<>();
            for (Link link : mechanics) {
                mechanicsIds.add(link.getId());
            }
            bg_mechanics.put(id, mechanicsIds);
        }

        return bg_mechanics;
    }

    public void syncBoardGameCollection(Context ctx) {
        this.ctx = ctx;
        ImageService is = new ImageService();
        ArrayList<BoardGame> apiGames = getBoardGames();
        BoardGameTable bgt = new BoardGameTable(ctx);
        Integer rowCount = bgt.fetchBoardGameCount();

        if (rowCount > 0) {
            ArrayList<BoardGame> dbGames = bgt.fetchAllBoardGames();
            Map<String, BoardGame> bgMap = markAPIvsDB(apiGames, dbGames);
            syncShallowIteratorComparison(bgMap, bgt);
            for (BoardGame bg : BoardGames) {
                bg.setThumbnailPath(is.getImgStorageDir() + File.separator + is.getFileNameFromURL(bg.getThumbnailURL()));
            }
        } else {
            syncDeep(apiGames, bgt);
        }


    }

    public int getDBNumberOfGames() {
        BoardGameTable bgt = new BoardGameTable(ctx);
        return bgt.fetchBoardGameCount();
    }

    private void syncDeep(ArrayList<BoardGame> boardGames, BoardGameTable bgt) {
        CategoryTable catCon = new CategoryTable(ctx);
        CategoryInGameTable cigtCon = new CategoryInGameTable(ctx);
        MechanicTable metCon = new MechanicTable(ctx);
        MechanicInGameTable migtCon = new MechanicInGameTable(ctx);
        ImageService is = new ImageService();

        bgt.deleteAllRowsFromTable();

        is.deleteImageDirectory();
        for (BoardGame game : boardGames) {
            saveImage(is, game);
            bgt.insert(game);
        }

        cigtCon.deleteAllRowsFromTable();
        migtCon.deleteAllRowsFromTable();
        catCon.deleteAllRowsFromTable();
        metCon.deleteAllRowsFromTable();

        Map<String, String> uniqueCategoriesMap = getUniqueCategories();
        catCon.syncCategories(uniqueCategoriesMap);

        Map<String, ArrayList<String>> categoriesInGame = getAllBoardGameCategories();
        cigtCon.insertAllCatergoriesInGame(categoriesInGame);

        Map<String, String> uniqueMechanicMap = getUniqueMechanics();
        metCon.syncMechanics(uniqueMechanicMap);

        Map<String, ArrayList<String>> mechanicsInGame = getAllBoardGameMechanics();
        migtCon.insertAllMechanicsInGame(mechanicsInGame);
    }

    private void saveImage(ImageService is, BoardGame game) {
        if (is.getAndStoreImage(game.getThumbnailURL())) {
            game.setThumbnailPath(is.getImgStorageDir() + File.separator + game.getThumbnailURLFileName());
        } else {
            game.setThumbnailPath("nofilepath");
            Log.d("BGCM-BGM", "No file path for: " + game.getPrimaryName());
        }
    }

    protected Map<String, BoardGame> markAPIvsDB(ArrayList<BoardGame> apiGames, ArrayList<BoardGame> dbGames) {
        Map<String, BoardGame> gameMap = new HashMap<String, BoardGame>();
        for (BoardGame game : dbGames) {
            game.setSyncValue("DBOnly");
            gameMap.put(game.getId(), game);
        }
        for (BoardGame game : apiGames) {
            String id = game.getId();
            if (gameMap.containsKey(id)) {
                gameMap.remove(id);
            } else {
                game.setSyncValue("APIOnly");
                gameMap.put(id, game);
            }
        }
        return gameMap;
    }

    private void syncShallowIteratorComparison(Map<String, BoardGame> bgMap, BoardGameTable bgt) {
        ImageService is = new ImageService();
        Integer countDeleted = 0, countInserted = 0;

        for (Map.Entry<String, BoardGame> game : bgMap.entrySet()) {
            BoardGame bg = game.getValue();
            String syncVal = bg.getSyncValue();
            Log.d("BGCM-BGT", "Id: " + bg.getId() + " Val: " + syncVal);
            if (syncVal.equals("DBOnly")) {
                deleteGame(bg, bgt);
                deleteImage(bg);
                countDeleted++;
            } else {
                bgt.insert(bg);
                saveImage(is, bg);
                countInserted++;
            }
        }
        //TODO : Need to also retrieve the mechanics and categories from the DB when using the DB instead of the API
        //When retrieving from the DB we are only getting board games and not mechanics and categories.
        Log.d("BGCM-BGT", "Deleted: " + countDeleted + " Inserted New: " + countInserted);
    }

    private void deleteGame(BoardGame bg, BoardGameTable bgt) {
        bgt.delete(bg);
    }

    private void deleteImage(BoardGame bg) {
        ImageService is = new ImageService();
        File imgFile = new File(bg.getThumbnailPath());
        is.deleteImageFile(imgFile);
    }

    protected ArrayList<BoardGame> findArraylistValuesNotInSecondArraylist(ArrayList<BoardGame> arrayListLeft, ArrayList<BoardGame> arrayListRight) {
        ArrayList<BoardGame> resultList = new ArrayList<>();
        for (BoardGame lgame : arrayListLeft) {
            if (idInArrayList(arrayListRight, lgame.getId()) == false) {
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

    //Development purposes only to clear out all data (both DB and images on disk)
    public void destroyEverything(Context c) {
        BoardGameTable bgt = new BoardGameTable(c);
        ImageService is = new ImageService();
        //Destroy DB
        bgt.destroyDB();
        //Destroy Images
        is.deleteImageDirectory();
    }

}
