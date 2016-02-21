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
 * Created by ggalasso on 2/21/16.
 */
public class CategoryInGameTableTest extends CategoryInGameTable {

    @Test
    public void testGetInsertSQL() throws Exception {
        // Arrange base final test for github contribution credit
        Map<String,ArrayList<String>> bgCatMap = new HashMap<>();
        ArrayList<String> columns = new ArrayList<>();
        columns.add("cg_bg_Id");
        columns.add("cg_ca_Id");
        // Arrange Mock Values
        ArrayList<String> firstitemIds = new ArrayList<>();
        firstitemIds.add("9009");
        firstitemIds.add("9010");
        ArrayList<String> secondItemIds = new ArrayList<>();
        secondItemIds.add("9008");
        bgCatMap.put("1001", firstitemIds);
        bgCatMap.put("1002",secondItemIds);

        // Arrange Expected String to compare
        String expected = "INSERT OR IGNORE INTO category_in_game (cg_bg_Id, cg_ca_Id) VALUES ('1001', '9009'),('1001', '9010'),('1002', '9008');";

        // Act
        String actual = getInsertSQL(bgCatMap, "category_in_game", columns);

        // Assert
        Assert.assertEquals(expected, actual);

    }
}