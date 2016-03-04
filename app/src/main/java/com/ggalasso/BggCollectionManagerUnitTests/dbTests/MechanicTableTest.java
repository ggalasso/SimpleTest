package com.ggalasso.BggCollectionManagerUnitTests.dbTests;

import com.ggalasso.BggCollectionManager.db.MechanicTable;

import junit.framework.Assert;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by Edward on 3/3/2016.
 */
public class MechanicTableTest extends MechanicTable {

    @Test
    public void testGetInsertSQL() throws Exception {
        // Arrange base final test for github contribution credit
        Map<String,String> bgCatMap = new HashMap<>();
        ArrayList<String> columns = new ArrayList<>();
        columns.add("me_Id");
        columns.add("me_Name");
        // Arrange Mock Values
        bgCatMap.put("1001", "Hand Management");
        bgCatMap.put("1002", "Area Enclosure");
        bgCatMap.put("1003", "Worker Placement");
        // Arrange Expected String to compare
        String expected = "INSERT OR IGNORE INTO mechanic (me_Id, me_Name) VALUES ('1001', 'Hand Management'),('1002', 'Area Enclosure'),('1003', 'Worker Placement');";

        // Act
        String actual = getInsertSQL(bgCatMap, "mechanic", columns);

        // Assert
        Assert.assertEquals(expected, actual);
    }
}