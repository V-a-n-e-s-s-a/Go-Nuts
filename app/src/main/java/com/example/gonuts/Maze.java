package com.example.gonuts;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


public class Maze extends View {

    private static final int COLS = 19;
    private static final int ROWS = 19;
    private static final float WALL_THICKNESS = 4;

    private float cellSize, hMargin, vMargin;
    private Paint wallPaint;

    private Bitmap squirrelBitmap;
    private float squirrelX, squirrelY;

    private float joystickCenterX, joystickCenterY;
    private float joystickRadius;
    private Paint joystickPaint;
    private float joystickDirectionX, joystickDirectionY;


    private int[][] mazeLayout = {
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


    public Maze(Context context, AttributeSet attrs) {
        super(context, attrs);

        wallPaint = new Paint();
        wallPaint.setColor(Color.rgb(21,66,57));
        wallPaint.setStrokeWidth(WALL_THICKNESS);



        // Set initial squirrel position
        squirrelBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.squirrel_image);
        squirrelX = 0;
        squirrelY = 0;

        // Initialize joystick properties
        joystickCenterX = 100;
        joystickCenterY = 100;
        joystickRadius = 100; // Adjust the size of the joystick
        joystickPaint = new Paint();
        joystickPaint.setColor(Color.RED);
        joystickPaint.setStyle(Paint.Style.FILL);
        joystickDirectionX = 0;
        joystickDirectionY = 0;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        //canvas.drawColor(Color.rgb(151, 192, 133));

        int width = getWidth();
        int height = getHeight();

        // Draw the background picture
        Bitmap backgroundBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.maze_background);
        Bitmap scaledBackgroundBitmap = Bitmap.createScaledBitmap(backgroundBitmap, width, height, false);
        canvas.drawBitmap(scaledBackgroundBitmap, 0, 0, null);

        if (width / height < COLS / ROWS) {
            cellSize = width / (COLS +5);
        } else {
            cellSize = height / (ROWS +5);
        }

        hMargin = (width - COLS * cellSize) / 2;
        vMargin = (height - ROWS * cellSize) / 2;

        canvas.translate(hMargin, vMargin);


        for (int x = 0; x < COLS; x++) {
            for (int y = 0; y < ROWS; y++) {
                if (mazeLayout[y][x] == 1) {
                    float left = x * cellSize;
                    float top = y * cellSize;
                    float right = (x + 1) * cellSize;
                    float bottom = (y + 1) * cellSize;

                    canvas.drawRect(left, top, right, bottom, wallPaint);
                }
                else {
                    // Draw paths
                    float left = x * cellSize;
                    float top = y * cellSize;
                    float right = (x + 1) * cellSize;
                    float bottom = (y + 1) * cellSize;

                    Paint pathPaint = new Paint();
                    pathPaint.setColor(Color.rgb(151, 192, 133));
                    canvas.drawRect(left, top, right, bottom, pathPaint);
                }
            }
        }

        // Load picture frame image
        Bitmap frameBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.maze_frame);

        // Scale picture frame to fit the maze
        float frameWidth = COLS * cellSize+50;
        float frameHeight = ROWS * cellSize+50;
        Bitmap scaledFrameBitmap = Bitmap.createScaledBitmap(frameBitmap, (int) frameWidth, (int) frameHeight, false);

        // Draw picture frame
        canvas.drawBitmap(scaledFrameBitmap, -25, -25, null);


        //Draw squirrel
        float squirrelWidth = cellSize;
        float squirrelHeight = cellSize;
        Bitmap scaledSquirrelBitmap = Bitmap.createScaledBitmap(squirrelBitmap, (int) squirrelWidth, (int) squirrelHeight, false);

        // location of squirrel
        float squirrelX = 9 * cellSize;
        float squirrelY = 17 * cellSize;

        // Draw the scaled squirrel bitmap at the calculated position
        canvas.drawBitmap(scaledSquirrelBitmap, squirrelX, squirrelY, null);

        // Draw joystick center
        canvas.drawCircle(joystickCenterX, joystickCenterY, 20, joystickPaint);

        // Draw joystick direction line
        float endX = joystickCenterX + joystickDirectionX * joystickRadius*5;
        float endY = joystickCenterY + joystickDirectionY * joystickRadius*5;
        canvas.drawLine(joystickCenterX, joystickCenterY, endX, endY, joystickPaint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                // Calculate the distance from the joystick center
                float dx = x - joystickCenterX;
                float dy = y - joystickCenterY;
                float distance = (float) Math.sqrt(dx * dx + dy * dy);

                // If the touch is within the joystick radius, update the joystick position
                if (distance <= joystickRadius) {
                    joystickDirectionX = dx / joystickRadius;
                    joystickDirectionY = dy / joystickRadius;
                    moveSquirrel(joystickDirectionX, joystickDirectionY); // Move the squirrel
                    invalidate(); // Redraw the view
                }
                break;
            case MotionEvent.ACTION_UP:
                // Reset the joystick direction when the touch is released
                joystickDirectionX = 0;
                joystickDirectionY = 0;
                invalidate(); // Redraw the view
                break;
        }

        return true;
    }

    public void moveSquirrel(float joystickDirectionX, float joystickDirectionY) {


        // Calculate the new position based on the joystick direction
        float newSquirrelX = squirrelX + joystickDirectionX * cellSize;
        float newSquirrelY = squirrelY + joystickDirectionY * cellSize;

        // Check if the new position is within the maze boundaries and not a wall
        if (isValidPosition(newSquirrelX, newSquirrelY)) {
            int mazeX = (int) ((newSquirrelX - hMargin) / cellSize);
            int mazeY = (int) ((newSquirrelY - vMargin) / cellSize);

            if (mazeLayout[mazeY][mazeX] != 1) {
                // Move the squirrel based on the joystick direction
                squirrelX += joystickDirectionX * 100; // Adjust speed as needed
                squirrelY += joystickDirectionY * 100; // Adjust speed as needed
            }
        }
    }

        private boolean isValidPosition(float x, float y) {
        // Check if the position is within the maze boundaries
        if (x < hMargin || x >= getWidth() - hMargin || y < vMargin || y >= getHeight() - vMargin) {
            return false; // Out of bounds
        }

        // Check if the position corresponds to a wall
        int mazeX = (int) ((x - hMargin) / cellSize);
        int mazeY = (int) ((y - vMargin) / cellSize);
        return mazeLayout[mazeY][mazeX] == 0; // Return true if it's not a wall
    }
}

