package com.ggalasso.BggCollectionManagerUnitTests.dbTests;

import com.ggalasso.BggCollectionManager.model.BoardGame;
import com.ggalasso.BggCollectionManager.model.Foo;
import com.ggalasso.BggCollectionManager.model.Link;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by Edward on 4/19/2016.
 */
public class SQLControllerTest {
    @Test
    public void testSQLControllergenericPrimitiveListCounter_GenericsWithInteger() throws Exception {

        Integer[] testsubject = {1,2,2,2,3,4};

        int result = com.ggalasso.BggCollectionManager.db.SQLController.genericPrimitiveListCounter(testsubject, 2);

        assertEquals(3, result);

    }
    @Test
    public void testGenericsWithString() throws Exception {

        String[] testsubject = {"1","2","2","2","3","4"};

        int result = com.ggalasso.BggCollectionManager.db.SQLController.genericPrimitiveListCounter(testsubject, "2");

        assertEquals(3, result);

    }

    @Test
    public void testFoo() throws Exception {
        Foo<Integer> sult = new Foo<>();
        sult.setT(5);

        int result = sult.getT();
        assertEquals(5, result);
    }

    @Test
    public void testFooWObject() throws Exception {
        Foo<BoardGame> result = new Foo<>();
        result.setT(new BoardGame());

        result.getT().setPrimaryName("Sam");
        assertEquals("Sam", result.getT().getPrimaryName());
    }

    @Test
    public void testSelectAllWhichActuallyConstructsNewObject() throws Exception {
        ArrayList<Link> b = new com.ggalasso.BggCollectionManager.db.SQLController().SelectAll(Link.class);

        assertEquals(2, b.size());
    }

    @Test
    public void testGetFieldsForObject_BoardGame_Expect_18(){
        // Arrange
        Integer expected = 18;
        Integer actual = 0;
        ArrayList<String> resultSet = new ArrayList<>();

        // Act
        resultSet = new com.ggalasso.BggCollectionManager.db.SQLController().getFieldsForObject(BoardGame.class);
        actual = resultSet.size();

        // Assert
        assertEquals(expected, actual);
    }

//    public <T> ArrayList<T> SelectAll(Class<T> foo){
//        ArrayList<T> list = new ArrayList<>();
//        Field[] fields = foo.getFields();
//        try {
//            Constructor<T> constructor = foo.getConstructor(foo);
//            list.add(constructor.newInstance(  ));
//        }catch(Exception ex){
//            return list;
//        }
//        return null;
//    }


}
