package popularmoviesstage2.udacity.com.popularmoviesstage2;

import android.net.Uri;
import android.provider.BaseColumns;

public class FavoritesContract {


    public static final String AUTHORITY = "popularmoviesstage2.udacity.com.popularmoviesstage2";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);


    public static final class FavoritesEntry implements BaseColumns {

        public static final String TABLE_NAME = "favorites";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_PLOT = "plot";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_POSTER = "poster";
        public static final String COLUMN_VOTE = "vote";


    }
}
