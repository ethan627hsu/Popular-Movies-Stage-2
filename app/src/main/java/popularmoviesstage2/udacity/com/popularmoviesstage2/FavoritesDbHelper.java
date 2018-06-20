package popularmoviesstage2.udacity.com.popularmoviesstage2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class FavoritesDbHelper extends SQLiteOpenHelper {

    public static final String dbName = "favorites.db";
    public static final int version = 1;

    public FavoritesDbHelper(Context context) {
        super(context, dbName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_FAV_TABLE = "CREATE TABLE " + FavoritesContract.FavoritesEntry.TABLE_NAME + " (" +
                FavoritesContract.FavoritesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                FavoritesContract.FavoritesEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL," +
                FavoritesContract.FavoritesEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                FavoritesContract.FavoritesEntry.COLUMN_PLOT + " TEXT NOT NULL, " +
                FavoritesContract.FavoritesEntry.COLUMN_DATE + " TEXT NOT NULL, " +
                FavoritesContract.FavoritesEntry.COLUMN_POSTER + " TEXT NOT NULL, " +
                FavoritesContract.FavoritesEntry.COLUMN_VOTE + " DOUBLE NOT NULL " +
                "); ";
        db.execSQL(CREATE_FAV_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + FavoritesContract.FavoritesEntry.TABLE_NAME);
        onCreate(db);
        Log.d("DBHELPER","Database version upgraded from " + oldVersion + " to " + newVersion);
    }
}
