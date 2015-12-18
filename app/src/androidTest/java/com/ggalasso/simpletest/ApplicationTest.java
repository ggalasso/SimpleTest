package com.ggalasso.simpletest;

import android.app.Application;
import android.test.ApplicationTestCase;

import com.ggalasso.simpletest.api.CollectionAPI;
import com.ggalasso.simpletest.controller.GameIdManager;
import com.ggalasso.simpletest.model.GameId;

import java.util.ArrayList;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void test() throws Exception {
        final int expected = 5;
        final int reality = 5;
        assertEquals(expected, reality);

    }

    public void testRetrieveTwentyGames() throws Exception {
        final int expected = 20;
        CollectionAPI capi = new CollectionAPI();
        GameIdManager gim = capi.getIDManager();
        ArrayList<GameId> gameIds = gim.getGameIds();
        final int reality = gameIds.size();
        assertEquals(expected, reality);
    }
}