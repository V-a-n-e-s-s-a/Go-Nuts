package com.example.gonuts;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Represents the game view, handling all drawing and game logic.
 * This class is responsible for setting up the game environment, managing game objects like the Squirrel, Fox,
 * and projectiles, and handling game state changes and drawing on the screen.
 */
public class GameView extends SurfaceView implements SurfaceHolder.Callback {
    private GameThread thread; // Main thread for the game loop
    private int[][] maze = new int[19][19]; // Representation of the game maze
    private Squirrel squirrel; // Player character
    private Fox fox; // Enemy character
    private Paint bushPaint; // Paint for drawing bushes (walls of the maze)
    private Paint emptySpacePaint; // Paint for empty spaces in the maze
    private Bitmap acornImage; // Bitmap for drawing acorns
    private Bitmap projectileImage; // Bitmap for drawing projectiles
    private Bitmap backgroundImage; // Background image for the game
    private Bitmap frameImage;  // Frame image around the game area
    private int blockSize;  // Size of each block in the grid
    private int offsetX;    // Horizontal offset to center the maze
    private int offsetY;    // Vertical offset to center the maze
    private Random random = new Random(); // Random number generator
    private List<Projectile> projectiles = new ArrayList<>(); // Add a list to manage projectiles

    /**
     * Handler for managing the respawn of the Fox. This makes it so that the fox is respawned periodically.
     */
    private Handler respawnHandler = new Handler();
    private Runnable respawnRunnable = new Runnable() {
        @Override
        public void run() {
            // Check if the fox exists and respawn it at a new location if it does.
            if (fox != null) {
                fox.respawn(blockSize, maze, offsetX, offsetY);
                postInvalidate();  // Request to redraw the view (calls the draw method)
            }
            // Schedule the next respawn
            respawnHandler.postDelayed(this, 10000);  // 10000ms = 10 seconds
        }
    };

    /**
     * Handler for managing the shooting of projectiles by the Fox. This handles the creation and scheduling
     * of projectiles aimed at the Squirrel.
     */
    private Handler shootHandler = new Handler();
    private Runnable shootRunnable = new Runnable() {
        @Override
        public void run() {
            // Create a projectile if the fox and squirrel are present
            if (fox != null && squirrel != null) {
                projectiles.add(new Projectile(
                        projectileImage,
                        fox.getX() + blockSize / 2,  // Assuming the fox image is centered in its block
                        fox.getY() + blockSize / 2,
                        squirrel.getX() + blockSize / 2,
                        squirrel.getY() + blockSize / 2
                ));
                postInvalidate(); // Request to redraw the view (calls the draw method)
            }
            // Schedule the next shot
            shootHandler.postDelayed(this, 2000); // 2000ms = 2 seconds
        }
    };

    /**
     * Constructs a GameView which sets up the game environment, initializes rendering components, and starts the game logic.
     * It sets the view to be focusable to handle interactions and starts timers for game events like fox respawning and projectile shooting.
     *
     * @param context The current context, used to create new views and access resources.
     */
    public GameView(Context context) {
        super(context);
        getHolder().addCallback(this);
        thread = new GameThread(getHolder(), this); // Initialize the main game thread
        //setFocusable(true); // Make the GameView focusable so it can handle events

        // Initialize paints for drawing the maze
        bushPaint = new Paint();
        bushPaint.setColor(Color.rgb(21,66,57));  // Dark green color for bushes
        bushPaint.setStyle(Paint.Style.FILL);

        emptySpacePaint = new Paint();
        emptySpacePaint.setColor(Color.rgb(151, 192, 133)); // Light green for empty spaces

        // Load the background and frame image
        backgroundImage = BitmapFactory.decodeResource(getResources(), R.drawable.maze_background);
        frameImage = BitmapFactory.decodeResource(getResources(), R.drawable.maze_frame);

        // Initialize the maze structure and randomize acorn placements
        initializeMaze();
        randomizeAcorns();

        // Start the respawn timer for the fox
        respawnHandler.postDelayed(respawnRunnable, 10000);  // 10000ms = 10 seconds

        // Start the timer for the projectile
        shootHandler.postDelayed(shootRunnable, 2000); // 2000ms = 2 seconds

    }

