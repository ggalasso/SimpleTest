package com.ggalasso.simpletest.view;

import android.content.Context;
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
import com.ggalasso.simpletest.controller.GameIdManager;
import com.ggalasso.simpletest.db.BoardGameTable;
import com.ggalasso.simpletest.model.BoardGame;
import com.ggalasso.simpletest.model.GameId;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {
    Context ctx = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BoardGameTable bgtCon = new BoardGameTable(ctx);
        //bgtCon.destroyEverything();

        CollectionAPI capi = new CollectionAPI();
        GameIdManager gim = capi.getIDManager();

        ThingAPI tapi = new ThingAPI();
        BoardGameManager bgm = tapi.getGameManager(gim.getIdListString());

        bgtCon.syncBoardGameCollection(bgm.getBoardGames());

        ArrayList<BoardGame> bgList = bgtCon.fetchAllBoardGames();

        if (bgList.size() > 0) {
            for (BoardGame bg : bgList) {
                Log.d("BGCM-MA", "---------------------------------");
                Log.d("BGCM-MA", "ID: " + bg.getId());
                Log.d("BGCM-MA", "Name: " + bg.getPrimaryName());
                Log.d("BGCM-MA", "Year Published: " + bg.getYearPub());
                Log.d("BGCM-MA", "Description: " + bg.getDescription().substring(0, 75));
                Log.d("BGCM-MA", "Thumbnail: " + bg.getThumbnail());
                Log.d("BGCM-MA", "Image: " + bg.getImage());
                Log.d("BGCM-MA", "Rating: " + bg.getRating());
                Log.d("BGCM-MA", "Rank: " + bg.getRank());
                Log.d("BGCM-MA", "Min Players: " + bg.getMinPlayers());
                Log.d("BGCM-MA", "Max Players: " + bg.getMaxPlayers());
                Log.d("BGCM-MA", "Play Time: " + bg.getPlayTime());
                Log.d("BGCM-MA", "Min Time: " + bg.getMinTime());
                Log.d("BGCM-MA", "Max Time: " + bg.getMaxTime());
                Log.d("BGCM-MA", "Min Age: " + bg.getMinAge());
            }
            Log.d("BGCM-MA", "TOTAL Board Games in DB: " + bgList.size());
        }

        //bgtCon.destroyEverything();
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
