package com.ggalasso.BggCollectionManager.controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.ggalasso.BggCollectionManager.api.ImageService;
import com.ggalasso.BggCollectionManager.api.XMLApi;
import com.ggalasso.BggCollectionManager.db.BoardGameTable;
import com.ggalasso.BggCollectionManager.db.CategoryInGameTable;
import com.ggalasso.BggCollectionManager.db.CategoryTable;
import com.ggalasso.BggCollectionManager.db.MechanicInGameTable;
import com.ggalasso.BggCollectionManager.db.MechanicTable;
import com.ggalasso.BggCollectionManager.db.Schema.CategoryHelper;
import com.ggalasso.BggCollectionManager.db.Schema.CategoryInGameHelper;
import com.ggalasso.BggCollectionManager.db.Schema.MechanicInGameHelper;
import com.ggalasso.BggCollectionManager.model.APIBoardGames;
import com.ggalasso.BggCollectionManager.model.BoardGame;
import com.ggalasso.BggCollectionManager.model.Link;

import org.junit.experimental.categories.Category;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Root(name = "items")
public class BoardGameManager {

    private static final Object lock = new Object();
    private static volatile BoardGameManager ourInstance = null;
    @ElementList(entry = "item", inline = true)
    private ArrayList<BoardGame> BoardGames;
    private List<String> apiIdArray;
    private BoardGameTable bgt;
    private CategoryTable catt;
    private MechanicTable mecht;
    private CategoryInGameTable cigt;
    private MechanicInGameTable migt;

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

    private void initializeTables(Context ctx){
        bgt = new BoardGameTable(ctx);
        catt = new CategoryTable(ctx);
        mecht = new MechanicTable(ctx);
        cigt = new CategoryInGameTable(ctx);
        migt = new MechanicInGameTable(ctx);
    }

    public BoardGame getBoardGameById(String id) {
        for (BoardGame game : getBoardGames()) {
            if (game.getId().equals(id)) {
                return game;
            }
        }
        return null;
    }

    public ArrayList<BoardGame> getBoardGames() {
        return BoardGames;
    }

    private void setBoardGames(ArrayList<BoardGame> bgList) {
        this.BoardGames = bgList;
    }

    private void setBoardGamesFromDB() {
        setBoardGames(bgt.fetchAllBoardGames());
    }

