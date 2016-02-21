package com.ggalasso.BggCollectionManagerUnitTests.dbTests;

import com.ggalasso.BggCollectionManager.db.CategoryTable;

import junit.framework.Assert;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by ggalasso on 2/21/16.
 */
public class CategoryTableTest extends CategoryTable {

    @Test
    public void testGetInsertSQL() throws Exception {
        // Arrange base final test for github contribution credit
        Map<String,String> bgCatMap = new HashMap<>();
        ArrayList<String> columns = new ArrayList<>();
        columns.add("ca_Id");
        columns.add("ca_Name");
        // Arrange Mock Values
        bgCatMap.put("1001", "Adventure");
        bgCatMap.put("1002", "Exploration");
        bgCatMap.put("1003", "Fantasy");
        // Arrange Expected String to compare
        String expected = "INSERT OR IGNORE INTO category_in_game (ca_Id, ca_Name) VALUES ('1001', 'Adventure'),('1002', 'Exploration'),('1003', 'Fantasy');";

        // Act
        String actual = getInsertSQL(bgCatMap, "category_in_game", columns);

        // Assert
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testFetchAllCategories() throws Exception {

    }

    @Test
    public void testFetchCategory() throws Exception {

    }
}