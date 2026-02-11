package com.codelab.basics;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PokemonDBHelper extends SQLiteOpenHelper implements PokemonDBInterface {

    public static final int DATABASE_VERSION = 5; // Incremented to force table recreation
    public static final String DATABASE_NAME = "PokemonDB.db";
    private static final String TABLE_NAME = "pokemon_table";

    // Column Names
    private static final String COL_ID = "id";
    private static final String COL_NAME = "name";
    private static final String COL_NUMBER = "pokedex_number";
    private static final String COL_POWER = "power_level";
    private static final String COL_DESC = "description";
    private static final String COL_ACCESS = "access_count";
    private static final String COL_IMAGE = "image_name"; // New Column

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    COL_NAME + " TEXT, " +
                    COL_NUMBER + " INTEGER, " +
                    COL_POWER + " INTEGER, " +
                    COL_DESC + " TEXT, " +
                    COL_ACCESS + " INTEGER DEFAULT 0, " +
                    COL_IMAGE + " TEXT)";

    private static final String SQL_DELETE_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public PokemonDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("PokemonDBHelper", "onCreate: " + SQL_CREATE_TABLE);
        db.execSQL(SQL_CREATE_TABLE);

        // Pre-load DB with Insert Statements (including image filenames)
        // Ensure these filenames match your drawable files (without .png/.jpg extension)
        db.execSQL("INSERT INTO " + TABLE_NAME + " (" + COL_NAME + ", " + COL_NUMBER + ", " + COL_POWER + ", " + COL_DESC + ", " + COL_ACCESS + ", " + COL_IMAGE + ") VALUES('Pikachu', 25, 55, 'Electric mouse pokemon that stores electricity in its cheeks.', 0, 'pikachu')");
        db.execSQL("INSERT INTO " + TABLE_NAME + " (" + COL_NAME + ", " + COL_NUMBER + ", " + COL_POWER + ", " + COL_DESC + ", " + COL_ACCESS + ", " + COL_IMAGE + ") VALUES('Charizard', 6, 90, 'Fire flying dragon that breathes hot fire.', 0, 'charizard')");
        db.execSQL("INSERT INTO " + TABLE_NAME + " (" + COL_NAME + ", " + COL_NUMBER + ", " + COL_POWER + ", " + COL_DESC + ", " + COL_ACCESS + ", " + COL_IMAGE + ") VALUES('Bulbasaur', 1, 45, 'Grass poison seed pokemon with a plant on its back.', 0, 'bulbasaur')");
        db.execSQL("INSERT INTO " + TABLE_NAME + " (" + COL_NAME + ", " + COL_NUMBER + ", " + COL_POWER + ", " + COL_DESC + ", " + COL_ACCESS + ", " + COL_IMAGE + ") VALUES('Mewtwo', 150, 110, 'Genetic psychic pokemon created in a lab.', 0, 'mewtwo')");
        db.execSQL("INSERT INTO " + TABLE_NAME + " (" + COL_NAME + ", " + COL_NUMBER + ", " + COL_POWER + ", " + COL_DESC + ", " + COL_ACCESS + ", " + COL_IMAGE + ") VALUES('Snorlax', 143, 80, 'Sleeping giant pokemon that blocks the road.', 0, 'snorlax')");
        db.execSQL("INSERT INTO " + TABLE_NAME + " (" + COL_NAME + ", " + COL_NUMBER + ", " + COL_POWER + ", " + COL_DESC + ", " + COL_ACCESS + ", " + COL_IMAGE + ") VALUES('Jigglypuff', 39, 35, 'Balloon singing pokemon that puts enemies to sleep.', 0, 'jigglypuff')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("PokemonDBHelper", "onUpgrade to version " + DATABASE_VERSION);
        db.execSQL(SQL_DELETE_TABLE);
        onCreate(db);
    }

    // Interface Implementation

    @Override
    public int count() {
        String countQuery = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int cnt = cursor.getCount();
        cursor.close();
        return cnt;
    }

    @Override
    public int save(Pokemon pokemon) {
        Log.v("PokemonDBHelper", "save=> " + pokemon.toString());
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COL_NAME, pokemon.getName());
        values.put(COL_NUMBER, pokemon.getPokedexNumber());
        values.put(COL_POWER, pokemon.getPowerLevel());
        values.put(COL_DESC, pokemon.getDescription());
        values.put(COL_ACCESS, pokemon.getAccessCount());
        values.put(COL_IMAGE, pokemon.getImageFileName()); // Save image name

        db.insert(TABLE_NAME, null, values);
        db.close();
        return 0;
    }

    @Override
    public int update(Pokemon pokemon) {
        // Implementation left as exercise/future requirement
        return 0;
    }

    @Override
    public int deleteById(Long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COL_ID + " = ?", new String[]{String.valueOf(id)});
    }

    private Random r = new Random();

    // Helper to add default rows if empty
    private void addDefaultRows() {
        int doCount = this.count();
        if (doCount == 0) {
            Log.v("PokemonDBHelper", "no rows in DB... adding defaults");
            // Note: passing default image 'pikachu' just to be safe, ideally use specific images
            this.save(new Pokemon(0, "Squirtle", 7, 48, "Tiny Turtle", 0, "pikachu"));
        }
    }

    @Override
    public List<Pokemon> findAll() {
        List<Pokemon> temp = new ArrayList<>();
        addDefaultRows(); // Ensure data exists

        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                // IMPORTANT: Column indexes must match the CREATE statement order
                // 0:id, 1:name, 2:number, 3:power, 4:desc, 5:access, 6:image
                Pokemon item = new Pokemon(
                        cursor.getLong(0),   // id
                        cursor.getString(1), // name
                        cursor.getInt(2),    // number
                        cursor.getInt(3),    // power
                        cursor.getString(4), // description
                        cursor.getInt(5),    // access_count
                        cursor.getString(6)  // image_name
                );
                temp.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return temp;
    }

    @Override
    public String getNameById(Long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COL_NAME}, COL_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(0);
            cursor.close();
            return name;
        }
        return null;
    }

    @Override
    public Pokemon getMaxPower() {
        // Returns the Pokemon with the highest Power Level
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY " + COL_POWER + " DESC LIMIT 1";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            Pokemon p = new Pokemon(
                    cursor.getLong(0),
                    cursor.getString(1),
                    cursor.getInt(2),
                    cursor.getInt(3),
                    cursor.getString(4),
                    cursor.getInt(5),
                    cursor.getString(6)
            );
            cursor.close();
            return p;
        }
        return null;
    }

    @Override
    public void incAccessCount(long id) {
        // Increment the access_count by 1 for the specific ID
        String cmdString = "UPDATE " + TABLE_NAME + " SET " + COL_ACCESS + " = " + COL_ACCESS + " + 1 WHERE " + COL_ID + "=" + id;
        Log.v("PokemonDBHelper", "incAccessCount SQL: " + cmdString);

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(cmdString);
        db.close();
    }

    @Override
    public long getMostAccessedId() {
        long mostID = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        // Query to get ID ordered by access_count descending, limit 1
        String query = "SELECT " + COL_ID + " FROM " + TABLE_NAME + " ORDER BY " + COL_ACCESS + " DESC LIMIT 1";

        Cursor cursor = db.rawQuery(query, null);
        if (cursor != null && cursor.moveToFirst()) {
            mostID = cursor.getLong(0);
            cursor.close();
        }
        return mostID;
    }
}