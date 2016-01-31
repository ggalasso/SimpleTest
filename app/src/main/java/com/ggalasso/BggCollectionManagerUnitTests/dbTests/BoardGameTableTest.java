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
    public void testCountDeletedResult() throws Exception {
        insertResults results = syncBoardGameCollection();
        assertEquals(0, results.getCountDeleted());
    }

    @Test
    public void testCountInsertedResult() throws Exception {
        insertResults results = syncBoardGameCollection();
        assertEquals(1, results.getCountInserted());
    }

    @Test
    public void testListOfGamesToDelete_2DeletionsToBeExpected() throws Exception {
        //BoardGameTable bgt = new BoardGameTable();
        //2,1,0 is generate 2 games return all of them
        ArrayList<BoardGame> apiGames = createXGamesList(2, 1, 0);
        //4,1,0 is generate 4 games return all of them
        ArrayList<BoardGame> dbGames = createXGamesList(4, 1, 0);
        ArrayList<BoardGame> toDelete = super.findArraylistValuesNotInSecondArraylist(dbGames, apiGames);
        assertEquals(2, toDelete.size());
    }

    @Test
    public void testListOfGamesToInsert_2InsertionsToBeExpected() throws Exception {
        ArrayList<BoardGame> apiGames = createXGamesList(4, 1, 0);
        ArrayList<BoardGame> dbGames = createXGamesList(2, 1, 0);
        ArrayList<BoardGame> toInsert = super.findArraylistValuesNotInSecondArraylist(apiGames, dbGames);
        assertEquals(2, toInsert.size());
    }

    //@Test
    insertResults syncBoardGameCollection() throws Exception {
        // Where passed in BoardGame Array is greater than the fetch boardgame array
        BoardGameTable bgt = new BoardGameTable();
        //3,1,0 is generate 3 games return all of them
        ArrayList<BoardGame> apiGames = createXGamesList(3, 1, 0);
        //2,1,0 is generate 2 games return all of them
        ArrayList<BoardGame> dbGames = createXGamesList(2, 1, 0);

        Map<String, BoardGame> bgMap = super.markAPIvsDB(apiGames, dbGames);

        int countDeleted = 0;
        int countInserted = 0;

        for (Map.Entry<String, BoardGame> game : bgMap.entrySet()) {
            BoardGame bg = game.getValue();
            String syncVal = bg.getSyncValue();
            if (syncVal.equals("DBOnly")) {
                countDeleted++;
            } else {
                countInserted++;
            }
        }

        insertResults results = new insertResults();
        results.setCountDeleted(countDeleted);
        results.setCountInserted(countInserted);
        return results;
    }


    /*
        Params:
            int gameCount = number of games you which to generate
            int mod = the modulus you which to check for
            int modResult = the result of the modulus we are looking for

            Examples:
                5, 1, 0 = 5 games, return them all
                5, 2, 1 = 5 games, return only the odd ones
                5, 2, 0 = 5 games, return only the even ones
                5, 3, 0 = 5 games, return every third game
     */
    private ArrayList<BoardGame> createXGamesList(int gameCount, int mod, int modResult) {
        ArrayList<BoardGame> games = new ArrayList<BoardGame>();
        for (int i = 1; i <= gameCount; i++) {
            if (i % mod == modResult) {
                BoardGame bg = new BoardGame();
                String id = String.valueOf(i);
                bg.setId(id);
                bg.setPrimaryName("Game" + id);
                games.add(bg);
            }
        }
        return games;
    }

}

class insertResults {
    private int countDeleted;
    private int countInserted;

    public int getCountInserted() {
        return countInserted;
    }

    public void setCountInserted(int countInserted) {
        this.countInserted = countInserted;
    }

    public int getCountDeleted() {
        return countDeleted;
    }

    public void setCountDeleted(int countDeleted) {
        this.countDeleted = countDeleted;
    }
}
