package dev.shaynemcneil.a6dbballgui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.graphics.Color;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.List;

/**
 * Unit tests for the BallDataSource.
 * These tests verify that the database API (create, read, update, delete) works correctly.
 * Robolectric is used to simulate an Android environment, allowing tests to run on a local JVM.
 */
@RunWith(RobolectricTestRunner.class)
public class BallDataSourceTest {

    private BallDataSource dataSource;

    /**
     * This method runs before each test. It sets up a fresh database connection.
     */
    @Before
    public void setUp() {
        Context context = RuntimeEnvironment.getApplication();
        dataSource = new BallDataSource(context);
        dataSource.open();
        // Start with a clean slate for every test
        dataSource.deleteAllBalls();
    }

    /**
     * This method runs after each test. It closes the database connection to release resources.
     */
    @After
    public void tearDown() {
        dataSource.close();
    }

    /**
     * Tests saving a single ball and retrieving it, verifying all its properties.
     */
    @Test
    public void testAddAndRetrieveSingleBall() {
        // 1. Create and save a ball with known properties
        dataSource.createBall("TestBall", 10.5f, 20.2f, 5.1f, -5.3f, 50f, "#FF00FF");

        // 2. Retrieve all balls from the database
        List<Ball> balls = dataSource.getAllBalls();

        // 3. Assert and verify the results
        assertEquals("Should be exactly one ball in the database", 1, balls.size());

        Ball retrievedBall = balls.get(0);
        assertNotNull("Retrieved ball should not be null", retrievedBall);

        // Assert that every property was saved and retrieved correctly
        assertEquals("Name should match", "TestBall", retrievedBall.ballName);
        assertEquals("X coordinate should match", 10.5f, retrievedBall.x, 0.001);
        assertEquals("Y coordinate should match", 20.2f, retrievedBall.y, 0.001);
        assertEquals("dX speed should match", 5.1f, retrievedBall.dx, 0.001);
        assertEquals("dY speed should match", -5.3f, retrievedBall.dy, 0.001);
        assertEquals("Radius should match", 50f, retrievedBall.radius, 0.001);
        assertEquals("Color should match", Color.parseColor("#FF00FF"), retrievedBall.color);
    }

    /**
     * Tests that clearing the database correctly removes all entries.
     */
    @Test
    public void testDeleteAllBalls() {
        // 1. Add a couple of balls
        dataSource.createBall("Ball1", 1, 1, 1, 1, 1, "#000001");
        dataSource.createBall("Ball2", 2, 2, 2, 2, 2, "#000002");

        // Verify they were added
        assertEquals("Should be 2 balls before deleting", 2, dataSource.getAllBalls().size());

        // 2. Delete all balls
        dataSource.deleteAllBalls();

        // 3. Assert that the database is now empty
        List<Ball> ballsAfterDelete = dataSource.getAllBalls();
        assertTrue("Database should be empty after deletion", ballsAfterDelete.isEmpty());
    }

    /**
     * Tests the update (CONFLICT_REPLACE) functionality by saving a ball with a name that already exists.
     */
    @Test
    public void testUpdateBall() {
        // 1. Create and save an initial ball
        dataSource.createBall("UpdatedBall", 0f, 0f, 0f, 0f, 10f, "#000000");

        // 2. Create a new ball with the SAME name but different properties
        dataSource.createBall("UpdatedBall", 100f, 100f, 5f, 5f, 20f, "#FFFFFF");

        // 3. Retrieve the balls
        List<Ball> balls = dataSource.getAllBalls();

        // 4. Assert that there is still only one ball and its properties have been updated
        assertEquals("Should still be only one ball due to update", 1, balls.size());

        Ball updatedBall = balls.get(0);
        assertEquals("X should be updated", 100f, updatedBall.x, 0.001);
        assertEquals("Radius should be updated", 20f, updatedBall.radius, 0.001);
        assertEquals("Color should be updated", Color.parseColor("#FFFFFF"), updatedBall.color);
    }
}
