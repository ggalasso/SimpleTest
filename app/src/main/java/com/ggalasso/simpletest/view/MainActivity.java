package com.ggalasso.simpletest.view;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.ggalasso.simpletest.R;
import com.ggalasso.simpletest.api.CollectionAPI;
import com.ggalasso.simpletest.api.ThingAPI;
import com.ggalasso.simpletest.controller.BoardGameManager;
import com.ggalasso.simpletest.controller.GameIDManager;
import com.ggalasso.simpletest.db.BoardGameTable;
import com.ggalasso.simpletest.db.DBhelper;
import com.ggalasso.simpletest.db.SQLController;
import com.ggalasso.simpletest.model.BoardGame;
import com.ggalasso.simpletest.model.GameID;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {
    Context ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean result = false;

        //SQLController dbCon = new SQLController(ctx);
        BoardGameTable bgtCon = new BoardGameTable(ctx);

        CollectionAPI capi = new CollectionAPI();
        ThingAPI tapi = new ThingAPI();

        GameIDManager gim = GameIDManager.getInstance();
        BoardGameManager bgm = BoardGameManager.getInstance();

        try {
            gim = capi.getIDList();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            bgm = tapi.getDetail(gim.getIdListString());
            //Log.i("MY", "blah");
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //bgtCon.open();
        bgtCon.dropAllTables();
        bgtCon.testTables();
        bgtCon.createAllTables();
        //bgtCon.testTables();

        // 09/20/15 - GAG - To remove everything from the board_game table use the deleteAll
        //bgtCon.deleteAll();

        ArrayList<GameID> apiList = gim.getGameIDs();
        String ID;

        for (GameID gi : apiList) {
            ID = gi.getObjectid();

            Log.d("BGCM-MA", "ID: " + bgm.getBoardGameById(ID).getId());
            Log.d("BGCM-MA", "Name: " + bgm.getBoardGameById(ID).getPrimaryName());

            try {

                result = bgtCon.isBoardGameInTable(ID);

                if (result) {
                    Log.i("BGCM-MA", "found the boardgame to already exist");
                    bgtCon.update(bgm.getBoardGameById(ID));
                } else {
                    Log.i("BGCM-MA", "found new boardgame to insert in the database");
                    bgtCon.insert(bgm.getBoardGameById(ID));
                }

                //bgtCon.delete(bgm.getBoardGameById("35052").getId());
                //bgtCon.delete(bgm.getBoardGameById("35052"));

            } catch (SQLiteConstraintException e) {
                //e.printStackTrace();
            } catch (NullPointerException e) {
                //e.printStackTrace();
            }

        }
        //Iterate over database

        ArrayList<BoardGame> bgList = bgtCon.fetchAllBoardGames();

        if (bgList.size() > 0) {
            for (BoardGame bg : bgList) {
                Log.d("BGCM-MA", "ID: " + bg.getId());
                Log.d("BGCM-MA", "Name: " + bg.getPrimaryName());
            }
            Log.d("BGCM-MA", "TOTAL: " + bgList.size());
        }

        //bgtCon.close();

        // Delete Board Game from Table by ID as a string
        bgtCon.delete(bgm.getBoardGameById("35052").getId());

        // Delete Board Game from Table by passing Board Game object
        bgtCon.delete(bgm.getBoardGameById("153938"));


        //09-20-15 GAG - Fetch all game ID's and print them out.
        ArrayList<String> gameIDList = bgtCon.fetchAllGameIds();
        if (gameIDList.size() > 0) {
            for ( String id : gameIDList) {
                Log.d("BGCM-MA", "Array ID: " + id); }
        }


        //Log.i("MY ERROR", "BoardGame: " + bgm.getIdListString());
        //Log.i("My Stuff", "Blah");
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
