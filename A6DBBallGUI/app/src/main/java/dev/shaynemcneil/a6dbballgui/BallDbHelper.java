package dev.shaynemcneil.a6dbballgui;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * A helper class to manage database creation and version management for the Balls database.
 * This class abstracts the low-level details of creating, opening, and upgrading the database.
 */
public class BallDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Balls.db";

    // The SQL statement for creating the balls table.
    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + BallContract.BallEntry.TABLE_NAME + " (" +
                    BallContract.BallEntry.COLUMN_NAME_NAME + " TEXT PRIMARY KEY," +
                    BallContract.BallEntry.COLUMN_NAME_X + " REAL," +
                    BallContract.BallEntry.COLUMN_NAME_Y + " REAL," +
                    BallContract.BallEntry.COLUMN_NAME_DX + " REAL," +
                    BallContract.BallEntry.COLUMN_NAME_DY + " REAL," +
                    BallContract.BallEntry.COLUMN_NAME_RADIUS + " REAL," +
                    BallContract.BallEntry.COLUMN_NAME_COLOR + " TEXT)";

    // The SQL statement for deleting the balls table.
    private static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + BallContract.BallEntry.TABLE_NAME;

    /**
     * Constructor for BallDbHelper.
     * @param context The context of the application.
     */
    public BallDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should happen.
     * @param db The database.
     */
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    /**
     * Called when the database needs to be upgraded. The implementation
     * should use this method to drop tables, add tables, or do anything else it
     * needs to upgrade to the new schema version.
     * @param db The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for app data, so its upgrade policy is
        // to simply to discard the data and start over.
        recreateDb(db);
    }

    /**
     * A helper method to force the database to be recreated.
     * This is useful for tests or clearing all data.
     * It drops the existing table and calls onCreate() to build it again.
     * @param db The SQLiteDatabase instance.
     */
    public void recreateDb(SQLiteDatabase db) {
        db.execSQL(SQL_DELETE_TABLE);
        onCreate(db);
    }
}
