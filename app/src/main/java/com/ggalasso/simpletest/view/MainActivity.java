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

        SQLController dbCon = new SQLController(ctx);

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
            Log.i("MY", "blah");
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            dbCon.open();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        ArrayList<GameID> list = gim.getGameIDs();
        String ID;

        for (GameID gi : list)
        {
            ID = gi.getObjectid();

            Log.d("MY", "ID: " + bgm.getBoardGameById(ID).getId());
            Log.d("MY", "Name: " + bgm.getBoardGameById(ID).getPrimaryName());

            try {

                result = dbCon.check(bgm.getBoardGameById(ID));

                if (result) {
                    Log.i("MY", "found the boardgame to already exist");
                    dbCon.update(bgm.getBoardGameById(ID));
                } else {
                    Log.i("MY", "found new boardgame to insert in the database");
                    dbCon.insert(bgm.getBoardGameById(ID));
                }

                //dbCon.delete(bgm.getBoardGameById("35052").getId());
                //dbCon.delete(bgm.getBoardGameById("35052"));

            } catch (SQLiteConstraintException e) {
                //e.printStackTrace();
            } catch (NullPointerException e) {
                //e.printStackTrace();
            }

        }
        //Iterate over database




        Cursor cr = dbCon.fetch();

        if (cr != null && cr.moveToFirst()){
            cr.moveToFirst();
            Log.d("MY", "ID: " + cr.getString(0));
            Log.d("MY", "Name: " + cr.getString(1));
        }

        dbCon.close();

        //Log.i("MY ERROR", "BoardGame: " + bgm.getIdListString());
        Log.i("My Stuff", "Blah");
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
