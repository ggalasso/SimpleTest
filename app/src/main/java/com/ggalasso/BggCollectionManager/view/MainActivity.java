package com.ggalasso.BggCollectionManager.view;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.ggalasso.BggCollectionManager.R;
import com.ggalasso.BggCollectionManager.api.XMLApi;
import com.ggalasso.BggCollectionManager.controller.BoardGameManager;
import com.ggalasso.BggCollectionManager.controller.GameIdManager;
import com.ggalasso.BggCollectionManager.db.BoardGameTable;
import com.ggalasso.BggCollectionManager.db.CategoryInGameTable;
import com.ggalasso.BggCollectionManager.db.CategoryTable;
import com.ggalasso.BggCollectionManager.db.MechanicInGameTable;
import com.ggalasso.BggCollectionManager.db.MechanicTable;
import com.ggalasso.BggCollectionManager.model.BoardGame;
import com.ggalasso.BggCollectionManager.model.Link;


import java.util.ArrayList;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    Context ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BoardGameTable bgtCon = new BoardGameTable(ctx);
        CategoryTable catCon = new CategoryTable(ctx);
        CategoryInGameTable cigtCon = new CategoryInGameTable(ctx);
        MechanicTable metCon = new MechanicTable(ctx);
        MechanicInGameTable migtCon = new MechanicInGameTable(ctx);

//        bgtCon.destroyEverything();
        XMLApi xapi = new XMLApi(GameIdManager.class, "https://boardgamegeek.com/xmlapi2/collection?username=brickedphoneclub&own=1");
        GameIdManager gim = (GameIdManager)xapi.getAPIManager();
        String download2 = "https://boardgamegeek.com/xmlapi2/thing?id=" + gim.getIdListString() + "&stats=1";
        xapi = new XMLApi(BoardGameManager.class, download2);
        BoardGameManager bgm = (BoardGameManager)xapi.getAPIManager();
        bgm.getIdListString();

//        CollectionAPI capi = new CollectionAPI();
//        GameIdManager gim = capi.getIDManager();

        //BoardGameManager bgm2 = BoardGameManager.getInstance();

//        ThingAPI tapi = new ThingAPI();
//        BoardGameManager bgm = tapi.getGameManager(gim.getIdListString());

        bgtCon.syncBoardGameCollection(bgm.getBoardGames());

        ArrayList<BoardGame> bgList = bgtCon.fetchAllBoardGames();

        ArrayList<Link> caLinks = bgm.getCategoryLinks();
        for (Link link : caLinks) {
            Log.d("BCGM-MA", "Category link is: " + link.getValue() + " id: " + link.getId() + " and type: " + link.getType());
        }

        catCon.delete("1002");
        catCon.delete("1001");
        catCon.delete("2145");

        Map<String, String> uniqueCategoriesMap = bgm.getUniqueCategories();
        catCon.syncCategories(uniqueCategoriesMap);

        Map<String, ArrayList<String>> categoriesInGame = bgm.getAllBoardGameCategories();
        cigtCon.insertAllCatergoriesInGame(categoriesInGame);

        Map<String, String> uniqueMechanicMap = bgm.getUniqueMechanics();
        metCon.syncMechanics(uniqueMechanicMap);

        Map<String, ArrayList<String>> mechanicsInGame = bgm.getAllBoardGameMechanics();
        migtCon.insertAllMechanicsInGame(mechanicsInGame);
        /////////////////////////catCon.syncCategories(uniqueCategoriesMap);


//        Map<String, String> categoryMap = new HashMap<>();
//        for (Link link : caLinks) {
//            categoryMap.put(link.getId(), link.getValue());
//        }

//        Map<String,Map<String,Integer>> categoryMap = new HashMap<String,Map<String,Integer>>();
//        for (Link link : caLinks){
//            String id = link.getId();
//            String categoryType = link.getValue();
//            Map<String,Integer> innerMap = new HashMap<>();
//
//            if (categoryMap.containsKey(id)) {
//                innerMap = categoryMap.get(id);
//                Integer numOf =  innerMap.get(categoryType);
//                innerMap.put(categoryType, ++numOf);
//                categoryMap.put(id, innerMap);
//            } else {
//                innerMap.put(categoryType, 1);
//                categoryMap.put(id, innerMap);
//            }
//        }
//
//        Iterator itr = categoryMap.entrySet().iterator();
//        while (itr.hasNext()){
//            Map.Entry pair = (Map.Entry)itr.next();
//            String id = (String)pair.getKey();
//            Map<String,Integer> innerMap = (Map)pair.getValue();
//
//            Iterator itr2 = innerMap.entrySet().iterator();
//            Map.Entry pair2 = (Map.Entry)itr2.next();
//            String categoryType = (String)pair2.getKey();
//            Integer numOf = (Integer)pair2.getValue();
//            Log.d("BGCM-MA", "Id: " + id + " Name: " + categoryType + " Number Of: " + numOf);
//            itr.remove();
//        }


//        if (bgList.size() > 0) {
//            for (BoardGame bg : bgList) {
//                Log.d("BGCM-MA", "---------------------------------");
//                Log.d("BGCM-MA", "ID: " + bg.getId());
//                Log.d("BGCM-MA", "Name: " + bg.getPrimaryName());
//                Log.d("BGCM-MA", "Year Published: " + bg.getYearPub());
//                Log.d("BGCM-MA", "Description: " + bg.getDescription().substring(0, 75));
//                Log.d("BGCM-MA", "Thumbnail: " + bg.getThumbnail());
//                Log.d("BGCM-MA", "Image: " + bg.getImage());
//                Log.d("BGCM-MA", "Rating: " + bg.getRating());
//                Log.d("BGCM-MA", "Rank: " + bg.getRank());
//                Log.d("BGCM-MA", "Min Players: " + bg.getMinPlayers());
//                Log.d("BGCM-MA", "Max Players: " + bg.getMaxPlayers());
//                Log.d("BGCM-MA", "Play Time: " + bg.getPlayTime());
//                Log.d("BGCM-MA", "Min Time: " + bg.getMinTime());
//                Log.d("BGCM-MA", "Max Time: " + bg.getMaxTime());
//                Log.d("BGCM-MA", "Min Age: " + bg.getMinAge());
//            }
//            Log.d("BGCM-MA", "TOTAL Board Games in DB: " + bgList.size());
//        }
        bgtCon.destroyEverything();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
