package com.ggalasso.BggCollectionManagerUnitTests.controllerTests;

import com.ggalasso.BggCollectionManager.controller.GameIdManager;
import com.ggalasso.BggCollectionManager.model.GameId;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

/**
 * Created by Edward on 2/15/2016.
 */
public class GameIdManagerTest {

    @Test
    public void testGetIdListString() throws Exception {
        // Arrange
        ArrayList<GameId> gameIds = new ArrayList<>();
        gameIds.add(new GameId("1001"));
        gameIds.add(new GameId("1002"));
        gameIds.add(new GameId("1003"));

        String expectedString = "1001,1002,1003";

        // Act
        String actualString = GameIdManager.getIdListString(gameIds);

        // Assert
        assertEquals(expectedString, actualString);
    }
}