    /**
     * Initializes the maze structure with a 19x19 grid where walls are represented by 1s and
     * open paths by 0s.
     */
    private void initializeMaze() {
        // Hardcoded grid representing the maze layout
        maze = new int[][] {
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1},
                {1,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,1},
                {1,0,1,1,0,1,1,1,0,1,0,1,1,1,0,1,1,0,1},
                {1,0,1,1,0,1,1,1,0,1,0,1,1,1,0,1,1,0,1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {1,0,1,1,0,1,0,1,1,1,1,1,0,1,0,1,1,0,1},
                {1,0,0,0,0,1,0,0,0,1,0,0,0,1,0,0,0,0,1},
                {1,0,0,1,0,1,1,1,0,1,0,1,1,1,0,1,0,0,1},
                {1,0,1,1,0,1,0,0,0,0,0,0,0,1,0,1,1,0,1},
                {1,0,0,0,0,1,0,0,0,0,0,0,0,1,0,0,0,0,1},
                {1,0,1,1,0,0,0,0,0,0,0,0,0,0,0,1,1,0,1},
                {1,0,0,1,0,1,0,1,1,1,1,1,0,1,0,1,0,0,1},
                {1,0,0,0,0,0,0,0,0,1,0,0,0,0,0,0,0,0,1},
                {1,0,1,1,0,1,1,1,0,1,0,1,1,1,0,1,1,0,1},
                {1,0,0,1,0,0,0,0,0,0,0,0,0,0,0,1,0,0,1},
                {1,1,0,0,0,1,0,1,1,1,1,1,0,1,0,0,0,1,1},
                {1,0,0,1,1,1,0,0,0,1,0,0,0,1,1,1,0,0,1},
                {1,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,1},
                {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1}
        };
    }

    /**
     * Randomizes the placement of acorns within the open paths of the maze. Each open cell has a 10% chance
     * of being assigned an acorn.
     */
    private void randomizeAcorns() {
        // Randomize acorns in open paths
        for (int i = 1; i < 18; i++) {
            for (int j = 1; j < 18; j++) {
                if (maze[i][j] == 0 && random.nextDouble() < 0.1) { // 10% chance to place an acorn
                    maze[i][j] = 2; // 2 represent an acorn
                }
            }
        }
    }

    /**
     * Handles resizing of the game view based on the new dimensions.
     * It also resizes images to fit the new block size and reinitializes game entities like the Squirrel and Fox.
     *
     * @param w The new width of this view.
     * @param h The new height of this view.
     * @param oldw The old width of this view.
     * @param oldh The old height of this view.
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        blockSize = Math.min(w, h) / 19;  // Determine the block size based on the smaller dimension
        offsetX = (w - blockSize * 19) / 2;  // Calculate offsets to center the maze
        offsetY = (h - blockSize * 19) / 2;

        // Resize the frame image to fit the maze size plus the desired frame thickness
        int frameWidth = blockSize * 19 + 2 * 30; // desiredFrameThickness is how thick you want the frame to be
        int frameHeight = frameWidth;  // Assuming a square frame for simplicity
        frameImage = Bitmap.createScaledBitmap(frameImage, frameWidth, frameHeight, false);

        // Instantiate the Squirrel and the Fox with the maze size
        squirrel = new Squirrel(getContext(), blockSize, offsetX, offsetY);
        fox = new Fox(getContext(), blockSize, maze, offsetX, offsetY);

        /**
         * Loads the projectile image from resources and scales it to match the block size of the maze.
         * This ensures that the projectile image is appropriately sized to fit within the game's grid system.
         */
        Bitmap originalProjectile = BitmapFactory.decodeResource(getResources(), R.drawable.slime_image);
        projectileImage = Bitmap.createScaledBitmap(originalProjectile, blockSize, blockSize, false);

