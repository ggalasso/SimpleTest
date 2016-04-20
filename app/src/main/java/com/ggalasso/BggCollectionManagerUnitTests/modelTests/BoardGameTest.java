package com.ggalasso.BggCollectionManagerUnitTests.modelTests;

import com.ggalasso.BggCollectionManager.db.SQLController;
import com.ggalasso.BggCollectionManager.model.BoardGame;
import com.ggalasso.BggCollectionManager.model.Foo;
import com.ggalasso.BggCollectionManager.model.Link;
import com.ggalasso.BggCollectionManager.model.Name;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by truthd on 2/16/2016.
 */
public class BoardGameTest {

    private BoardGame bg;

    @Before
    public void setUp() throws Exception {
        bg = new BoardGame();
    }

    @Test
    public void testGetPrimaryName() throws Exception {
        // arrange
        //BoardGame bg = new BoardGame();
        ArrayList<Name> names = new ArrayList<>();
        names.add(new Name("ChessSquared", "1", "alternate"));
        names.add(new Name("Chess 3d", "2", "alternate"));
        names.add(new Name("Chess", "3", "primary"));
        bg.setNames(names);
        // act
        String actualName = bg.getPrimaryName();
        // assert
        assertEquals("Chess", actualName);

    }

    @Test
    public void testGetCategoryLinks() throws Exception {
        //arrange
        ArrayList<Link> links = new ArrayList<>();
        links.add(new Link("Family", "1", "boardgamecategory"));
        links.add(new Link("Role Playing", "2", "boardgamecategory"));
        links.add(new Link("Card Game", "3", "boardgamecategory"));
        links.add(new Link("Bluffing", "4", "boardgamemechanic"));
        bg.setLinks(links);
        //act
        ArrayList<Link> catLinks = bg.getCategoryLinks();
        Integer catSize = catLinks.size();
        //assert
        assertEquals(3, catSize.intValue());
    }

    @Test
    public void testGetMechanicLinks() throws Exception {
        //arrange
        ArrayList<Link> links = new ArrayList<>();
        links.add(new Link("Family", "1", "boardgamecategory"));
        links.add(new Link("Role Playing", "2", "boardgamecategory"));
        links.add(new Link("Card Game", "3", "boardgamecategory"));
        links.add(new Link("Bluffing", "4", "boardgamemechanic"));
        links.add(new Link("Drafting", "5", "boardgamemechanic"));
        links.add(new Link("Engine Building", "6", "boardgamemechanic"));
        bg.setLinks(links);
        //act
        ArrayList<Link> mechanicLinks = bg.getMechanicLinks();
        Integer mechanicSize = mechanicLinks.size();
        //assert
        assertEquals(3, mechanicSize.intValue());
    }

    @Test
         public void testGenerics() throws Exception {

        Integer[] testsubject = {1,2,2,2,3,4};

        int result = SQLController.testingGeneric(testsubject, 2);

        assertEquals(3, result);

    }
    @Test
    public void testGenerics2() throws Exception {

        String[] testsubject = {"1","2","2","2","3","4"};

        int result = SQLController.testingGeneric(testsubject, "2");

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
    public void TESTINGTEST() throws Exception {
        ArrayList<Link> b = new SQLController().SelectAll(Link.class);

        assertEquals(2, b.size());
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