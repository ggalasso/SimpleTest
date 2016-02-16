package com.ggalasso.BggCollectionManagerUnitTests.controllerTests;

import com.ggalasso.BggCollectionManager.controller.GameIdManager;
import com.ggalasso.BggCollectionManager.model.GameId;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Edward on 2/15/2016.
 */
public class GameIdManagerTest {

    @Test
    public void testGetIdListString() throws Exception {
        // Arrange
        //GameIdManager gim = GameIdManager.getInstance();

        GameId game1 = new GameId();
        game1.setObjectid("1001");
        GameId game2 = new GameId();
        game2.setObjectid("1002");
        GameId game3 = new GameId();
        game3.setObjectid("1003");

        ArrayList<GameId> gameIds = new ArrayList<>();
        gameIds.add(game1);
        gameIds.add(game2);
        gameIds.add(game3);

        String expectedString = "1001,1002,1003";
        // Act
        String actualString = GameIdManager.getIdListString(gameIds);

        // Assert
        assertEquals(expectedString, actualString);
    }
}