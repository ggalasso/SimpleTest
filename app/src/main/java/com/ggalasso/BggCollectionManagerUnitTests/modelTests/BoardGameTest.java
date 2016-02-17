package com.ggalasso.BggCollectionManagerUnitTests.modelTests;

import com.ggalasso.BggCollectionManager.model.BoardGame;
import com.ggalasso.BggCollectionManager.model.Link;
import com.ggalasso.BggCollectionManager.model.Name;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
}