package com.example.gonuts;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import android.view.SurfaceHolder;

/**
 * Tests the GameThread class responsible for managing the game loop.
 * This class ensures that the GameThread correctly handles its running state and interacts
 * properly with the SurfaceHolder to perform drawing operations via the GameView.
 */
@RunWith(MockitoJUnitRunner.class)
public class GameThreadTest {
    @Mock
    private SurfaceHolder mockSurfaceHolder;
    @Mock
    private GameView mockGameView;
    private GameThread gameThread;

    /**
     * Sets up the test environment before each test. This method initializes the mocked
     * dependencies and the GameThread instance with these mocks to facilitate the testing
     * of thread behavior independently of the actual Android framework.
     */
    @Before
    public void setUp() {
        gameThread = new GameThread(mockSurfaceHolder, mockGameView);
    }

    /**
     * Tests the running state management of the GameThread. Verifies that the running
     * state can be correctly set and retrieved, reflecting whether the thread should
     * be actively running or not.
     */
    @Test
    public void testGameThreadRunning() {
        assertFalse("Initially, thread should not be running", gameThread.isRunning());
        gameThread.setRunning(true);
        assertTrue("Game thread should be set to run", gameThread.isRunning());
        gameThread.setRunning(false);
        assertFalse("Game thread should be set to stop", gameThread.isRunning());
    }
}