    private void setBoardGamesFromAPI(String username) {
        XMLApi xapi = new XMLApi(GameIdManager.class, "https://boardgamegeek.com/xmlapi2/collection?username=" + username + "&own=1");
        GameIdManager gim = (GameIdManager) xapi.getAPIManager();
        String download2 = "https://boardgamegeek.com/xmlapi2/thing?id=" + gim.getIdListString() + "&stats=1";
        xapi = new XMLApi(APIBoardGames.class, download2);
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

    private ArrayList<Link> getCategoryLinks() {
        return getCategoryLinks(getBoardGames());
    }

    private ArrayList<Link> getCategoryLinks(ArrayList<BoardGame> boardGames) {
        ArrayList<Link> categoryLinks = new ArrayList<>();
        for (BoardGame bg : boardGames) {
            categoryLinks.addAll(bg.getCategoryLinks());
        }
        return categoryLinks;
    }

    private ArrayList<Link> getMechanicLinks() {
        return getMechanicLinks(getBoardGames());
    }

    private ArrayList<Link> getMechanicLinks(ArrayList<BoardGame> boardGames) {
        ArrayList<Link> mechanicLinks = new ArrayList<>();
        for (BoardGame bg : boardGames) {
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

    private void insertNewAPIMechanicsInGame(ArrayList<BoardGame> listOfGamesToSave){
        MechanicInGameTable cigt = new MechanicInGameTable();
        ArrayList<String> mechs = new ArrayList<>();
        Map<String,ArrayList<String>> bgMechMap = new HashMap<>();
        for (BoardGame game : listOfGamesToSave){
            for (Link mech : game.getCategoryLinks()){
                mechs.add(mech.getId());
            }
            bgMechMap.put(game.getId(), mechs);
        }
        cigt.insertAllMechanicsInGame(bgMechMap);
    }

    private void insertNewAPICategoriesInGame(ArrayList<BoardGame> listOfGamesToSave){
        CategoryInGameTable cigt = new CategoryInGameTable();
        ArrayList<String> cats = new ArrayList<>();
        Map<String,ArrayList<String>> bgCatMap = new HashMap<>();
        for (BoardGame game : listOfGamesToSave){
            for (Link cat : game.getCategoryLinks()){
                cats.add(cat.getId());
            }
            bgCatMap.put(game.getId(), cats);
        }
        cigt.insertAllCatergoriesInGame(bgCatMap);
    }

    private void insertNewAPIMechanics(ArrayList<BoardGame> listOfGamesToSave){
        Map<String, String> mechanicMap = new HashMap<>();
        Map<String, String> newMechMap = new HashMap<>();
        MechanicTable mech = new MechanicTable();
        ArrayList<String> dbMechanics = mech.fetchAllMechanicIds();

        // Build categoryMap to get unique categories from API Games
        for (Link link : getMechanicLinks(listOfGamesToSave)) {
            mechanicMap.put(link.getId(), link.getValue());
        }

        // Loop through the new categories and save any ones that are not in the database
        for (Map.Entry<String, String> mechanic : mechanicMap.entrySet()) {
            String mechId = mechanic.getKey();
            String mechVal = mechanic.getValue();
            Log.d("BGCM-BGM", "Id: " + mechId + " Mech: " + mechVal);
            if (!dbMechanics.contains(mechId)) {
                Log.d("BGCM-BGM", "Inserting new category with id:" + mechId + " and val: " + mechVal);
                newMechMap.put(mechId, mechVal);
            }
        }

        if (!newMechMap.isEmpty()) {
            mech.syncMechanics(newMechMap);
        }
    }

    private void insertNewAPICategories(ArrayList<BoardGame> listOfGamesToSave) {
        Map<String, String> categoryMap = new HashMap<>();
        Map<String, String> newCatMap = new HashMap<>();
        CategoryTable cat = new CategoryTable();
        ArrayList<String> dbCategories = cat.fetchAllCategoryIds();

        // Build categoryMap to get unique categories from API Games
        for (Link link : getCategoryLinks(listOfGamesToSave)) {
            categoryMap.put(link.getId(), link.getValue());
        }

        // Loop through the new categories and save any ones that are not in the database
        for (Map.Entry<String, String> category : categoryMap.entrySet()) {
            String catId = category.getKey();
            String catVal = category.getValue();
            Log.d("BGCM-BGM", "Id: " + catId + " Cat: " + catVal);
            if (!dbCategories.contains(catId)) {
                Log.d("BGCM-BGM", "Inserting new category with id:" + catId + " and val: " + catVal);
                newCatMap.put(catId, catVal);
            }
        }

        if (!newCatMap.isEmpty()) {
            cat.syncCategories(newCatMap);
        }
    }

    private Map<String, String> getUniqueMechanics() {
        Map<String, String> mechanicMap = new HashMap<>();
        for (Link link : getMechanicLinks()) {
            mechanicMap.put(link.getId(), link.getValue());
        }
        return mechanicMap;
    }

    private Map<String, ArrayList<String>> getAllBoardGameCategories() {
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

    private Map<String, ArrayList<String>> getAllBoardGameMechanics() {
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

    private void syncShallow(String username) {
        ImageService is = new ImageService();

        String newGames = getListOfNewAPIGames(username);
        //No new games
        if (!newGames.isEmpty()) {
            ArrayList<BoardGame> apiGamesNoteInDB = getAPIGamesNotInDB(newGames).getBoardGames();
            //Pass the list of new games to be saved to the DB and file system.
            saveAllBoardGameData(apiGamesNoteInDB);
        }

        ArrayList<String> dbGamesNotInAPI = getDBGamesNotInAPI();
        for (String id : dbGamesNotInAPI) {
            //Game id to be deleted
            deleteGameById(id);
        }

        //Remove any catagories or mechanics if we deleted the only game associated with them
        ArrayList<String> orphanedCats = catt.getOrphanedCategories();

        ArrayList<String> orphanedMecs = mecht.getOrphanedMechanics();

//        ArrayList<BoardGame> dbGames = bgt.fetchAllBoardGames();
//        Map<String, BoardGame> bgMap = markAPIvsDB(apiGames, dbGames);
//        syncShallowIteratorComparison(bgMap, bgt);

        // TODO: Get Db Games to find games that need to be deleted from the database, which are the ones not in the
        // API Call.
        // Then Get the DB Games, combine them with the API Games from earlier in this code and set it to the
        // BoardGameManager Collection.

        //for (BoardGame bg : BoardGames) {
        //    bg.setThumbnailPath(is.getImgStorageDir() + File.separator + is.getFileNameFromURL(bg.getThumbnailURL()));
        //}

        //Assume DB is all in sync, but probably need to do more work above this to ensure it's all correct at this call
        setBoardGamesFromDB();
    }

    private String getListOfNewAPIGames(String username) {
        XMLApi xapi = new XMLApi(GameIdManager.class, "https://boardgamegeek.com/xmlapi2/collection?username=" + username + "&own=1");
        GameIdManager gim = (GameIdManager) xapi.getAPIManager();
        String newGameIdString = "";

        //List<String> apiIdArray = Arrays.asList(gim.getIdListString().split(","));
        apiIdArray = Arrays.asList(gim.getIdListString().split(","));
        ArrayList<String> dbIdArray = getDBGameIds();

        // Adding new games from API to DB
        Set<String> dbGameSet = new HashSet<String>(dbIdArray);
        Set<String> addGameSet = new HashSet<String>();
        for (String id : apiIdArray) {
            if (!dbGameSet.contains(id)) {
                addGameSet.add(id);
            }
        }
        for (String id : addGameSet) {
            if (newGameIdString.isEmpty()) {
                newGameIdString = id;
            } else {
                newGameIdString += "," + id;
            }
        }
        return newGameIdString;
    }

    private APIBoardGames getAPIGamesNotInDB(String newGameIdString) {
        String download = "https://boardgamegeek.com/xmlapi2/thing?id=" + newGameIdString + "&stats=1";
        XMLApi xapi = new XMLApi(APIBoardGames.class, download);
        return (APIBoardGames) xapi.getAPIManager();
    }

    private ArrayList<String> getDBGamesNotInAPI() {
        //We were calling gim.getIdListString() again here, but it was failing because it gim was null due to simple
        //directly accessing the constructor of gim rather than calling the getInstance method. So instead we saved a
        //local copy of the apiIdList into the bgm.
        ArrayList<String> dbIdArray = getDBGameIds();
        //Find the list of id's that are not in the API but are in the DB
        Set<String> apiGameSet = new HashSet<String>(apiIdArray);
        ArrayList<String> gamesNotInAPI = new ArrayList<String>();
        Integer deletedCount = 0;
        for (String id : dbIdArray) {
            if (!apiGameSet.contains(id)) {
                //Add the game id to the ArrayList so we can return it
                gamesNotInAPI.add(deletedCount, id);
                deletedCount++;
            }
        }
        return gamesNotInAPI;
    }

    private int getDBNumberOfGames() {
        return bgt.fetchBoardGameCount();
    }

    private ArrayList<String> getDBGameIds() {
        return bgt.fetchAllGameIds();
    }

    private void syncDeep(String username) {
        deleteAllBoardGameData();
        setBoardGamesFromAPI(username);
        saveAllBoardGameData(this.getBoardGames());
    }

    private void saveAllBoardGameData(ArrayList<BoardGame> listOfGamesToSave) {
        ImageService is = new ImageService();

        for (BoardGame game : listOfGamesToSave) {
            saveImage(is, game);
            bgt.insert(game);
        }

        // TODO : Currently choking on the insertNewAPIMechanicsInGame... need to root cause it.
        //        session Monday, 10/24/16)
        insertNewAPICategories(listOfGamesToSave);
        insertNewAPIMechanics(listOfGamesToSave);
        insertNewAPICategoriesInGame(listOfGamesToSave);
        insertNewAPIMechanicsInGame(listOfGamesToSave);
    }

    private void deleteAllBoardGameData() {
        deleteAllGameDataFromTables();
        deleteAllGameImagesFromFile();
    }

    private void deleteAllGameImagesFromFile() {
        ImageService is = new ImageService();
        is.deleteImageDirectory();
    }

    private void deleteAllGameDataFromTables() {
        bgt.deleteAllRowsFromTable();
        cigt.deleteAllRowsFromTable();
        migt.deleteAllRowsFromTable();
        catt.deleteAllRowsFromTable();
        mecht.deleteAllRowsFromTable();
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

    private void deleteGameById(String id) {
        cigt.fetchTableCount(CategoryInGameHelper.getTableName());
        migt.fetchTableCount(MechanicInGameHelper.getTableName());

        //TODO Need to create a statement to grab the games in the category or mechanic table that aren't listed in
        // the in game helper tables.
        cigt.deleteFromTableWhere(CategoryInGameHelper.getTableName(), "cg_bg_id = " + id);
        migt.deleteFromTableWhere(MechanicInGameHelper.getTableName(), "mg_bg_id = " + id);
        bgt.delete(id);

        cigt.fetchTableCount(CategoryInGameHelper.getTableName());
        migt.fetchTableCount(MechanicInGameHelper.getTableName());
    }

    public void loadBoardGameCollection(String username, Context ctx) {
        initializeTables(ctx);
        boolean request_to_delete_everything = false;
        XMLApi xapi = new XMLApi(GameIdManager.class, "https://boardgamegeek.com/xmlapi2/collection?username=" + username + "&own=1");
        GameIdManager gim = (GameIdManager) xapi.getAPIManager();

        if (request_to_delete_everything || getDBNumberOfGames() == 0) {
            syncDeep(username);
        } else {
            syncShallow(username);
        }
        //deleteGameById("1032");
        //Log.d("BGCM-MA", "Found the following id's to retrieve details from the API: " + newGameIdString);
        //if (!newGameIdString.isEmpty()) {
        //setBoardGamesFromAPI(newGameIdString);
        //}
        //syncBoardGameCollection(ctx);
    }
}
