package com.ggalasso.BggCollectionManagerUnitTests.dbTests;

import com.ggalasso.BggCollectionManager.db.CategoryInGameTable;

import junit.framework.Assert;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by Edward on 2/8/2016.
 */
public class CategoryInGameTableTest extends CategoryInGameTable {

    @Test
    public void testGetSQLInsertString() throws Exception {
        // Arrange
        Map<String,ArrayList<String>> bgCatMap = new HashMap<>();
        ArrayList<String> firstitemIds = new ArrayList<>();
        firstitemIds.add("9009");
        ArrayList<String> secondItemIds = new ArrayList<>();
        secondItemIds.add("9008");
        bgCatMap.put("1001", firstitemIds);
        bgCatMap.put("1002",secondItemIds);
        String expected = "INSERT OR IGNORE INTO category_in_game (cg_bg_Id, cg_ca_Id) VALUES ('1002', '9008'),('1001', '9009');";

        // Act
        String actual = super.getSQLInsertString(bgCatMap,"category_in_game","cg_bg_Id","cg_ca_Id");

        // Assert
        Assert.assertEquals(expected, actual);

    }
}