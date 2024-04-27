package com.example.gonuts;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents the fox character in the game. This class manages the fox's position, image, and ability to respawn
 * at different locations within the maze. The fox can appear at any wall position that is not on the outermost layer
 * of the maze.
 */
public class Fox {
    private Bitmap image; // The image representing the fox
    private int x, y; // Fox's position
    private List<Projectile> projectiles = new ArrayList<>(); // Add a list to manage projectiles
    private Random random = new Random(); // Random generator for selecting spawn locations

    /**
     * Constructs a Fox object, setting its initial position to a random wall location inside the maze and resizing its image.
     *
     * @param context The context used for accessing resources.
     * @param mazeBlockSize The size of each block in the maze, used to scale the fox's image.
     * @param maze The array representing the maze structure, where walls are marked with 1.
     * @param mazeOffsetX The horizontal offset to position the fox correctly within the maze.
     * @param mazeOffsetY The vertical offset to position the fox correctly within the maze.
     */
    public Fox(Context context, int mazeBlockSize, int[][] maze, int mazeOffsetX, int mazeOffsetY) {
        image = BitmapFactory.decodeResource(context.getResources(), R.drawable.fox_image);

        // Find all valid wall positions that are not on the outermost layer
        List<int[]> wallPositions = new ArrayList<>();
        for (int i = 1; i < maze.length - 1; i++) {
            for (int j = 1; j < maze[i].length - 1; j++) {
                if (maze[i][j] == 1) {
                    wallPositions.add(new int[]{i, j});
                }
            }
        }

        // Select a random wall position for the fox
        if (!wallPositions.isEmpty()) {
            int[] position = wallPositions.get(random.nextInt(wallPositions.size()));
            x = position[1] * mazeBlockSize + mazeOffsetX;
            y = position[0] * mazeBlockSize + mazeOffsetY;
        }

        // Resize the fox to fit a block in the maze
        image = Bitmap.createScaledBitmap(image, mazeBlockSize, mazeBlockSize, false);
    }

    /**
     * Respawns the fox at a new wall position within the maze, not on the outermost layer.
     * This method recalculates valid positions and selects one at random.
     *
     * @param mazeBlockSize Size of each maze block.
     * @param maze The maze grid.
     * @param mazeOffsetX Horizontal offset for correct positioning.
     * @param mazeOffsetY Vertical offset for correct positioning.
     */
    public void respawn(int mazeBlockSize, int[][] maze, int mazeOffsetX, int mazeOffsetY) {
        // Find all valid wall positions that are not on the outermost layer
        List<int[]> wallPositions = new ArrayList<>();
        for (int i = 1; i < maze.length - 1; i++) {
            for (int j = 1; j < maze[i].length - 1; j++) {
                if (maze[i][j] == 1) {
                    wallPositions.add(new int[]{i, j});
                }
            }
        }

        // Select a random wall position for the fox
        if (!wallPositions.isEmpty()) {
            int[] position = wallPositions.get(random.nextInt(wallPositions.size()));
            x = position[1] * mazeBlockSize + mazeOffsetX;
            y = position[0] * mazeBlockSize + mazeOffsetY;
        }
    }

    /**
     * Retrieves the x-coordinate of the fox's current position.
     *
     * @return The x-coordinate.
     */
    public float getX() {
        return x;
    }

    /**
     * Retrieves the y-coordinate of the fox's current position.
     *
     * @return The y-coordinate.
     */
    public float getY() {
        return y;
    }

    /**
     * Draws the fox on the provided canvas at its current position.
     *
     * @param canvas The canvas on which to draw the fox.
     */
    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);
    }

}
