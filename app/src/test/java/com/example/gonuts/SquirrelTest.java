package com.example.gonuts;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests the Squirrel class to ensure that all movement methods function correctly.
 * This test suite checks that the Squirrel moves the correct distance in the correct direction
 * when each movement method is called.
 *
 * The Squirrel class is assumed to have simple movement logic, adjusting its x and y coordinates
 * based on a fixed speed value when moveUp(), moveDown(), moveLeft(), and moveRight() methods are called.
 */
public class SquirrelTest {
    private Squirrel squirrel;

    /**
     * Sets up the environment for each test. This method is executed before each test method.
     * It initializes the Squirrel object with a known starting position (100, 100) to ensure
     * consistent and predictable testing conditions.
     */
    @Before
    public void setUp() {
        // Initialize Squirrel at position (100, 100)
        squirrel = new Squirrel(100, 100);
    }

    /**
     * Tests the moveUp() method of the Squirrel class.
     * Ensures that calling moveUp() decreases the y-coordinate by the squirrel's speed.
     * Expected result is y = 95, given the initial y-coordinate is 100 and the speed is 5.
     */
    @Test
    public void testMoveUp() {
        squirrel.moveUp();
        assertEquals("Moving up decreases y by 5", 95, squirrel.getY());
    }

    /**
     * Tests the moveDown() method of the Squirrel class.
     * Ensures that calling moveDown() increases the y-coordinate by the squirrel's speed.
     * Expected result is y = 105, given the initial y-coordinate is 100 and the speed is 5.
     */
    @Test
    public void testMoveDown() {
        squirrel.moveDown();
        assertEquals("Moving down increases y by 5", 105, squirrel.getY());
    }

    /**
     * Tests the moveLeft() method of the Squirrel class.
     * Ensures that calling moveLeft() decreases the x-coordinate by the squirrel's speed.
     * Expected result is x = 95, given the initial x-coordinate is 100 and the speed is 5.
     */
    @Test
    public void testMoveLeft() {
        squirrel.moveLeft();
        assertEquals("Moving left decreases x by 5", 95, squirrel.getX());
    }

    /**
     * Tests the moveRight() method of the Squirrel class.
     * Ensures that calling moveRight() increases the x-coordinate by the squirrel's speed.
     * Expected result is x = 105, given the initial x-coordinate is 100 and the speed is 5.
     */
    @Test
    public void testMoveRight() {
        squirrel.moveRight();
        assertEquals("Moving right increases x by 5", 105, squirrel.getX());
    }
}
