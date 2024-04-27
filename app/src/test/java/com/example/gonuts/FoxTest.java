package com.example.mycopyofgonuts;

package com.example.mycopyofgonuts;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Random;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 * Tests the functionality of the Fox class, focusing particularly on the
 * ability of the fox to spawn at correct locations
 */
@RunWith(MockitoJUnitRunner.class)
public class FoxTest {

    @Mock
    private Context mockContext;
    @Mock
    private Resources mockResources;
    @Mock
    private Bitmap mockBitmap;
    @Mock
    private Random mockRandom;

    private Fox fox;
    private int[][] maze = new int[][]{
            {1, 1, 1, 1, 1},
            {1, 0, 1, 0, 1},
            {1, 0, 1, 0, 1},
            {1, 0, 0, 0, 1},
            {1, 1, 1, 1, 1}
    };

    /**
     * Sets up the test environment before each test method.
     * This method prepares the mocked dependencies required for creating a Fox instance,
     * including Context, Resources, and Bitmap. It also sets up a controlled Random instance
     * to ensure consistent and predictable behavior in tests.
     */
    @Before
    public void setUp() {
        when(mockContext.getResources()).thenReturn(mockResources);
        when(mockResources.getDrawable(anyInt())).thenReturn(null);
        when(BitmapFactory.decodeResource(mockResources, R.drawable.fox_image)).thenReturn(mockBitmap);
        when(mockBitmap.getWidth()).thenReturn(10);
        when(mockBitmap.getHeight()).thenReturn(10);
        when(mockBitmap.isRecycled()).thenReturn(false);
        doReturn(mockBitmap).when(mockBitmap).copy(any(Bitmap.Config.class), anyBoolean());

        // Set up controlled randomness
        when(mockRandom.nextInt(anyInt())).thenReturn(1); // Forces the random position to be predictable

        fox = new Fox(mockContext, 1, maze, 0, 0);
        fox.random = mockRandom; // Inject our mocked Random instance
    }

    /**
     * Tests the respawn functionality of the Fox class to ensure the fox respawns
     * at a valid random location within the maze. The test verifies that the fox
     * does not respawn on the outermost layer and that the position is recalculated
     * based on a mock random mechanism.
     */
    @Test
    public void testFoxRespawn() {
        // Assuming maze block size is 1 and offsets are 0 for simplicity
        fox.respawn(1, maze, 0, 0);
        // Check that fox respawns at the mocked random location
        assertEquals("Fox should respawn at the middle left cell", 1, fox.getX());
        assertEquals("Fox should respawn at the middle left cell", 3, fox.getY());
    }
}

