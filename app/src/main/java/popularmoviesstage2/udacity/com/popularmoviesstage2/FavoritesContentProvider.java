package popularmoviesstage2.udacity.com.popularmoviesstage2;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static popularmoviesstage2.udacity.com.popularmoviesstage2.FavoritesContract.FavoritesEntry.TABLE_NAME;

public class FavoritesContentProvider extends ContentProvider {
    private FavoritesDbHelper mFavoritesDbHelper;

    @Override
    public boolean onCreate() {
        mFavoritesDbHelper = new FavoritesDbHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        final SQLiteDatabase db = mFavoritesDbHelper.getReadableDatabase();

        return db.query(TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        final SQLiteDatabase db = mFavoritesDbHelper.getWritableDatabase();

        long id = db.insert(TABLE_NAME, null, values);


        return ContentUris.withAppendedId(FavoritesContract.BASE_CONTENT_URI, id);

    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        final SQLiteDatabase db = mFavoritesDbHelper.getWritableDatabase();

 /*       db.delete(TABLE_NAME, selection, selectionArgs);*/
 db.delete(TABLE_NAME,selection,selectionArgs);

        return 1;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {

        final SQLiteDatabase db = mFavoritesDbHelper.getWritableDatabase();
        String id = uri.getPathSegments().get(1);

        db.update(TABLE_NAME, values, "_id=?", new String[]{id});
        return 0;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }
}