//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.util.AttributeSet;
//import android.view.MotionEvent;
//import android.view.View;
//
//public class Maze extends View {
//
//    private Bitmap squirrelBitmap;
//    private float squirrelX, squirrelY;
//
//    private float joystickCenterX, joystickCenterY;
//    private float joystickRadius;
//    private Paint joystickPaint;
//    private float joystickDirectionX, joystickDirectionY;
//
//    public Maze(Context context, AttributeSet attrs) {
//        super(context, attrs);
//
//        // Initialize squirrel image and position
//        squirrelBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.squirrel_image);
//        squirrelX = 0;
//        squirrelY = 0;
//
//        // Initialize joystick properties
//        joystickCenterX = 100;
//        joystickCenterY = 100;
//        joystickRadius = 100;
//        joystickPaint = new Paint();
//        joystickPaint.setColor(Color.RED);
//        joystickPaint.setStyle(Paint.Style.FILL);
//        joystickDirectionX = 0;
//        joystickDirectionY = 0;
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        // Draw squirrel at the current position
//        float squirrelWidth = 100;
//        float squirrelHeight = 100;
//        Bitmap scaledSquirrelBitmap = Bitmap.createScaledBitmap(squirrelBitmap, (int) squirrelWidth, (int) squirrelHeight, false);
//
//        // Draw the scaled squirrel bitmap at the squirrel's current position
//        canvas.drawBitmap(scaledSquirrelBitmap, this.squirrelX, this.squirrelY, null);
//
//        // Draw joystick center
//        canvas.drawCircle(joystickCenterX, joystickCenterY, 20, joystickPaint);
//
//        // Draw joystick direction line
//        float endX = joystickCenterX + joystickDirectionX * joystickRadius;
//        float endY = joystickCenterY + joystickDirectionY * joystickRadius;
//        canvas.drawLine(joystickCenterX, joystickCenterY, endX, endY, joystickPaint);
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        float x = event.getX();
//        float y = event.getY();
//
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_DOWN:
//            case MotionEvent.ACTION_MOVE:
//                // Calculate the distance from the joystick center
//                float dx = x - joystickCenterX;
//                float dy = y - joystickCenterY;
//                float distance = (float) Math.sqrt(dx * dx + dy * dy);
//
//                // If the touch is within the joystick radius, update the joystick position
//                if (distance <= joystickRadius) {
//                    joystickDirectionX = dx / joystickRadius;
//                    joystickDirectionY = dy / joystickRadius;
//                    moveSquirrel(); // Move the squirrel
//                    invalidate(); // Redraw the view
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                // Reset the joystick direction when the touch is released
//                joystickDirectionX = 0;
//                joystickDirectionY = 0;
//                invalidate(); // Redraw the view
//                break;
//        }
//
//        return true;
//    }
//
//    private void moveSquirrel() {
//        // Move the squirrel based on the joystick direction
//        squirrelX += joystickDirectionX * 200; // Adjust speed as needed
//        squirrelY += joystickDirectionY * 200; // Adjust speed as needed
//
//        // Ensure the squirrel stays within the view bounds
////        squirrelX = Math.max(0, Math.min(getWidth() - squirrelBitmap.getWidth(), squirrelX));
////        squirrelY = Math.max(0, Math.min(getHeight() - squirrelBitmap.getHeight(), squirrelY));
//    }

