package org.knuth.multimediaremote.android.addressbook;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * @author Lukas Knuth
 * @version 1.0
 * The loader used to query the Database for all stored servers.
 */
public class AddressbookLoader extends SimpleCursorLoader {

    /** Context which is used to open the Database */
    private final Context context;

    /**
     * Creates the new loader.
     * @param context a context used to open the Database.
     */
    public AddressbookLoader(Context context) {
        super(context);
        this.context = context;
    }

    /**
     * Query's the database for all stored servers and returns the
     *  resulting cursor.
     * @return the result as a Cursor.
     */
    @Override
    public Cursor loadInBackground() {
        SQLiteDatabase db = new DatabaseHelper(context).getReadableDatabase();
        final String sql = "SELECT name, _id" +
                "FROM servers" +
                "ORDER BY name";
        return db.rawQuery(sql, null);
    }
}
