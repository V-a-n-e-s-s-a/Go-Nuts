package com.example.gonuts;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
/**
 * A class for testing the functionality of the Projectile class. Tests include
 * methods to ensure that projectile position updates correctly according to its velocity
 * and that collision detection logic is functioning as expected.
 */
public class ProjectileTest {
    private Projectile projectile;

    /**
     * Sets up the testing environment before each test method is executed.
     * This method initializes a Projectile object at a known starting position with
     * a specified velocity to ensure predictable behavior in tests.
     */
    @Before
    public void setUp() {
        // Projectile starts at (100, 100) moving at (10, 5) per update
        projectile = new Projectile(100, 100, 10, 5);
    }

    /**
     * Tests the update method of the Projectile class. Verifies that the projectile's
     * position is updated correctly based on its initial velocity.
     */
    @Test
    public void testUpdate() {
        projectile.update();
        assertEquals("X position should be updated by velocityX", 110, projectile.getX(), 0.01);
        assertEquals("Y position should be updated by velocityY", 105, projectile.getY(), 0.01);
    }

    /**
     * Tests the checkCollision method of the Projectile class. Ensures that the collision detection
     * accurately identifies when the projectile is sufficiently close to a specified target point,
     * and when it is not, based on the target's size.
     */
    @Test
    public void testCheckCollision() {
        assertTrue("Projectile should detect collision with target at (110, 105)", 
                   projectile.checkCollision(110, 105, 5));
        assertFalse("Projectile should not detect collision with target far away", 
                    projectile.checkCollision(200, 200, 5));
    }
}
