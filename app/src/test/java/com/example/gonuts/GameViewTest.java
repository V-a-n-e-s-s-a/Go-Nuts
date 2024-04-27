package com.example.gonuts;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import android.content.Context;
/**
 * Test class for {@link GameView} focusing on its game state management and scoring system.
 * This class utilizes Mockito to handle dependencies and JUnit for assertions to ensure that
 * game state transitions and scoring behave as expected under various conditions.
 */
@RunWith(MockitoJUnitRunner.class)
public class GameViewTest {

    @Mock
    Context mockContext;

    private GameView gameView;

    /**
     * Sets up the environment for each test, creating a new instance of GameView
     * with a mocked Context to ensure that Android-specific functionalities do not
     * interfere with the ability to perform tests.
     */
    @Before
    public void setUp() {
        gameView = new GameView(mockContext);
    }

    /**
     * Tests the startGame method to ensure it properly initializes the game state,
     * setting the game as running and resetting the score to zero.
     */
    @Test
    public void testStartGame() {
        gameView.startGame();
        assertTrue("Game should be running after start", gameView.isGameRunning());
        assertEquals("Score should be reset to 0 on start", 0, gameView.getScore());
    }

    /**
     * Tests the stopGame method to confirm that it correctly stops the game,
     * preventing any further changes to the game state.
     */
    @Test
    public void testStopGame() {
        gameView.startGame();
        gameView.stopGame();
        assertFalse("Game should not be running after stop", gameView.isGameRunning());
    }
