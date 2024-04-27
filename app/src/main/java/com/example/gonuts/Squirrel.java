package com.example.gonuts;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/**
 * Represents the squirrel character in the game. This class manages the squirrel's position, image, and drawing.
 * The squirrel is initialized in the middle of the maze and resized to fit within a maze block.
 */
public class Squirrel {
    private Bitmap image; // The image representing the squirrel
    private int x, y; // Squirrel's position

    /**
     * Constructs a Squirrel object, setting its initial position and resizing its image.
     *
     * @param context The context used for accessing resources.
     * @param mazeBlockSize The size of each block in the maze, used to scale the squirrel's image.
     * @param mazeOffsetX The horizontal offset to position the squirrel correctly within the maze.
     * @param mazeOffsetY The vertical offset to position the squirrel correctly within the maze.
     */
    public Squirrel(Context context, int mazeBlockSize, int mazeOffsetX, int mazeOffsetY) {
        image = BitmapFactory.decodeResource(context.getResources(), R.drawable.squirrel_image);

        // Assuming the squirrel starts in the middle of the maze
        x = mazeBlockSize * 9 + mazeOffsetX; // 9 to position in the middle cell of the 19x19 grid
        y = mazeBlockSize * 9 + mazeOffsetY;

        // Resize the squirrel to fit a block in the maze
        image = Bitmap.createScaledBitmap(image, mazeBlockSize, mazeBlockSize, false);
    }

    /**
     * Gets the x-coordinate of the squirrel's position.
     *
     * @return The x-coordinate of the squirrel.
     */
    public float getX() {
        return x;
    }

    /**
     * Gets the y-coordinate of the squirrel's position.
     *
     * @return The y-coordinate of the squirrel.
     */
    public float getY() {
        return y;
    }

    /**
     * Moves the squirrel position up.
     *
     */
    public void moveUp() {
        y -= speed;
    }

    /**
     * Moves the squirrel position down.
     *
     */
    public void moveDown() {
        y += speed;
    }

    /**
     * Moves the squirrel position left.
     *
     */
    public void moveLeft() {
        x -= speed;
    }

    /**
     * Moves the squirrel position right.
     *
     */
    public void moveRight() {
        x += speed;
    }

    /**
     * Draws the squirrel on the provided canvas.
     *
     * @param canvas The canvas on which to draw the squirrel.
     */
    public void draw(Canvas canvas) {
        canvas.drawBitmap(image, x, y, null);
    }



    // future methods to be added
