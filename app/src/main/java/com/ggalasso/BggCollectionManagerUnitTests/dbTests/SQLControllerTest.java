package com.ggalasso.BggCollectionManagerUnitTests.dbTests;

import com.ggalasso.BggCollectionManager.db.CategoryInGameTable;
import com.ggalasso.BggCollectionManager.db.SQLController;

import junit.framework.Assert;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by Edward on 2/8/2016.
 */
public class SQLControllerTest extends SQLController {

    @Test
    public void testGetSQLInsertString() throws Exception {
        // Arrange base final test for github contribution credit
        Map<String,ArrayList<String>> bgCatMap = new HashMap<>();
        ArrayList<String> columns = new ArrayList<>();
        columns.add("cg_bg_Id");
        columns.add("cg_ca_Id");
        // Arrange Mock Values
        ArrayList<String> firstitemIds = new ArrayList<>();
        firstitemIds.add("9009");
        ArrayList<String> secondItemIds = new ArrayList<>();
        secondItemIds.add("9008");
        bgCatMap.put("1001", firstitemIds);
        bgCatMap.put("1002",secondItemIds);

        // Arrange Expected String to compare
        String expected = "INSERT OR IGNORE INTO category_in_game (cg_bg_Id, cg_ca_Id) VALUES ('1001', '9009'),('1002', '9008');";

        // Act
        String actual = super.getSQLInsertString(bgCatMap,"category_in_game",columns);

        // Assert
        Assert.assertEquals(expected, actual);

    }
}