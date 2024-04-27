package com.example.gonuts;

import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.widget.Toast;

/**
 * GameThread manages the main game loop and rendering process. It controls the timing for updating game states
 * and drawing.
 */
public class GameThread extends Thread {
    private boolean running; // Flag to control the game loop
    private final SurfaceHolder surfaceHolder; // Holder providing access and control over the surface
    private final GameView gameView; // The game view to update and draw
    private static final int MAX_FPS = 60; // Maximum frames per second
    private long targetTime = 1000 / MAX_FPS; // target time in milliseconds to achieve desired FPS

    /**
     * Constructs a GameThread.
     *
     * @param surfaceHolder The surface holder on which the game is drawn, which manages the surface lifecycle.
     * @param gameView The game view that handles game logic and rendering.
     */
    public GameThread(SurfaceHolder surfaceHolder, GameView gameView) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gameView = gameView;
    }

    /**
     * This method sets the running state of the thread.
     * The game loop will stop running when it's set to false.
     *
     * @param running The state to set for the game loop.
     */
    public void setRunning(boolean running) {
        this.running = running;
    }

    /**
     * The main game loop that controls update and draw cycles. It ensures that the game updates and redraws
     * at a consistent rate, defined by MAX_FPS. It also handles locking and unlocking the canvas to ensure
     * that drawing operations do not conflict with surface updates.
     */
    @Override
    public void run() {
        long startTime;
        long timeMillis;
        long waitTime;
        Canvas canvas;

        while (running) {
            startTime = System.nanoTime();
            canvas = null;

            try {
                // Try locking the canvas for pixel editing
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    if (canvas != null) {
                        this.gameView.update(); // Update the game state before drawing
                        this.gameView.draw(canvas); // Draw the frame
                    }
                }
            } finally {
                if (canvas != null) {
                    // Make sure to unlock the canvas and post the changes
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }

            timeMillis = (System.nanoTime() - startTime) / 1000000;
            waitTime = targetTime - timeMillis;

            try {
                // Maintain the frame rate by waiting if necessary
                if (waitTime > 0) {
                    sleep(waitTime);
                }
            } catch (InterruptedException e) {
                // Properly handle interruption to stop the thread safely
                Thread.currentThread().interrupt();
            }
        }
    }

}