        // Resize the acorn image to fit the new block size
        loadAndResizeBitmaps();
    }

    /**
     * Loads and resizes bitmap images used within the game. Specifically, it resizes the acorn image
     * to fit the size of the blocks in the maze.
     */
    private void loadAndResizeBitmaps() {
        Bitmap originalAcorn = BitmapFactory.decodeResource(getResources(), R.drawable.acorn_image);
        acornImage = Bitmap.createScaledBitmap(originalAcorn, blockSize, blockSize, false);
    }

    /**
     * Called when the surface is created. This method starts the game thread, setting the game in motion.
     *
     * @param holder The surface holder provided by the system.
     */
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
    }

    /**
     * Called when the surface changes, for example during orientation changes. This method is currently
     * implemented as empty, used only if needed for responding to size changes.
     *
     * @param holder The surface holder whose surface has changed.
     * @param format The new PixelFormat of the surface.
     * @param width The new width of the surface.
     * @param height The new height of the surface.
     */
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}

    /**
     * Called when the surface is destroyed. This method stops the game thread and removes callbacks
     * to prevent memory leaks.
     *
     * @param holder The surface holder whose surface is being destroyed.
     */
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Remove callbacks when the view is destroyed to avoid leaks
        respawnHandler.removeCallbacks(respawnRunnable);

        // Remove callbacks to avoid leaks
        shootHandler.removeCallbacks(shootRunnable);
    }

    /**
     * Updates the state of the game, including checking for collisions between projectiles and the squirrel,
     * as well as handling projectile movement and interaction with the game environment.
     */
    public void update() {
        // Update and check for collisions for each projectile
        Iterator<Projectile> iterator = projectiles.iterator();
        while (iterator.hasNext()) {
            Projectile projectile = iterator.next();
            projectile.update();

            // Collision with squirrel: check if a projectile is close enough to consider it a collision
            if (Math.hypot(projectile.getX() - squirrel.getX(), projectile.getY() - squirrel.getY()) < blockSize) {
                iterator.remove(); // Remove the projectile on collision
                endGame();// game ends when projectile hits squirrel
            }

            // Collision with maze frame
            // Check if the projectile has collided with the frame; if so, remove it
        }
    }

    /**
     * Ends the game once the game loop stops. Stops when squirrel gets hit by slime
     */
    public void endGame() {
        thread.setRunning(false);  // Stop the game loop, ending the game
    }

    /**
     * Pauses the game by safely stopping the game thread. Ensures that the thread stops completely before proceeding.
     */
    public void pause() {
        // Safely pause the thread
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join(); // Wait for the thread to finish
                retry = false;
            } catch (InterruptedException e) {
                // Retry stopping the thread if interrupted
            }
        }
    }

    /**
     * Resumes the game by recreating and starting the game thread if it was previously terminated.
     */
    public void resume() {
        if (thread.getState() == Thread.State.TERMINATED) {
            thread = new GameThread(getHolder(), this); // Recreate the thread
            thread.setRunning(true);
            thread.start(); // Start the new thread
        }
    }

    /**
     * Draws the game elements on the canvas, including the background, maze, frame, and characters.
     * This is the main drawing method called every frame.
     *
     * @param canvas The canvas on which to draw the game elements.
     */
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            //Draw the background image on the screen
            canvas.drawBitmap(backgroundImage, 0, 0, null);

            //Draw the maze over the background image
            drawMaze(canvas);

            // Draw the frame centered on the screen over the maze
            int frameX = (getWidth() - frameImage.getWidth()) / 2;
            int frameY = (getHeight() - frameImage.getHeight()) / 2;
            canvas.drawBitmap(frameImage, frameX, frameY, null);

            // Draw the Fox
            if (fox != null) {
                fox.draw(canvas);
            }

            //Draw the squirrel
            if (squirrel != null) {
                squirrel.draw(canvas);
            }

            // Draw all active projectiles
            for (Projectile projectile : projectiles) {
                projectile.draw(canvas);
            }
        }
    }

    /**
     * Draws the maze based on the predetermined grid and iterates through each cell of the maze array
     * to determine what to draw based on the cell's value: 1 represents a wall (bush), 2 represents a
     * space with an acorn, and 0 represents an empty path and are colored in with their respective
     * shades so players can easily recognize paths, obstacles, and collectibles.
     *
     * @param canvas The canvas on which the maze is drawn.
     */
    private void drawMaze(Canvas canvas) {
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                int x = j * blockSize + offsetX;
                int y = i * blockSize + offsetY;
                // Draw bushes/wall
                if (maze[i][j] == 1) {
                    canvas.drawRect(x, y, x + blockSize, y + blockSize, bushPaint);
                }
                // Draw an empty space and acorn
                else if (maze[i][j] == 2 && acornImage != null) {
                    canvas.drawRect(x, y, x + blockSize, y + blockSize, emptySpacePaint);
                    canvas.drawBitmap(acornImage, x, y, null);
                } else if (maze[i][j] == 0) {
                    // Draw an empty space as the path
                    canvas.drawRect(x, y, x + blockSize, y + blockSize, emptySpacePaint);
                }
            }
        }
    }
}
