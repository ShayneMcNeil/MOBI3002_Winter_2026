package dev.shaynemcneil.a6dbballgui;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Acts as a data source for Ball objects, abstracting away all database operations.
 * This class provides a clean API for the rest of the application to interact with
 * the database without needing to know any SQL or database-specific details.
 */
public class BallDataSource {

    // The database instance.
    private SQLiteDatabase database;
    // The database helper used to manage the database creation and versioning.
    private final BallDbHelper dbHelper;

    // Defines the columns that will be retrieved from the database in queries.
    private final String[] allColumns = {
            BallContract.BallEntry.COLUMN_NAME_NAME,
            BallContract.BallEntry.COLUMN_NAME_X,
            BallContract.BallEntry.COLUMN_NAME_Y,
            BallContract.BallEntry.COLUMN_NAME_DX,
            BallContract.BallEntry.COLUMN_NAME_DY,
            BallContract.BallEntry.COLUMN_NAME_RADIUS,
            BallContract.BallEntry.COLUMN_NAME_COLOR
    };

    /**
     * Constructor for BallDataSource.
     * @param context The application context, used to initialize the dbHelper.
     */
    public BallDataSource(Context context) {
        dbHelper = new BallDbHelper(context);
    }

    /**
     * Opens the database connection so it can be used.
     * This should be called before any other database operations.
     * @throws SQLException if the database cannot be opened for writing.
     */
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    /**
     * Closes the database connection to release resources.
     * This should be called when the data source is no longer needed.
     */
    public void close() {
        dbHelper.close();
    }

    /**
     * Creates a new ball, saves it to the database, and returns the corresponding Ball object.
     * If a ball with the same name already exists, it will be updated (replaced).
     *
     * @param name The unique name for the ball.
     * @param x The initial x-coordinate.
     * @param y The initial y-coordinate.
     * @param dx The initial horizontal speed.
     * @param dy The initial vertical speed.
     * @param radius The radius of the ball.
     * @param colorStr The color of the ball as a hex string (e.g., "#FF0000").
     * @return The newly created Ball object.
     */
    public Ball createBall(String name, float x, float y, float dx, float dy, float radius, String colorStr) {
        int color;
        // Safely parse the color string. If it's invalid, use a default color.
        try {
            color = Color.parseColor(colorStr);
        } catch (IllegalArgumentException e) {
            color = Color.BLACK; // Default color
            Log.e("BallDataSource", "Invalid color string: " + colorStr, e);
        }

        // Use ContentValues to store the key-value pairs for the database record.
        ContentValues values = new ContentValues();
        values.put(BallContract.BallEntry.COLUMN_NAME_NAME, name);
        values.put(BallContract.BallEntry.COLUMN_NAME_X, x);
        values.put(BallContract.BallEntry.COLUMN_NAME_Y, y);
        values.put(BallContract.BallEntry.COLUMN_NAME_DX, dx);
        values.put(BallContract.BallEntry.COLUMN_NAME_DY, dy);
        values.put(BallContract.BallEntry.COLUMN_NAME_RADIUS, radius);
        // Store the color as a hex string for consistency.
        values.put(BallContract.BallEntry.COLUMN_NAME_COLOR, String.format("#%06X", (0xFFFFFF & color)));

        // Insert the new record into the database. CONFLICT_REPLACE ensures an update if the name already exists.
        database.insertWithOnConflict(BallContract.BallEntry.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

        // Return a new Ball object representing the data we just saved.
        return new Ball(name, x, y, dx, dy, radius, color);
    }

    /**
     * Deletes all balls from the database by dropping and recreating the table.
     * This provides a robust way to ensure a clean state, especially for testing.
     */
    public void deleteAllBalls() {
        dbHelper.recreateDb(database);
    }

    /**
     * Retrieves all balls from the database.
     * @return A List of all Ball objects found in the database. The list will be empty if the database is empty.
     */
    public List<Ball> getAllBalls() {
        List<Ball> balls = new ArrayList<>();
        // Use a try-with-resources statement to ensure the cursor is automatically closed.
        try (Cursor cursor = database.query(BallContract.BallEntry.TABLE_NAME, allColumns, null, null, null, null, null)) {
            // The while loop correctly handles an empty cursor and iterates through all rows.
            while (cursor.moveToNext()) {
                // Convert the current cursor row to a Ball object and add it to the list.
                Ball ball = cursorToBall(cursor);
                balls.add(ball);
            }
        }
        return balls;
    }

    /**
     * A private helper method to map a row from the database (via a Cursor) to a Ball object.
     * @param cursor The cursor positioned at the row to be converted.
     * @return A new Ball object containing the data from the cursor row.
     */
    private Ball cursorToBall(Cursor cursor) {
        String name = cursor.getString(cursor.getColumnIndexOrThrow(BallContract.BallEntry.COLUMN_NAME_NAME));
        float x = cursor.getFloat(cursor.getColumnIndexOrThrow(BallContract.BallEntry.COLUMN_NAME_X));
        float y = cursor.getFloat(cursor.getColumnIndexOrThrow(BallContract.BallEntry.COLUMN_NAME_Y));
        float dx = cursor.getFloat(cursor.getColumnIndexOrThrow(BallContract.BallEntry.COLUMN_NAME_DX));
        float dy = cursor.getFloat(cursor.getColumnIndexOrThrow(BallContract.BallEntry.COLUMN_NAME_DY));
        float radius = cursor.getFloat(cursor.getColumnIndexOrThrow(BallContract.BallEntry.COLUMN_NAME_RADIUS));
        String colorStr = cursor.getString(cursor.getColumnIndexOrThrow(BallContract.BallEntry.COLUMN_NAME_COLOR));

        // Create and return the Ball object, parsing the color string back to an integer.
        return new Ball(name, x, y, dx, dy, radius, Color.parseColor(colorStr));
    }
}
