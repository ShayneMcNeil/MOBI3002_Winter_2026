package dev.shaynemcneil.a6dbballgui;

import android.provider.BaseColumns;

/**
 * A contract class for the Balls database schema.
 * A contract class is a container for constants that define names for URIs, tables, and columns.
 * It allows you to use the same constants across all the other classes in the same package.
 * This lets you change a column name in one place and have it propagate throughout your code.
 */
public final class BallContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private BallContract() {}

    /**
     * Defines the table contents for the 'balls' table.
     * By implementing the BaseColumns interface, your inner class can inherit a primary key column
     * called _ID, which some Android classes such as cursor adapters expect it to have.
     */
    public static class BallEntry implements BaseColumns {
        public static final String TABLE_NAME = "balls";
        public static final String COLUMN_NAME_NAME = "name"; // Ball's unique name (Primary Key)
        public static final String COLUMN_NAME_X = "x"; // X-coordinate
        public static final String COLUMN_NAME_Y = "y"; // Y-coordinate
        public static final String COLUMN_NAME_DX = "dx"; // Horizontal speed
        public static final String COLUMN_NAME_DY = "dy"; // Vertical speed
        public static final String COLUMN_NAME_RADIUS = "radius"; // Ball's radius
        public static final String COLUMN_NAME_COLOR = "color"; // Ball's color as a hex string
    }
}
