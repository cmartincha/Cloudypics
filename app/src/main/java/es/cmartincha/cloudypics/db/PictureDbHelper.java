package es.cmartincha.cloudypics.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Carlos on 14/06/2015.
 */
public class PictureDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Cloudypics.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + PictureDb.PictureEntry.TABLE_NAME + " (" +
                    PictureDb.PictureEntry._ID + " INTEGER PRIMARY KEY," +
                    PictureDb.PictureEntry.COLUMN_NAME_PATH + " TEXT);";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + PictureDb.PictureEntry.TABLE_NAME;

    public PictureDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
