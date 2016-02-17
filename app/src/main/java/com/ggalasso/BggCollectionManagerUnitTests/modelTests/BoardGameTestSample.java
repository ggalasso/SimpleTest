package com.ggalasso.BggCollectionManagerUnitTests.modelTests;

import com.ggalasso.BggCollectionManager.model.BoardGame;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.*;

/**
 * Created by truthd on 1/21/2016.
 */
public class BoardGameTestSample extends TestCase {

    @Mock
    private BoardGame bg;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        Mockito.when(bg.getId()).thenReturn("171");
        Mockito.when(bg.getPrimaryName()).thenReturn("Chess");
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetPrimaryName() throws Exception {
        assertEquals(bg.getPrimaryName(), "Chess");
    }

    @Test
    public void testGetIdOption1() throws Exception {
        BoardGame test = mock(BoardGame.class);
        when(test.getId()).thenReturn("172");
        assertEquals(test.getId(), "172");
    }

    @Test
    public void testGetIdOption2() throws Exception {
        assertEquals(bg.getId(), "171");
    }


}