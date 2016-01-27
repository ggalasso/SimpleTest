package com.ggalasso.BggCollectionManagerUnitTests.dbTests;

import android.content.Context;
import android.util.Log;

import com.ggalasso.BggCollectionManager.db.BoardGameTable;
import com.ggalasso.BggCollectionManager.model.BoardGame;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

import static java.security.AccessController.getContext;
import static junit.framework.Assert.*;
import static org.junit.Assert.*;

/**
 * Created by Edward on 1/26/2016.
 */
public class BoardGameTableTest extends BoardGameTable {

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testSyncBoardGameCollection() throws Exception {
        // Where passed in BoardGame Array is greater than the fetch boardgame array
        BoardGameTable bgt = new BoardGameTable();
        ArrayList<BoardGame> apiGames = new ArrayList<>();

        BoardGame bg1 = new BoardGame();
        bg1.setId("1001");
        bg1.setPrimaryName("chess");

        BoardGame bg2 = new BoardGame();
        bg2.setId("1002");
        bg2.setPrimaryName("monopoly");

        BoardGame bg3 = new BoardGame();
        bg3.setId("1003");
        bg3.setPrimaryName("poker");

        apiGames.add(bg1);
        apiGames.add(bg2);
        apiGames.add(bg3);

        ArrayList<BoardGame> dbGames = new ArrayList<BoardGame>(apiGames);

        dbGames.remove(0);

        Map<String,BoardGame> bgMap = super.markAPIvsDB(apiGames, dbGames);

        int countDeleted = 0;
        int countInserted = 0;

        //assertEquals(countDeleted, countInserted);

        for (Map.Entry<String,BoardGame> game : bgMap.entrySet()){
            BoardGame bg = game.getValue();
            String syncVal = bg.getSyncValue();
            //Log.d("BGCM-BGT", "Id: " + bg.getId() + " Val: " + syncVal);
            if (syncVal.equals("DBOnly")){
                countDeleted++;
            } else {
                countInserted++;
            }
        }

        assertEquals(0,countDeleted);
        assertEquals(1,countInserted);
    }
}