package org.knuth.multimediaremote.android.addressbook;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * @author Lukas Knuth
 * @version 1.0
 * A helper-class for the Database-connection.
 */
final class DatabaseHelper extends SQLiteOpenHelper {

    /** The file-name of the SQLite Database */
    private final static String DB_NAME = "servers.db";
    /** The current version-number of the Database */
    private final static int DB_VERSION = 0;

    /**
     * Creates a new Database-Helper to open a connection
     *  to the SQLite-Database.
     * @param context the applications context to open
     *  the Database.
     */
    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /**
     * Executed when the Database is first created (if
     *  the Database-file does not already exist).
     * @param sqLiteDatabase the Database (writable).
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Create Database-structure:
        final String sql = "CREATE TABLE servers (" +
                "name TEXT," +
                "address TEXT," +
                "port INTEGER," +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT" +
                ")";
        sqLiteDatabase.execSQL(sql);
    }

    /**
     * Executed when the Database is updated to a newer version.
     * @param sqLiteDatabase the (writable) Database.
     * @param i the old version-number.
     * @param i1 the new version-number.
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
